package net.n2oapp.framework.sandbox;

import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.ui.RequestInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.sandbox.cases.nesting_object_fields.MessageInfo;
import net.n2oapp.framework.sandbox.exception.SandboxAlertMessagesException;

import java.util.ArrayList;
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
        if (exception instanceof SandboxAlertMessagesException) {
            List<MessageInfo> messageInfos = ((SandboxAlertMessagesException) exception).getMessageInfos();
            List<ResponseMessage> responseMessages = new ArrayList<>();
            for (MessageInfo message : messageInfos) {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setTitle(message.getTitle());
                responseMessage.setText(message.getText());
                responseMessage.setSeverityType(SeverityType.valueOf(message.getColor()));
                responseMessage.setPlacement(MessagePlacement.valueOf(message.getPlacement()));
                responseMessages.add(responseMessage);
            }
            return responseMessages;
        }
        return alertMessageBuilder.buildMessages(exception, requestInfo);
    }
}
