package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import net.n2oapp.framework.engine.data.rest.json.RestEngineTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;

@Configuration
@ConditionalOnClass(MongoClient.class)
public class N2oMongoAutoConfiguration {

    @Value("${n2o.engine.mongodb.connection_url:}")
    private String connectionUrl;

    @Value("${n2o.engine.mongodb.dateformat.serialize}")
    private String serializingFormat;

    @Value("${n2o.engine.mongodb.dateformat.deserialize}")
    private String[] deserializingFormats;

    @Value("${n2o.engine.mongodb.dateformat.exclusion-keys}")
    private String[] exclusionKeys;

    @Deprecated
    private MongoClient mongo;

    @Bean
    @ConditionalOnMissingBean
    public MongoDbDataProviderEngine mongoDbDataProviderEngine() {
        MongoDbDataProviderEngine mongoDbDataProviderEngine = new MongoDbDataProviderEngine(mongoObjectMapper());
        return mongoDbDataProviderEngine;
    }

    @PreDestroy
    public void close() {
        if (mongo != null)
            mongo.close();
    }

    @Deprecated
    @Bean
    @ConditionalOnProperty(value = "n2o.engine.mongodb.connection_url")
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(connectionUrl);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongo = MongoClients.create(mongoClientSettings);
        return mongo;
    }

    private ObjectMapper mongoObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(serializingFormat));
        RestEngineTimeModule module = new RestEngineTimeModule(deserializingFormats, exclusionKeys);
        objectMapper.registerModules(module);
        return objectMapper;
    }
}
