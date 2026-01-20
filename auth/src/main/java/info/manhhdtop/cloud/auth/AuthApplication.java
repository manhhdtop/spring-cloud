package info.manhhdtop.cloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthApplication {
    static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
