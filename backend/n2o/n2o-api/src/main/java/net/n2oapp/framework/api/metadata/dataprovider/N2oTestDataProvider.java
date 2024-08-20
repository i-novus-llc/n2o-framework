package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

import static net.n2oapp.framework.api.metadata.dataprovider.N2oTestDataProvider.PrimaryKeyType.integer;

/**
 * Структура тестового провайдера данных
 */
@Getter
@Setter
public class N2oTestDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String file;
    private Operation operation;
    private String primaryKey = "id";
    private String primaryKeys;
    private PrimaryKeyType primaryKeyType = integer;

    public N2oTestDataProvider() {
        primaryKeys = primaryKey + "s";
    }

    public enum Operation {
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

    public enum PrimaryKeyType {
        integer,
        string
    }
}
