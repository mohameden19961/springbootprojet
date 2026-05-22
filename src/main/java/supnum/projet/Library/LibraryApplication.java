package supnum.projet.Library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {
    public static void main(String[] args) {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        if (url != null && !url.startsWith("jdbc:")) {
            System.setProperty("spring.datasource.url", "jdbc:" + url);
        }
        System.out.println("=== DEBUG === URL: " + System.getProperty("spring.datasource.url"));
        SpringApplication.run(LibraryApplication.class, args);
    }
}
