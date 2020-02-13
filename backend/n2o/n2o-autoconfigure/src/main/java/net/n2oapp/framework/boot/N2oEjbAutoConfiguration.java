package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.ejb.EjbInvocationEngine;
import net.n2oapp.framework.boot.ejb.EjbObjectLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ejb.EJB;
import javax.naming.NamingException;

@Configuration
@ConditionalOnClass(EJB.class)
public class N2oEjbAutoConfiguration {
    @Bean
    public EjbInvocationEngine ejbInvocationEngine() {
        return new EjbInvocationEngine();
    }

    @Bean
    public EjbObjectLocator ejbObjectLocator() throws NamingException {
        return new EjbObjectLocator();
    }
}
