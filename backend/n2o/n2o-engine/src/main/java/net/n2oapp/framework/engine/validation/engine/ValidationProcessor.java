package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.exception.N2oValidationException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.exception.ValidationMessage;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.engine.validation.engine.info.ObjectValidationInfo;
import net.n2oapp.framework.engine.validation.engine.info.QueryValidationInfo;
import net.n2oapp.framework.engine.validation.engine.info.ValidationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationProcessor {

    private InvocationProcessor invocationProcessor;

    public ValidationProcessor(InvocationProcessor invocationProcessor) {
        this.invocationProcessor = invocationProcessor;
    }

    public List<FailInfo> validate(ObjectValidationInfo info, N2oValidation.ServerMoment moment) {
        Validator validator = buildValidator(info, moment);
        return collectFails(validator, info);
    }

    public List<FailInfo> validate(QueryValidationInfo info, N2oValidation.ServerMoment moment) {
        Validator validator = buildValidator(info, moment);
        return collectFails(validator, info);
    }

    private List<FailInfo> collectFails(Validator validator, ValidationInfo info) {
        List<FailInfo> fails = validator.validate();
        List<FailInfo> failsWithDanger = getFailsWithDanger(fails);
        if (!failsWithDanger.isEmpty()) {
            throwDangerException(failsWithDanger, info.getMessageForm(), info.getMessageForm());
        }
        return fails;
    }

    private Validator buildValidator(QueryValidationInfo info, N2oValidation.ServerMoment moment) {
        return Validator.newBuilder()
                .addDataSet(info.getDataSet())
                .addInvocationProcessor(invocationProcessor)
                .addValidations(info.getValidations())
                .addValidations(info.getObject() != null ? info.getObject().getValidations() : null)
                .addMoment(moment)
                .build();
    }

    private Validator buildValidator(ObjectValidationInfo info, N2oValidation.ServerMoment moment) {
        return Validator.newBuilder()
                .addDataSet(info.getDataSet())
                .addInvocationProcessor(invocationProcessor)
                .addValidations(info.getValidations())
                .addMoment(moment)
                .build();
    }

    private List<FailInfo> getFailsWithDanger(List<FailInfo> fails) {
        return fails
                .stream()
                .filter(fail -> SeverityType.danger.equals(fail.getSeverity()))
                .collect(Collectors.toList());
    }

    private void throwDangerException(List<FailInfo> fails, String failWidgetId, String messageForm) {
        List<ValidationMessage> messages = new ArrayList<>();
        String userMessage = null;
        N2oDialog dialog = null;
        for (FailInfo fail : fails) {
            messages.add(new ValidationMessage(fail.getMessage(), fail.getFieldId(), fail.getValidationId()));
            if (fail.getFieldId() == null)
                userMessage = fail.getMessage();
            if (fail.getDialog() != null)
                dialog = fail.getDialog();
        }

        N2oValidationException exc = new N2oValidationException(userMessage, failWidgetId, messages, messageForm);
        exc.setDialog(dialog);
        throw exc;
    }
}
