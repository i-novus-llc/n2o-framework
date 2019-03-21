package net.n2oapp.framework.engine.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.util.RestClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestEngineConfiguration {

    @Value("${n2o.engine.rest.dateformat.serialize}")
    private String serializingFormat;

    @Value("${n2o.engine.rest.dateformat.deserialize}")
    private String[] deserializingFormats;

    @Value("${n2o.engine.timeout}")
    private String timeoutInMillis;

    @Value("${n2o.engine.rest.url}")
    private String baseRestUrl;

    @Bean
    public RestInvocationEngine restInvocationEngine(RestClient restClient, RestProcessingEngine processing) {
        return new RestInvocationEngine(restClient, processing);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestDataProviderEngine restDataProviderEngine(ApacheRestClient restClient) {
        RestDataProviderEngine restDataProviderEngine = new RestDataProviderEngine(restClient, restClient.getObjectMapper());
        restDataProviderEngine.setBaseRestUrl(baseRestUrl);
        return restDataProviderEngine;
    }

    @Bean
    public RestProcessingEngine restProcessingEngine() {
        return new RestProcessingEngine();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApacheRestClient n2oRestClient(@Qualifier("restObjectMapper") ObjectMapper mapper) {
        ApacheRestClient restClient = new ApacheRestClient(timeoutInMillis);
        restClient.setMapper(mapper);
        return restClient;
    }
}
