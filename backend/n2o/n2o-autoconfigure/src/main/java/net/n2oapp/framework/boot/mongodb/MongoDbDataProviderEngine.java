package net.n2oapp.framework.boot.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import lombok.Setter;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import org.bson.Document;

import java.util.Map;

/**
 * Сервис для выполнения запросов к MongoDb
 */
@Setter
public class MongoDbDataProviderEngine implements MapInvocationEngine<N2oMongoDbDataProvider> {
    private String connectionUrl;
    private String databaseName;


    private MongoCollection<Document> getCollection(N2oMongoDbDataProvider invocation) {
        String connUrl = invocation.getConnectionUrl() != null ? invocation.getConnectionUrl() : connectionUrl;
        String dbName = invocation.getDatabaseName() != null ? invocation.getDatabaseName() : databaseName;
        return new MongoClient(new MongoClientURI(connUrl)).getDatabase(dbName)
                .getCollection(invocation.getCollectionName());
        // TODO close connection
    }

    @Override
    public Object invoke(N2oMongoDbDataProvider invocation, Map<String, Object> inParams) {
        return execute(invocation, inParams, getCollection(invocation));
    }

    @Override
    public Class<? extends N2oMongoDbDataProvider> getType() {
        return N2oMongoDbDataProvider.class;
    }

    private Object execute(N2oMongoDbDataProvider invocation, Map<String, Object> inParams, MongoCollection<Document> collection) {
        if (invocation.getOperation() == null)
            return find(inParams, collection);
        switch (invocation.getOperation()) {
            case find:
                return find(inParams, collection);
            case insertOne:
                return insertOne(invocation, inParams);
            case updateOne:
                return updateOne(invocation, inParams, collection);
            case updateMany:
                return updateMany(invocation, inParams, collection);
            case deleteOne:
                return deleteOne(invocation, inParams, collection);
            case deleteMany:
                return deleteMany(invocation, inParams, collection);
            case countDocuments:
                return collection.count();
            default:
                throw new N2oException("Unsupported invocation's operation");
        }
    }

    private Object find(Map<String, Object> inParams, MongoCollection<Document> collection) {
        return null;
    }

    private Object insertOne(N2oMongoDbDataProvider invocation, Map<String, Object> inParams) {
        return null;
    }

    private Object updateOne(N2oMongoDbDataProvider invocation, Map<String, Object> inParams, MongoCollection<Document> collection) {
        return null;
    }

    private Object updateMany(N2oMongoDbDataProvider invocation, Map<String, Object> inParams, MongoCollection<Document> collection) {
        return null;
    }

    private Object deleteOne(N2oMongoDbDataProvider invocation, Map<String, Object> inParams, MongoCollection<Document> collection) {
        return null;
    }

    private Object deleteMany(N2oMongoDbDataProvider invocation, Map<String, Object> inParams, MongoCollection<Document> collection) {
        return null;
    }
}

