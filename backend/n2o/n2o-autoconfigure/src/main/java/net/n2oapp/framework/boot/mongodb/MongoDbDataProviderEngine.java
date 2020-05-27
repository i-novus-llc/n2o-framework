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
                if (filter.contains(":eqOrIsNull "))
                    f = eqOrIsNullFilter(filter, inParams);
                else if (filter.contains(":eq "))
                    f = eqFilter(filter, inParams);
                else if (filter.contains(":like "))
                    f = likeFilter(filter, inParams);
                else if (filter.contains(":in "))
                    f = inFilter(filter, inParams);
                else if (filter.contains(":more "))
                    f = moreFilter(filter, inParams);
                else if (filter.contains(":less "))
                    f = lessFilter(filter, inParams);
                else if (filter.contains(":isNull "))
                    f = isNullFilter(filter);
                else if (filter.contains(":isNotNull "))
                    f = isNotNullFilter(filter);

                if (f != null)
                    filtersByFields.add(f);
            }
            filters = filtersByFields.isEmpty() ? null : Filters.and(filtersByFields);
        }
        return filters;
    }

    private Bson eqFilter(String eqFilter, Map<String, Object> inParams) {
        Bson filter = null;
        String[] splittedFilter = eqFilter.replace(" ", "").split(":eq");
        Object pattern = inParams.get(splittedFilter[1].replace(":", ""));
        if (pattern != null) {
            if ("id".equals(splittedFilter[0]))
                filter = Filters.eq("_id", new ObjectId((String) pattern));
            else
                filter = Filters.eq(splittedFilter[0], pattern);
        }
        return filter;
    }

    private Bson likeFilter(String likeFilter, Map<String, Object> inParams) {
        Bson filter = null;
        String[] splittedFilter = likeFilter.replace(" ", "").split(":like");
        Object pattern = inParams.get(splittedFilter[1].replace(":", ""));
        if (pattern != null) {
            filter = Filters.regex(splittedFilter[0], "(?i)"+ Pattern.quote((String) pattern));
        }
        return filter;
    }

    private Bson inFilter(String inFilter, Map<String, Object> inParams) {
        Bson filter = null;
        String[] splittedFilter = inFilter.replace(" ", "").split(":in");
        Object paramsValue = inParams.get(splittedFilter[1].replace(":", ""));
        List patterns = paramsValue instanceof List ? (List) paramsValue : Arrays.asList(paramsValue);
        if (!patterns.isEmpty()) {
            if ("id".equals(splittedFilter[0]))
                filter = Filters.in("_id", patterns.stream().map(id -> new ObjectId((String) id)).toArray());
            else
                filter = Filters.in(splittedFilter[0], patterns);
        }
        return filter;
    }

    private Bson moreFilter(String moreFilter, Map<String, Object> inParams) {
        Bson filter = null;
        String[] splittedFilter = moreFilter.replace(" ", "").split(":more");
        Object pattern = inParams.get(splittedFilter[1].replace(":", ""));
        if (pattern != null) {
            if ("id".equals(splittedFilter[0]))
                filter = Filters.gt("_id", new ObjectId((String) pattern));
            else
                filter = Filters.gt(splittedFilter[0], pattern);
        }
        return filter;
    }

    private Bson lessFilter(String lessFilter, Map<String, Object> inParams) {
        Bson filter = null;
        String[] splittedFilter = lessFilter.replace(" ", "").split(":less");
        Object pattern = inParams.get(splittedFilter[1].replace(":", ""));
        if (pattern != null) {
            if ("id".equals(splittedFilter[0]))
                filter = Filters.lt("_id", new ObjectId((String) pattern));
            else
                filter = Filters.lt(splittedFilter[0], pattern);
        }
        return filter;
    }

    private Bson isNullFilter(String isNullFilter) {
        String[] splittedFilter = isNullFilter.replace(" ", "").split(":isNull");
        return Filters.eq(splittedFilter[0], null);
    }

    private Bson isNotNullFilter(String isNotNullFilter) {
        String[] splittedFilter = isNotNullFilter.replace(" ", "").split(":isNotNull");
        return Filters.ne(splittedFilter[0], null);
    }

    private Bson eqOrIsNullFilter(String eqOrIsNullFilter, Map<String, Object> inParams) {
        Bson filter = null;
        String[] splittedFilter = eqOrIsNullFilter.replace(" ", "").split(":eqOrIsNull");
        Object pattern = inParams.get(splittedFilter[1].replace(":", ""));
        if (pattern != null)
            filter = Filters.or(Filters.eq(splittedFilter[0], pattern), Filters.eq(splittedFilter[0], null));
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

