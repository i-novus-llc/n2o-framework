package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.camunda.CamundaDataProviderEngine;
import net.n2oapp.framework.boot.camunda.CamundaProxyEngine;
import net.n2oapp.framework.boot.camunda.EmbeddedCamundaProxyEngine;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(BpmPlatform.class)
public class N2oCamundaAutoConfiguration {

    @Value("${n2o.engine.camunda.rest_url:}")
    private String restUrl;

    @Bean
    @ConditionalOnMissingBean
    public CamundaDataProviderEngine camundaDataProviderEngine(@Autowired CamundaProxyEngine camundaEngine) {
        return new CamundaDataProviderEngine(camundaEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "n2o.engine.camunda.rest_url", matchIfMissing = true)
    public CamundaProxyEngine embeddedCamundaEngine(@Autowired ProcessEngine processEngine) {
        return new EmbeddedCamundaProxyEngine(processEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "n2o.engine.camunda.rest_url")
    public CamundaProxyEngine restCamundaEngine() {
        return null; //TODO
    }
}
