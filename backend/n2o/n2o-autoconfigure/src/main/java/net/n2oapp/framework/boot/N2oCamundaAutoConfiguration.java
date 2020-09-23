package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.camunda.*;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class N2oCamundaAutoConfiguration {

    @Bean
    @ConditionalOnClass(ProcessEngine.class)
    public CamundaProxyEngine embeddedCamundaEngine() {
        return new EmbeddedCamundaProxyEngine();
    }

    @Bean
    @ConditionalOnProperty(name = "n2o.engine.camunda.rest_url")
    public CamundaProxyEngine restCamundaEngine() {
        return new RestCamundaProxyEngine();
    }

    @Bean
    @ConditionalOnBean(CamundaProxyEngine.class)
    public CamundaDataProviderEngine camundaDataProviderEngine() {
        return new CamundaDataProviderEngine();
    }
}
