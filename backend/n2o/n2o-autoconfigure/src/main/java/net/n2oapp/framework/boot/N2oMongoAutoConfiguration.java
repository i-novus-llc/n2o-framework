package net.n2oapp.framework.boot;

import com.mongodb.MongoClient;
import net.n2oapp.framework.boot.mongodb.MongoDbDataProviderEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MongoClient.class)
public class N2oMongoAutoConfiguration {

    @Value("${n2o.engine.mongodb.connection_url}")
    private String connectionUrl;

    @Value("${n2o.engine.mongodb.database_name}")
    private String databaseName;

    @Bean
    @ConditionalOnMissingBean
    public MongoDbDataProviderEngine mongoDbDataProviderEngine() {
        MongoDbDataProviderEngine mongoDbDataProviderEngine = new MongoDbDataProviderEngine();
        mongoDbDataProviderEngine.setConnectionUrl(connectionUrl);
        mongoDbDataProviderEngine.setDatabaseName(databaseName);
        return mongoDbDataProviderEngine;
    }
}
