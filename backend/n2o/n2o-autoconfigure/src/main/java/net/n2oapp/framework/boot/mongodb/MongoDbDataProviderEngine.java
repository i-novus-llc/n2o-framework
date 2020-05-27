package net.n2oapp.framework.boot.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Setter;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.*;

/**
 * Сервис для выполнения запросов к MongoDb
 */
@Setter
public class MongoDbDataProviderEngine implements MapInvocationEngine<N2oMongoDbDataProvider> {
    private String connectionUrl;
    private String databaseName;
    private MongoClient mongoClient;

    private MongoCollection<Document> getCollection(N2oMongoDbDataProvider invocation) {
        String connUrl = invocation.getConnectionUrl() != null ? invocation.getConnectionUrl() : connectionUrl;
        String dbName = invocation.getDatabaseName() != null ? invocation.getDatabaseName() : databaseName;

        if (connUrl == null)
            throw new N2oException("Need to define n2o.engine.mongodb.connection_url property");

        mongoClient = new MongoClient(new MongoClientURI(connUrl));

        return mongoClient
                .getDatabase(dbName)
                .getCollection(invocation.getCollectionName());
    }

    @Override
    public Object invoke(N2oMongoDbDataProvider invocation, Map<String, Object> inParams) {
        try {
            return execute(invocation, inParams, getCollection(invocation));
        } finally {
            if (mongoClient != null)
                mongoClient.close();
        }
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
                return insertOne(inParams, collection);
            case updateOne:
                return updateOne(inParams, collection);
            case deleteOne:
                return deleteOne(inParams, collection);
            case deleteMany:
                return deleteMany(inParams, collection);
            case countDocuments:
                return collection.countDocuments();
            default:
                throw new N2oException("Unsupported invocation's operation");
        }
    }

    private Object find(Map<String, Object> inParams, MongoCollection<Document> collection) {
        List<Document> result = new ArrayList<>();
        Bson order = getSorting(inParams);
        Bson projection = inParams.containsKey("select") && inParams.get("select") != null ?
                include((List<String>) inParams.get("select")) : null;

        for (Document document : collection.find().projection(projection).sort(order))
            result.add(document);

        /*
        Bson filter;
        if (((List) inParams.get("filters")).get(0).equals("id :in :id")) {
            Object[] ids = ((List) inParams.get("id")).stream().map(id -> new ObjectId((String) id)).toArray();
            filter = Filters.in("_id", ids);
        } else
            filter = Filters.eq("_id", new ObjectId((String) inParams.get("id")));
*/

        return result;
    }

    private Bson getSorting(Map<String, Object> inParams) {
        Bson order = null;
        if (inParams.containsKey("sorting") && inParams.get("sorting") != null && inParams.get("sorting") != null) {
            List<String> sortings = (List<String>) inParams.get("sorting");
            List<String> asc = new ArrayList<>();
            List<String> desc = new ArrayList<>();
            for(String sort : sortings) {
                String[] str = sort.split(" :");
                String sortDirection = (String) inParams.get(str[1]);
                if (sortDirection.equals("asc")) {
                    asc.add(str[0]);
                } else {
                    desc.add(str[0]);
                }
            }
            if (!asc.isEmpty() && !desc.isEmpty()) {
                order = orderBy(ascending(asc), descending(desc));
            } else if (!asc.isEmpty()) {
                order = orderBy(ascending(asc));
            } else {
                order = orderBy(descending(desc));
            }
        }
        return order;
    }

    private Object insertOne(Map<String, Object> inParams, MongoCollection<Document> collection) {
        Document document = new Document(inParams);
        collection.insertOne(document);
        return document.get("_id").toString();
    }

    private Object updateOne(Map<String, Object> inParams, MongoCollection<Document> collection) {
        if (!inParams.containsKey("id"))
            throw new N2oException("Id is required for operation \"updateOne\"");

        String id = (String) inParams.get("id");
        Map<String, Object> data = new HashMap<>(inParams);
        data.remove("id");

        collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", new Document(data)));
        return null;
    }

    private Object deleteOne(Map<String, Object> inParams, MongoCollection<Document> collection) {
        if (!inParams.containsKey("id"))
            throw new N2oException("Id is required for operation \"deleteOne\"");

        collection.deleteOne(Filters.eq("_id", new ObjectId((String) inParams.get("id"))));
        return null;
    }

    private Object deleteMany(Map<String, Object> inParams, MongoCollection<Document> collection) {
        if (!inParams.containsKey("ids"))
            throw new N2oException("Ids is required for operation \"deleteMany\"");

        Object[] ids = ((List) inParams.get("ids")).stream().map(id -> new ObjectId((String) id)).toArray();
        collection.deleteMany(Filters.in("_id", ids));
        return null;
    }
}

