package net.n2oapp.framework.sandbox.exception;

import lombok.Getter;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.ui.ResponseMessage;

import java.util.List;

public class SandboxAlertMessagesException extends N2oException {

    @Getter
    private List<ResponseMessage> responseMessages;

    public SandboxAlertMessagesException(List<ResponseMessage> responseMessages) {
        this.responseMessages = responseMessages;
    }
}
