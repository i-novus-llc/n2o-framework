package net.n2oapp.framework.api.exception;

import lombok.Getter;

@Getter
public class ValidationMessage {
    private String message;
    private String messageTitle;
    private String fieldId;
    private String validationId;

    public ValidationMessage(String message, String fieldId, String validationId) {
        this.message = message;
        this.fieldId = fieldId;
        this.validationId = validationId;
    }

    public ValidationMessage(String message, String messageTitle, String fieldId, String validationId) {
        this.message = message;
        this.messageTitle = messageTitle;
        this.fieldId = fieldId;
        this.validationId = validationId;
    }
}
