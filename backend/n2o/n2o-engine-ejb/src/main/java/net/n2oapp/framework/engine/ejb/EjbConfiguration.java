package net.n2oapp.framework.engine.ejb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.NamingException;

@Configuration
public class EjbConfiguration {
    @Bean
    public EjbInvocationEngine ejbInvocationEngine() {
        return new EjbInvocationEngine();
    }

    @Bean
    public EjbObjectLocator ejbObjectLocator() throws NamingException {
        return new EjbObjectLocator();
    }
}
