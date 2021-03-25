package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import net.n2oapp.framework.engine.data.rest.json.RestEngineTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
@ConditionalOnClass(MongoClient.class)
public class N2oMongoAutoConfiguration {

    @Value("${n2o.engine.mongodb.connection_url:}")
    private String connectionUrl;

    @Value("${n2o.engine.mongodb.database_name:}")
    private String databaseName;

    @Value("${spring.data.mongodb.database:}")
    private String springDatabaseName;

    @Value("${n2o.engine.mongodb.dateformat.serialize}")
    private String serializingFormat;

    @Value("${n2o.engine.mongodb.dateformat.deserialize}")
    private String[] deserializingFormats;

    @Value("${n2o.engine.mongodb.dateformat.exclusion-keys}")
    private String[] exclusionKeys;


    @Bean
    @ConditionalOnMissingBean
    public MongoDbDataProviderEngine mongoDbDataProviderEngine(MongoClient mongoClient) {
        MongoDbDataProviderEngine mongoDbDataProviderEngine = new MongoDbDataProviderEngine(mongoClient,
                StringUtils.isEmpty(databaseName) ? springDatabaseName : databaseName,
                mongoObjectMapper());
        return mongoDbDataProviderEngine;
    }

    @Bean
    @ConditionalOnProperty(value = "n2o.engine.mongodb.connection_url")
    public MongoClient mongo() {
        return new MongoClient(new MongoClientURI(connectionUrl));
    }

    private ObjectMapper mongoObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(serializingFormat));
        RestEngineTimeModule module = new RestEngineTimeModule(deserializingFormats, exclusionKeys);
        objectMapper.registerModules(module);
        return objectMapper;
    }
}
