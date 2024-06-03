package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.camunda.CamundaDataProviderEngine;
import net.n2oapp.framework.boot.camunda.CamundaProxyEngine;
import net.n2oapp.framework.boot.camunda.EmbeddedCamundaProxyEngine;
import net.n2oapp.framework.boot.camunda.RestCamundaProxyEngine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class N2oCamundaAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = "org.camunda.bpm.engine.ProcessEngine")
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
