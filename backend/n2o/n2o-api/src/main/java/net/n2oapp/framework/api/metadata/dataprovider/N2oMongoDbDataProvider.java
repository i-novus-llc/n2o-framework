package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

/**
 * MongoDb провайдер данных
 */
@Getter
@Setter
public class N2oMongoDbDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String connectionUrl;
    private String databaseName;
    private String collectionName;
    private Operation operation;

    public enum Operation {
        find,
        insertOne,
        updateOne,
        deleteOne,
        deleteMany,
        countDocuments
    }
}
