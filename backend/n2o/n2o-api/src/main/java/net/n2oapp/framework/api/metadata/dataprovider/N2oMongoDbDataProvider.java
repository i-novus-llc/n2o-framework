package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
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
    private OperationEnum operation;

    @RequiredArgsConstructor
    @Getter
    public enum OperationEnum implements N2oEnum {
        FIND("find"),
        INSERT_ONE("insertOne"),
        UPDATE_ONE("updateOne"),
        DELETE_ONE("deleteOne"),
        DELETE_MANY("deleteMany"),
        COUNT_DOCUMENTS("countDocuments");

        private final String id;
    }
}
