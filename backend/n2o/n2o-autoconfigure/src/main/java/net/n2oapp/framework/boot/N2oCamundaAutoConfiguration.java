package net.n2oapp.framework.boot;

import net.n2oapp.framework.boot.camunda.CamundaDataProviderEngine;
import net.n2oapp.framework.boot.camunda.CamundaEngine;
import net.n2oapp.framework.boot.camunda.EmbeddedCamundaEngine;
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
    public CamundaDataProviderEngine camundaDataProviderEngine(@Autowired CamundaEngine camundaEngine) {
        return new CamundaDataProviderEngine(camundaEngine);
    }

    @Bean
    @ConditionalOnProperty(name = "n2o.engine.camunda.rest_url", matchIfMissing = true)
    public CamundaEngine embeddedCamundaEngine(@Autowired ProcessEngine processEngine) {
        return new EmbeddedCamundaEngine(processEngine);
    }

    @Bean
    @ConditionalOnProperty(name = "n2o.engine.camunda.rest_url")
    public CamundaEngine restCamundaEngine() {
        return null; //TODO
    }
}
