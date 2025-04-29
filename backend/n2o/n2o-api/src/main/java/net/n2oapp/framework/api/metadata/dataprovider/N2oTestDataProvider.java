package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
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

    public enum OperationEnum {
        findAll,
        findOne,
        create,
        update,
        updateMany,
        updateField,
        delete,
        deleteMany,
        count,
        echo
    }

    public enum PrimaryKeyTypeEnum {
        integer,
        string
    }
}
