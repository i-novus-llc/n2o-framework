package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

/**
 * Структура тестового провайдера данных
 */
@Getter
@Setter
public class N2oTestDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String file;
    private Operation operation;

    public enum Operation {
        findAll,
        findOne,
        create,
        update,
        delete,
        count,
        echo
    }
}
