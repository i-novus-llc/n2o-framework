package net.n2oapp.framework.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Бизнес исключение
 */
@Getter
@Setter
public class N2oUserException extends N2oException {
    private List<ValidationMessage> messages;
    private String validationFailKey;


    public N2oUserException(String userMessage) {
        super();
        setUserMessage(userMessage);
        setHttpStatus(400);
    }

    public N2oUserException(String userMessage, String alertKey, List<ValidationMessage> messages) {
        super();
        setUserMessage(userMessage);
        setHttpStatus(400);
        this.messages = messages;
    }

    public N2oUserException(String userMessage, String techMessage) {
        super(techMessage);
        setUserMessage(userMessage);
        setHttpStatus(400);
    }

}