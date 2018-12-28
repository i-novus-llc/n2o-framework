package net.n2oapp.framework.examples.master_detail;

import net.n2oapp.framework.engine.sql.jdbc.EnableRoutingDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRoutingDataSource
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
