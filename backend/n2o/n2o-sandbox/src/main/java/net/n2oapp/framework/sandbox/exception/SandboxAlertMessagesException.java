package net.n2oapp.framework.sandbox.exception;

import lombok.Getter;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.List;

public class SandboxAlertMessagesException extends N2oUserException {

    @Getter
    private List<ResponseMessage> responseMessages;

    public SandboxAlertMessagesException(List<ResponseMessage> responseMessages) {
        super(null);
        this.responseMessages = responseMessages;
    }
}
