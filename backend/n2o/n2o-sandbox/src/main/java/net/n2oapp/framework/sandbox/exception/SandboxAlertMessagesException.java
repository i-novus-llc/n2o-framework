package net.n2oapp.framework.sandbox.exception;

import lombok.Getter;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.sandbox.cases.nesting_object_fields.MessageInfo;

import java.util.List;

public class SandboxAlertMessagesException extends N2oException {

    @Getter
    private List<MessageInfo> messageInfos;

    public SandboxAlertMessagesException(List<MessageInfo> messageInfos) {
        this.messageInfos = messageInfos;
    }
}
