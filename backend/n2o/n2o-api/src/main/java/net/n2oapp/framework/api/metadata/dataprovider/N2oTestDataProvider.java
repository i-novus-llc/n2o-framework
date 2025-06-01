package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

/**
 * Структура тестового провайдера данных
 */
@Getter
@Setter
public class N2oTestDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String file;
    private OperationEnum operation;
    private String primaryKey;
    private String primaryKeys;
    private PrimaryKeyTypeEnum primaryKeyType;

    @RequiredArgsConstructor
    @Getter
    public enum OperationEnum implements N2oEnum {
        FIND_ALL("findAll"),
        FIND_ONE("findOne"),
        CREATE("create"),
        UPDATE("update"),
        UPDATE_MANY("updateMany"),
        UPDATE_FIELD("updateField"),
        DELETE("delete"),
        DELETE_MANY("deleteMany"),
        COUNT("count"),
        ECHO("echo");

        private final String id;
    }

    @RequiredArgsConstructor
    @Getter
    public enum PrimaryKeyTypeEnum implements N2oEnum {
        INTEGER("integer"),
        STRING("string");

        private final String id;
    }
}
