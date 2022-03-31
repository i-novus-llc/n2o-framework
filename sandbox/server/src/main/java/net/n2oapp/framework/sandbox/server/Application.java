package net.n2oapp.framework.sandbox.server;

import net.n2oapp.framework.boot.N2oFrameworkAutoConfiguration;
import net.n2oapp.framework.sandbox.N2oSandboxConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {N2oFrameworkAutoConfiguration.class})
@Import({N2oSandboxConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
