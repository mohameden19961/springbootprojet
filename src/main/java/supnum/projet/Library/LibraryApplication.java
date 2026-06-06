package supnum.projet.Library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibraryApplication {
    public static void main(String[] args) {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        if (url != null && url.startsWith("postgresql://")) {
            try {
                String rest = url.substring("postgresql://".length());
                // rest = "user:password@host/db" or "host/db"
                String[] atSplit = rest.split("@", 2);
                String hostPortDb;
                String user = null;
                String password = null;
                if (atSplit.length == 2) {
                    String[] userPass = atSplit[0].split(":", 2);
                    user = userPass[0];
                    password = userPass.length > 1 ? userPass[1] : "";
                    hostPortDb = atSplit[1];
                } else {
                    hostPortDb = atSplit[0];
                }
                String[] hostDbSplit = hostPortDb.split("/", 2);
                String host = hostDbSplit[0];
                String db = hostDbSplit.length > 1 ? hostDbSplit[1] : "";

                String jdbcUrl = "jdbc:postgresql://" + host + ":5432/" + db;
                System.setProperty("spring.datasource.url", jdbcUrl);
                if (user != null) {
                    System.setProperty("spring.datasource.username", user);
                    System.setProperty("spring.datasource.password", password);
                }
            } catch (Exception e) {
                System.err.println("Impossible de parser SPRING_DATASOURCE_URL, utilisation des valeurs par défaut");
            }
        }
        SpringApplication.run(LibraryApplication.class, args);
    }
}
