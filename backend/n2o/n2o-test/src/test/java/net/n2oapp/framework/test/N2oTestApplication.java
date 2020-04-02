package net.n2oapp.framework.test;

import net.n2oapp.framework.boot.sql.jdbc.EnableRoutingDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableRoutingDataSource
@ComponentScan
public class N2oTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(N2oTestApplication.class, args);
    }

}
