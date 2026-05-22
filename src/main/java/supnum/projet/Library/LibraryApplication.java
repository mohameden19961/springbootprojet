package supnum.projet.Library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {
    public static void main(String[] args) {
        String url = System.getenv("SPRING_DATASOURCE_URL");
        if (url != null) {
            // Convertir postgresql:// en jdbc:postgresql:// avec port 5432
            url = url.replace("postgresql://", "");
            // url = "user:password@host/db"
            String[] atSplit = url.split("@");
            String userInfo = atSplit[0]; // user:password
            String hostDb = atSplit[1];   // host/db
            String[] hostDbSplit = hostDb.split("/");
            String host = hostDbSplit[0];
            String db = hostDbSplit[1];
            String[] userPass = userInfo.split(":");
            String user = userPass[0];
            String password = userPass[1];
            
            String jdbcUrl = "jdbc:postgresql://" + host + ":5432/" + db;
            System.setProperty("spring.datasource.url", jdbcUrl);
            System.setProperty("spring.datasource.username", user);
            System.setProperty("spring.datasource.password", password);
            System.out.println("=== JDBC URL: " + jdbcUrl);
        }
        SpringApplication.run(LibraryApplication.class, args);
    }
}
