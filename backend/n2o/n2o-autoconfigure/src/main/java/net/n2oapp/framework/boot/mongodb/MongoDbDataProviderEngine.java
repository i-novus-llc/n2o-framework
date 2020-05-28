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

import java.util.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
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
                return countDocument(inParams, collection);
            default:
                throw new N2oException("Unsupported invocation's operation");
        }
    }

    private Object find(Map<String, Object> inParams, MongoCollection<Document> collection) {
        Bson order = getSorting(inParams);
        Bson projection = inParams.containsKey("select") && inParams.get("select") != null ?
                include((List<String>) inParams.get("select")) : null;
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
        if (inParams.containsKey("sorting") && inParams.get("sorting") != null) {
            List<String> sortings = (List<String>) inParams.get("sorting");
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
        if (inParams.containsKey("filters") && inParams.get("filters") != null) {
            List<String> filterList = (List<String>) inParams.get("filters");
            List<Bson> filtersByFields = new ArrayList<>();
            for (String filter : filterList) {
                Bson f = null;
                String[] splittedFilter = filter.replace(" ", "").split(":");
                String field = splittedFilter[0];
                Object pattern = inParams.get(splittedFilter[2]);

                switch (splittedFilter[1]) {
                    case "eq":
                        f = eqFilter(field, pattern);
                        break;
                    case "notEq":
                        f = notEqFilter(field, pattern);
                        break;
                    case "like":
                        f = likeFilter(field, pattern);
                        break;
                    case "likeStart":
                        f = likeStartFilter(field, pattern);
                        break;
                    case "in":
                        f = inFilter(field, pattern);
                        break;
                    case "notIn":
                        f = notInFilter(field, pattern);
                        break;
                    case "more":
                        f = moreFilter(field, pattern);
                        break;
                    case "less":
                        f = lessFilter(field, pattern);
                        break;
                    case "isNull":
                        f = isNullFilter(field);
                        break;
                    case "isNotNull":
                        f = isNotNullFilter(field);
                        break;
                    case "eqOrIsNull":
                        f = eqOrIsNullFilter(field, pattern);
                        break;
                    default:
                        throw new N2oException("Wrong filter type!");
                }

                if (f != null)
                    filtersByFields.add(f);
            }
            filters = filtersByFields.isEmpty() ? null : Filters.and(filtersByFields);
        }
        return filters;
    }

    private Bson eqFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null) {
            if ("id".equals(field))
                filter = eq("_id", new ObjectId((String) pattern));
            else
                filter = eq(field, pattern);
        }
        return filter;
    }

    private Bson notEqFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null) {
            if ("id".equals(field))
                filter = not(eq("_id", new ObjectId((String) pattern)));
            else
                filter = not(eq(field, pattern));
        }
        return filter;
    }

    private Bson likeFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null)
            filter = Filters.regex(field, "(?i)" + Pattern.quote((String) pattern));
        return filter;
    }

    private Bson likeStartFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null)
            filter = Filters.regex(field, "(?i)\\b" + Pattern.quote((String) pattern));
        return filter;
    }

    private Bson inFilter(String field, Object pattern) {
        Bson filter = null;
        List patterns = pattern instanceof List ? (List) pattern : Arrays.asList(pattern);
        if (!patterns.isEmpty()) {
            if ("id".equals(field))
                filter = in("_id", patterns.stream().map(id -> new ObjectId((String) id)).toArray());
            else
                filter = in(field, patterns);
        }
        return filter;
    }

    private Bson notInFilter(String field, Object pattern) {
        Bson filter = null;
        List patterns = pattern instanceof List ? (List) pattern : Arrays.asList(pattern);
        if (!patterns.isEmpty()) {
            if ("id".equals(field))
                filter = not(in("_id", patterns.stream().map(id -> new ObjectId((String) id)).toArray()));
            else
                filter = not(in(field, patterns));
        }
        return filter;
    }

    private Bson moreFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null) {
            if ("id".equals(field))
                filter = Filters.gt("_id", new ObjectId((String) pattern));
            else
                filter = Filters.gt(field, pattern);
        }
        return filter;
    }

    private Bson lessFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null) {
            if ("id".equals(field))
                filter = Filters.lt("_id", new ObjectId((String) pattern));
            else
                filter = Filters.lt(field, pattern);
        }
        return filter;
    }

    private Bson isNullFilter(String field) {
        return eq(field, null);
    }

    private Bson isNotNullFilter(String field) {
        return Filters.ne(field, null);
    }

    private Bson eqOrIsNullFilter(String field, Object pattern) {
        Bson filter = null;
        if (pattern != null)
            filter = Filters.or(eq(field, pattern), eq(field, null));
        return filter;
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

