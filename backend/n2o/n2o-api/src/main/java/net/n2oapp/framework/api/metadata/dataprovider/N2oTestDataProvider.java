package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

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
    private PrimaryKeyType primaryKeyType = integer;

    public enum Operation {
        findAll,
        findOne,
        create,
        update,
        delete,
        count,
        echo
    }

    public enum PrimaryKeyType {
        integer,
        string
    }
}
