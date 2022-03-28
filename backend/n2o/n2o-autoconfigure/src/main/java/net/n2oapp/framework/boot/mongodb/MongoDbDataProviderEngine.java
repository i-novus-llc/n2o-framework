package net.n2oapp.framework.boot.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import lombok.Setter;
import net.n2oapp.framework.api.PlaceHoldersResolver;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oMongoDbDataProvider;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.*;
import static net.n2oapp.framework.api.PlaceHoldersResolver.replaceOptional;

/**
 * Сервис для выполнения запросов к MongoDb
 */
@Setter
public class MongoDbDataProviderEngine implements MapInvocationEngine<N2oMongoDbDataProvider> {
    public static final String SELECT = "select";
    public static final String SORTING = "sorting";
    public static final String FILTERS = "filters";

    @Autowired
    private MongoClient mongoClient;
    @Value("${n2o.engine.mongodb.database_name:}")
    private String databaseName;
    @Value("${spring.data.mongodb.database:}")
    private String springDatabaseName;

    private ObjectMapper mapper;

    private static final Function<String, Integer> defaultSuffixIdx = str -> {
        if (str.startsWith("."))
            return 0;
        String[] ends = str.split("[^A-Za-z0-9_\\.]");
        return ends[0].replaceAll("\\.+$", "").length();
    };

    public MongoDbDataProviderEngine(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    @Override
    public Object invoke(N2oMongoDbDataProvider invocation, Map<String, Object> inParams) {
        String dbName = invocation.getDatabaseName() != null ? invocation.getDatabaseName() : !StringUtils.isEmpty(databaseName) ? databaseName : springDatabaseName;

        if (invocation.getConnectionUrl() == null) {
            MongoCollection<Document> collection = mongoClient
                    .getDatabase(dbName)
                    .getCollection(invocation.getCollectionName());
            return execute(invocation, inParams, collection);
        } else {
            ConnectionString connectionString = new ConnectionString(invocation.getConnectionUrl());
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            try (MongoClient mongoClient = MongoClients.create(mongoClientSettings)) {
                MongoCollection<Document> collection = mongoClient
                        .getDatabase(dbName)
                        .getCollection(invocation.getCollectionName());
                return execute(invocation, inParams, collection);
            }
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
                return countDocument(inParams, collection);
            default:
                throw new N2oException("Unsupported invocation's operation");
        }
    }

    private Object find(Map<String, Object> inParams, MongoCollection<Document> collection) {
        Bson order = getSorting(inParams);
        Bson projection = inParams.containsKey(SELECT) && inParams.get(SELECT) != null ?
                include((List<String>) inParams.get(SELECT)) : null;
        int limit = inParams.containsKey("limit") ? (int) inParams.get("limit") : 10;
        int offset = inParams.containsKey("offset") ? (int) inParams.get("offset") : 0;
        Bson filter = getFilters(inParams);
        if (filter == null)
            filter = new Document();
        List<Document> result = new ArrayList<>();
        for (Document document : collection.find(filter).projection(projection).sort(order).limit(limit).skip(offset))
            result.add(document);
        return result;
    }

    private Integer countDocument(Map<String, Object> inParams, MongoCollection<Document> collection) {
        Bson filter = getFilters(inParams);
        if (filter == null)
            filter = new Document();
        return (int) collection.countDocuments(filter);
    }

    private Bson getSorting(Map<String, Object> inParams) {
        Bson order = null;
        if (inParams.containsKey(SORTING) && inParams.get(SORTING) != null) {
            List<String> sortings = (List<String>) inParams.get(SORTING);
            List<Bson> sortFields = new ArrayList<>();
            for (String sort : sortings) {
                String[] str = sort.replace(" ", "").split(":");
                if ("desc".equals(inParams.get(str[1])))
                    sortFields.add(descending(str[0]));
                else
                    sortFields.add(ascending(str[0]));
            }
            order = orderBy(sortFields);
        }
        return order;
    }

    private Bson getFilters(Map<String, Object> inParams) {
        Bson filters = null;
        if (inParams.containsKey(FILTERS) && inParams.get(FILTERS) != null) {
            PlaceHoldersResolver resolver = new PlaceHoldersResolver("#", "", false, defaultSuffixIdx);
            List<String> filterList = (List<String>) inParams.get(FILTERS);
            List<Bson> filtersByFields = new ArrayList<>();
            for (String filter : filterList) {
                Bson f = BasicDBObject.parse(resolver.resolve(filter, PlaceHoldersResolver.replaceByJson(replaceOptional(inParams::get), mapper)));
                filtersByFields.add(f);
            }
            filters = filtersByFields.isEmpty() ? null : Filters.and(filtersByFields);
        }
        return filters;
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

        collection.updateOne(eq("_id", new ObjectId(id)), new Document("$set", new Document(data)));
        return null;
    }

    private Object deleteOne(Map<String, Object> inParams, MongoCollection<Document> collection) {
        if (!inParams.containsKey("id"))
            throw new N2oException("Id is required for operation \"deleteOne\"");

        collection.deleteOne(eq("_id", new ObjectId((String) inParams.get("id"))));
        return null;
    }

    private Object deleteMany(Map<String, Object> inParams, MongoCollection<Document> collection) {
        if (!inParams.containsKey("ids"))
            throw new N2oException("Ids is required for operation \"deleteMany\"");

        Object[] ids = ((List) inParams.get("ids")).stream().map(id -> new ObjectId((String) id)).toArray();
        collection.deleteMany(in("_id", ids));
        return null;
    }
}

