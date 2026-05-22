package supnum.projet.Library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        String profile = System.getenv("SPRING_PROFILES_ACTIVE");
        System.out.println("=== DEBUG ===");
        System.out.println("SPRING_DATASOURCE_URL = " + url);
        System.out.println("SPRING_PROFILES_ACTIVE = " + profile);
        System.out.println("=== END DEBUG ===");
        SpringApplication.run(LibraryApplication.class, args);
    }
}