package net.n2oapp.framework.sandbox.autotest;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.processor.N2oActionException;
import net.n2oapp.framework.sandbox.exception.SandboxAlertMessagesException;

public class SandboxAutotestOperationExceptionHandler implements OperationExceptionHandler {

    @Override
    public N2oException handle(CompiledObject.Operation o, DataSet data, Exception e) {
        if (e instanceof N2oUserException || e instanceof N2oSpelException)
            return (N2oException) e;
        if (e instanceof SandboxAlertMessagesException) {
            return (SandboxAlertMessagesException) e;
        }
        N2oException n2oE = new N2oActionException(e);
        if (o.getFailText() != null) {
            //вывод fail-text вместо внутренней ошибки
            n2oE.setUserMessage(StringUtils.resolveLinks(o.getFailText(), data));
        }
        return n2oE;
    }
}
