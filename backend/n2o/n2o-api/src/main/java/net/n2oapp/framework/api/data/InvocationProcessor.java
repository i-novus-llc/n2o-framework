package net.n2oapp.framework.api.data;


import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;

import java.util.Collection;

/**
 * Процессор выполнения действий
 */
public interface InvocationProcessor {

    /**
     * Выполнение действия
     *
     * @param invocation    исходная модель действия
     * @param inDataSet     данные переданные с клиента
     * @param inParameters  маппинг данных с клиента
     * @param outParameters маппинг данных на клиент
     * @return данные для клиента
     */
    DataSet invoke(
            N2oInvocation invocation,
            DataSet inDataSet,
            Collection<AbstractParameter> inParameters,
            Collection<ObjectSimpleField> outParameters);
}
