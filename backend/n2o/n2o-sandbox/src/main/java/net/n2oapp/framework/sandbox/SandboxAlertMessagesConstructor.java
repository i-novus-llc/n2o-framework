package net.n2oapp.framework.sandbox;

import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.ui.RequestInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.sandbox.exception.SandboxAlertMessagesException;

import java.util.Collections;
import java.util.List;

public class SandboxAlertMessagesConstructor implements AlertMessagesConstructor {

    private final AlertMessageBuilder alertMessageBuilder;

    public SandboxAlertMessagesConstructor(AlertMessageBuilder alertMessageBuilder){
        this.alertMessageBuilder = alertMessageBuilder;
    }

    @Override
    public List<ResponseMessage> constructMessages(Exception exception) {
        return Collections.singletonList(alertMessageBuilder.build(exception));
    }

    @Override
    public List<ResponseMessage> constructMessages(Exception exception, RequestInfo requestInfo) {
        if (exception instanceof SandboxAlertMessagesException)
            return ((SandboxAlertMessagesException) exception).getResponseMessages();
        return alertMessageBuilder.buildMessages(exception, requestInfo);
    }
}
