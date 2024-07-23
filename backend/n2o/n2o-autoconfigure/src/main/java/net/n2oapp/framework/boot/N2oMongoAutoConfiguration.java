package net.n2oapp.framework.boot;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

import static net.n2oapp.framework.boot.ObjectMapperConstructor.dataObjectMapper;

@Configuration
@ConditionalOnClass(MongoClient.class)
public class N2oMongoAutoConfiguration {

    @Value("${n2o.engine.mongodb.connection_url:}")
    private String connectionUrl;


    @Deprecated
    private MongoClient mongo;

    @Bean
    @ConditionalOnMissingBean
    public MongoDbDataProviderEngine mongoDbDataProviderEngine() {
        return new MongoDbDataProviderEngine(dataObjectMapper());
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
}
