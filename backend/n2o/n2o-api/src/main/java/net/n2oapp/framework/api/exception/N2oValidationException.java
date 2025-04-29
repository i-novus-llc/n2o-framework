package net.n2oapp.framework.api.exception;

import java.util.List;

public class N2oValidationException extends N2oUserException {

    public N2oValidationException(String userMessage) {
        super(userMessage);
    }

    public N2oValidationException(String userMessage, String fieldId) {
        super(userMessage);
        setField(fieldId);
    }

    public N2oValidationException(String userMessage, List<ValidationMessage> messages) {
        super(userMessage, messages);
    }
}
