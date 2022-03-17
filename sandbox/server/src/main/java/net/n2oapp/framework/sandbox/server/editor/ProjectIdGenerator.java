package net.n2oapp.framework.sandbox.server.editor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Random;

@Component
public class ProjectIdGenerator {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Random random = new Random();
    private int attempts;
    private int length;

    public ProjectIdGenerator(@Value("${n2o.sandbox.length:5}") int length,
                              @Value("${n2o.sandbox.attempts:5}") int attempts) {
        this.length = length;
        this.attempts = attempts;
    }

    public String generate(String basePath) {
        int maxLength = length;
        do {
            String result = generateAndCheck(basePath, maxLength, attempts);
            if (result != null) {
                return result;
            } else {
                maxLength++;
            }
        } while (true);
    }

    private String generateAndCheck(String basePath, int length, int maxAttempts) {
        boolean exists;
        int attempt = 0;
        String candidate;
        do {
            attempt++;
            candidate = generateString(length);
            exists = Paths.get(basePath, candidate).toFile().exists();
        } while (exists && attempt < maxAttempts);
        if (!exists) {
            return candidate;
        } else {
            return null;
        }
    }

    private String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char letter = ALPHABET.charAt(random.nextInt(ALPHABET.length()));
            sb.append(letter);
        }
        return sb.toString();
    }
}
