package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.validation.*;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.*;

/**
 * Валидатор данных
 */
public class Validator implements Iterable<Validation> {

    private InvocationProcessor serviceProvider;
    private DomainProcessor domainProcessor;
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
        for (Validation v : validationList) {
            handleValidation(v, fails);
        }
        return fails;
    }

    private void handleValidation(Validation v, List<FailInfo> fails) {
        if (checkValidation(v)) {
            if (isMultiSet(v.getFieldId())) {
                String commonFieldId = v.getFieldId();
                String commonMessage = StringUtils.resolveLinks(v.getMessage(), dataSet);
                String commonMessageTitle = StringUtils.resolveLinks(v.getMessageTitle(), dataSet);
                int count = ((DataList) dataSet.get(getMultiSetId(v.getFieldId()))).size();
                for (int i = 0; i< count; i++) {
                    v.setFieldId(replaceIndex(commonFieldId, i));
                    v.setMessage(replaceIndex(commonMessage, i));
                    v.setMessageTitle(replaceIndex(commonMessageTitle, i));
                    dataSet.put("index", i);
                    validateField(v, fails);
                }
                dataSet.remove("index");
                v.setMessage(commonMessage);
                v.setMessageTitle(commonMessageTitle);
                v.setFieldId(commonFieldId);
            } else {
                validateField(v, fails);
            }
        }
    }

    private String replaceIndex(String commonFieldId, int i) {
        return commonFieldId.replaceAll("\\[index]", "[" + i + "]");
    }

    private void validateField(Validation v, List<FailInfo> fails) {
        v.validate(dataSet, serviceProvider, message -> {
            FailInfo failInfo = new FailInfo();
            failInfo.setMoment(v.getMoment());
            failInfo.setValidationId(v.getId());
            failInfo.setValidationClass(v.getClass().getSimpleName());
            failInfo.setSeverity(v.getSeverity());
            failInfo.setFieldId(v.isForField() ? v.getFieldId() : null);
            failInfo.setMessage(message);
            failInfo.setMessageTitle(StringUtils.resolveLinks(v.getMessageTitle(), dataSet));
            if (v instanceof ValidationDialog)
                failInfo.setDialog(((ValidationDialog) v).getDialog());
            fails.add(failInfo);
            afterFail(v);
        }, domainProcessor);
    }

    private String getMultiSetId(String fieldId) {
        String[] fields = fieldId.split("\\[index\\].");
        return fields[0];
    }

    private boolean isMultiSet(String fieldId) {
        return fieldId != null && fieldId.contains("[index]");
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
                && checkRequiredConstraint(validation)
                && checkSide(validation);
    }

    private boolean checkSide(Validation validation) {
        return validation.getSide() == null || validation.getSide().contains("server");
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
                            && v.getRequiredFields().contains(inParam.getId())
                            && !dataSet.containsKey(inParam.getId())) {
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

    @Override
    public Iterator<Validation> iterator() {
        return validationList.iterator();
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

        public Builder addDomainProcessor(DomainProcessor processor) {
            Validator.this.domainProcessor = processor;
            return this;
        }

        public Validator build() {
            sort();
            return Validator.this;
        }

        /**
         * Сортирует валидации по SeverityType.
         * С одинаковым SeverityType располагает сначала валидации полей, потом валидации виджета.
         * С одинаковым признаком "для поля/ для виджета" располагает по типу валидации:
         * Mandatory -> Condition -> Constraint
         * <p>
         * Порядок сортировки:
         * Danger - Field - Mandatory
         * Danger - Field - Condition
         * Danger - Field - Constraint
         * Danger - Widget - Mandatory
         * ...
         * Success - Widget - Constraint
         */
        private void sort() {
            if (validationList == null) return;
            Comparator<Validation> comparator = Comparator
                    .comparing(Validation::getSeverity)
                    .reversed()
                    .thenComparing(Validation::isForField)
                    .reversed()
                    .thenComparing((v1, v2) -> {
                        if (v1.getClass() == v2.getClass())
                            return 0;
                        else if (v1.getClass() == MandatoryValidation.class)
                            return -1;
                        else if (v1.getClass() == ConditionValidation.class) {
                            if (v2.getClass() == ConstraintValidation.class)
                                return -1;
                            else
                                return 1;
                        } else
                            return 1;
                    });
            Collections.sort(validationList, comparator);
        }
    }
}
