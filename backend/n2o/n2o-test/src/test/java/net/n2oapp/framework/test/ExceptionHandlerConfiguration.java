package net.n2oapp.framework.test;

import net.n2oapp.framework.api.data.OperationExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    @Primary
    public OperationExceptionHandler operationExceptionHandler() {
        return new TestOperationExceptionHandler();
    }

}
