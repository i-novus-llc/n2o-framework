package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.data.validation.ValidationDialog;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.*;

/**
 * @author operehod
 * @since 02.04.2015
 */
public class Validator {

    private InvocationProcessor serviceProvider;
    private DataSet dataSet;
    private List<Validation> validationList;
    private N2oValidation.ServerMoment moment;

    private Boolean widgetWasValidated = false;
    private Set<String> validatedFields = new HashSet<>();
    private Set<String> usedValidations = new HashSet<>();
    private List<FailInfo> fails = new ArrayList<>();
    private boolean afterDanger = false;


    public List<FailInfo> validate() {
        if (validationList == null) return Collections.emptyList();
        ValidationUtil.sort(validationList);
        for (Validation v : validationList) {
            handleValidation(v, fails);
        }
        return fails;
    }

    private void handleValidation(Validation v, List<FailInfo> fails) {
        if (checkValidation(v)) {
            v.validate(dataSet, serviceProvider, message -> {
                FailInfo failInfo = new FailInfo();
                failInfo.setMoment(v.getMoment());
                failInfo.setValidationId(v.getId());
                failInfo.setValidationClass(v.getClass().getSimpleName());
                failInfo.setSeverity(v.getSeverity());
                failInfo.setFieldId(v.isForField() ? v.getFieldId() : null);
                failInfo.setMessage(message);
                if (v instanceof ValidationDialog)
                    failInfo.setDialog(((ValidationDialog) v).getDialog());
                fails.add(failInfo);
                afterFail(v);
            });
        }
    }

    private void afterFail(Validation v) {
        if (SeverityType.danger.equals(v.getSeverity()))
            afterDanger = true;
        if (!v.isForField())
            widgetWasValidated = true;
        else
            validatedFields.add(v.getFieldId());

        usedValidations.add(v.getId());
    }

    private boolean checkValidation(Validation validation) {
        return checkAfterDanger(validation)
                && checkEnabled(validation)
                && checkMoment(validation)
                && checkUnique(validation)
                && checkUsed(validation)
                && checkRequiredConstraint(validation);
    }

    private boolean checkAfterDanger(Validation validation) {
        if (SeverityType.danger.equals(validation.getSeverity()))
            return true;

        return !afterDanger;
    }

    private boolean checkRequiredConstraint(Validation validation) {
        if (validation instanceof ConstraintValidation) {
            ConstraintValidation v = (ConstraintValidation) validation;
            if (v.getInParametersList() != null) {
                for (AbstractParameter inParam : v.getInParametersList()) {
                    if (inParam.getRequired() != null
                            && inParam.getRequired()
                            && v.getRequiredFields().contains(inParam.getName())
                            && !dataSet.containsKey(inParam.getName())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkUsed(Validation v) {
        return !usedValidations.contains(v.getId());
    }

    private boolean checkUnique(Validation v) {
        return v.isForField() ? !validatedFields.contains(v.getFieldId()) : !widgetWasValidated;
    }

    private boolean checkEnabled(Validation v) {
        if (v.getEnabled() != null)
            return v.getEnabled();
        if (v.getEnablingConditions() != null) {
            for (String enablingCondition : v.getEnablingConditions()) {
                if (!ScriptProcessor.evalForBoolean(enablingCondition, dataSet))
                    return false;
            }
        }
        return true;
    }

    private boolean checkMoment(Validation validation) {
        return validation.getMoment() == null || moment == null || moment == validation.getMoment();
    }

    public static Builder newBuilder() {
        return new Validator().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder addMoment(N2oValidation.ServerMoment moment) {
            Validator.this.moment = moment;
            return this;
        }

        public Builder addValidations(List<Validation> validations) {
            if (validations == null) return this;
            if (validationList == null)
                validationList = new ArrayList<>();

            Validator.this.validationList.addAll(validations);
            return this;
        }

        public Builder addDataSet(DataSet dataSet) {
            Validator.this.dataSet = dataSet;
            return this;
        }

        public Builder addInvocationProcessor(InvocationProcessor processor) {
            Validator.this.serviceProvider = processor;
            return this;
        }

        public Validator build() {
            return Validator.this;
        }
    }
}
