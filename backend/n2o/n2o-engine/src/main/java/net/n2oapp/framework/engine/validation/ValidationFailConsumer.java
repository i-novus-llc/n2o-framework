package net.n2oapp.framework.engine.validation;

import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.ui.ResponseInfo;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.engine.validation.engine.FailCallback;
import net.n2oapp.framework.engine.validation.engine.FailInfo;

/**
 * @author operehod
 * @since 15.06.2015
 */
public class ValidationFailConsumer implements FailCallback {
    private ResponseInfo responseInfo;

    public ValidationFailConsumer(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    @Override
    public void accept(FailInfo info) {
        responseInfo.addMessage(createResponseMessage(info.getMessage(), info.getSeverity(), info.getFieldId()));
    }

    public static ResponseMessage createResponseMessage(String message, SeverityType severity, String fieldId) {
        ResponseMessage res = new ResponseMessage();
        res.setText(message);
        res.setField(fieldId);
        res.setSeverityType(severity);
        return res;
    }
}
