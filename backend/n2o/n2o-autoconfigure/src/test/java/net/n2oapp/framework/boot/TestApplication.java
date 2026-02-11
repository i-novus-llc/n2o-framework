package net.n2oapp.framework.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Тестовое приложение для интеграционных тестов CSRF
 */
@SpringBootApplication(exclude = N2oCamundaAutoConfiguration.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer, Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer) throws Exception {
        // Настройка CSRF защиты
        http.csrf(csrfCustomizer);
        http.cors(corsCustomizer);
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    /**
     * Тестовый REST контроллер для проверки CSRF защиты
     */
    @RestController
    @RequestMapping("/api")
    public static class TestController {

        @GetMapping("/test/data")
        public ResponseEntity<Map<String, String>> getData() {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "GET request successful");
            return ResponseEntity.ok(response);
        }

        @PostMapping("/test/data")
        public ResponseEntity<Map<String, String>> createData(@RequestBody Map<String, Object> data) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "POST request successful");
            response.put("received", data.toString());
            return ResponseEntity.ok(response);
        }

        @PutMapping("/test/data/{id}")
        public ResponseEntity<Map<String, String>> updateData(
                @PathVariable String id,
                @RequestBody Map<String, Object> data) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "PUT request successful");
            response.put("id", id);
            response.put("received", data.toString());
            return ResponseEntity.ok(response);
        }

        @DeleteMapping("/test/data/{id}")
        public ResponseEntity<Map<String, String>> deleteData(@PathVariable String id) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "DELETE request successful");
            response.put("id", id);
            return ResponseEntity.ok(response);
        }

        @PatchMapping("/test/data/{id}")
        public ResponseEntity<Map<String, String>> patchData(
                @PathVariable String id,
                @RequestBody Map<String, Object> data) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "PATCH request successful");
            response.put("id", id);
            response.put("received", data.toString());
            return ResponseEntity.ok(response);
        }

        @RequestMapping(value = "/test/data", method = RequestMethod.HEAD)
        public ResponseEntity<Void> headData() {
            return ResponseEntity.ok().build();
        }

        @RequestMapping(value = "/test/data", method = RequestMethod.OPTIONS)
        public ResponseEntity<Void> optionsData() {
            return ResponseEntity.ok().build();
        }

        // Публичный endpoint без CSRF защиты
        @PostMapping("/public/feedback")
        public ResponseEntity<Map<String, String>> submitFeedback(@RequestBody Map<String, Object> feedback) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Public endpoint - CSRF not required");
            response.put("feedback", feedback.toString());
            return ResponseEntity.ok(response);
        }
    }
}
