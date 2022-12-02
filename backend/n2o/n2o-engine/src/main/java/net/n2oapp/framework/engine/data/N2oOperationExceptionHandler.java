package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.exception.N2oActionException;

/**
 * Обработка исключений операции по умолчанию
 */
public class N2oOperationExceptionHandler implements OperationExceptionHandler {
    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        if (e instanceof N2oUserException || e instanceof N2oSpelException)
            return (N2oException) e;
        N2oException n2oE = new N2oActionException(e);
        if (o.getFailText() != null) {
            //вывод fail-text вместо внутренней ошибки
            n2oE.setUserMessage(StringUtils.resolveLinks(o.getFailText(), data));
        }
        if (o.getFailTitle() != null)
            n2oE.setUserMessageTitle(StringUtils.resolveLinks(o.getFailTitle(), data));
        return n2oE;
    }
}
