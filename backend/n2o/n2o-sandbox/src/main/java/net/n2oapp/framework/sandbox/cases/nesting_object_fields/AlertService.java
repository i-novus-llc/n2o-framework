package net.n2oapp.framework.sandbox.cases.nesting_object_fields;

import net.n2oapp.framework.sandbox.exception.SandboxAlertMessagesException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertService {

    public void createAlerts(List<MessageInfo> messages) {
        throw new SandboxAlertMessagesException(messages);
    }
}
