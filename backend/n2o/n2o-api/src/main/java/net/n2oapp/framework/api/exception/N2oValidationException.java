package net.n2oapp.framework.api.exception;

import lombok.Getter;

import java.util.List;

public class N2oValidationException extends N2oUserException {

    @Getter
    private String messageForm;

    public N2oValidationException(String userMessage) {
        super(userMessage);
    }

    public N2oValidationException(String userMessage, String fieldId) {
        super(userMessage);
        setField(fieldId);
    }

    public N2oValidationException(String userMessage,
                                  String failAlertWidgetId,
                                  List<ValidationMessage> messages,
                                  String messageForm) {
        super(userMessage, failAlertWidgetId, messages);
        this.messageForm = messageForm;
    }
}
