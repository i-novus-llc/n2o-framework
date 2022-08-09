package net.n2oapp.framework.api.ui;

import java.util.Collections;
import java.util.List;

public class N2oAlertMessagesConstructor implements AlertMessagesConstructor {

    private final AlertMessageBuilder alertMessageBuilder;

    public N2oAlertMessagesConstructor(AlertMessageBuilder alertMessageBuilder){
        this.alertMessageBuilder = alertMessageBuilder;
    }

    @Override
    public List<ResponseMessage> constructMessages(Exception exception) {
        return Collections.singletonList(alertMessageBuilder.build(exception));
    }

    @Override
    public List<ResponseMessage> constructMessages(Exception exception, RequestInfo requestInfo) {
        return alertMessageBuilder.buildMessages(exception, requestInfo);
    }
}
