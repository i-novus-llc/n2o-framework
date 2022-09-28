package net.n2oapp.framework.sandbox.cases.alerts;

import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.sandbox.exception.SandboxAlertMessagesException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    public void createAlerts(List<ResponseMessage> messages) {
        throw new SandboxAlertMessagesException(messages);
    }
}
