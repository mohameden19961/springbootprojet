package supnum.projet.Library.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter implements Filter {

    private final ConcurrentHashMap<String, long[]> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MILLIS = TimeUnit.MINUTES.toMillis(1);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        if (!path.equals("/api/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String ip = getClientIp(request);
        long now = System.currentTimeMillis();

        long[] record = requestCounts.compute(ip, (key, val) -> {
            if (val == null || now - val[0] > WINDOW_MILLIS) {
                return new long[]{now, 1};
            }
            val[1]++;
            return val;
        });

        if (record[1] > MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Trop de tentatives. Réessayez dans une minute.\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
