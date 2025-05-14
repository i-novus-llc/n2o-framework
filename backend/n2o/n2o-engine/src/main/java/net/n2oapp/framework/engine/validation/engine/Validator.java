package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * Валидатор данных
 */
public class Validator implements Iterable<Validation> {

    private InvocationProcessor serviceProvider;
    private DomainProcessor domainProcessor;
    private DataSet dataSet;
    private List<Validation> validationList;
    private N2oValidation.ServerMomentEnum moment;

    private Boolean widgetWasValidated = false;
    private Set<String> validatedFields = new HashSet<>();
    private Set<String> usedValidations = new HashSet<>();
    private List<FailInfo> fails = new ArrayList<>();
    private boolean afterDanger = false;

    private static final String MULTISET_INDEX_KEY = "index";
    private static final String MULTISET_INDEX_PREFIX = "$index_";
    private static final String MULTISET_INDEX_PREFIX_ESC = "\\$index_";


    public List<FailInfo> validate() {
        if (validationList == null)
            return Collections.emptyList();
        for (Validation v : validationList)
            handleValidation(v, fails);

        return fails;
    }

    private void handleValidation(Validation v, List<FailInfo> fails) {
        if (checkValidation(v)) {
            if (isMultiSet(v.getFieldId())) {
                int maxLevel = getMaxLevel(v.getFieldId());
                if (v.getFieldId().contains("[" + MULTISET_INDEX_KEY + "]")) {
                    String commonFieldId = v.getFieldId();
                    String commonMessage = StringUtils.resolveLinks(v.getMessage(), dataSet);
                    String commonMessageTitle = StringUtils.resolveLinks(v.getMessageTitle(), dataSet);
                    String multiSetId = getMultiSetId(v.getFieldId());
                    int count = dataSet.containsKey(multiSetId) ? ((DataList) dataSet.get(multiSetId)).size() : 0;
                    for (int i = 0; i < count; i++) {
                        try {
                            Validation copiedValidation = v.getClass().getConstructor(v.getClass()).newInstance(v);
                            copiedValidation.setFieldId(replaceIndex(commonFieldId, MULTISET_INDEX_KEY, i));
                            copiedValidation.setMessage(replaceIndex(commonMessage, MULTISET_INDEX_KEY, i));
                            copiedValidation.setMessageTitle(replaceIndex(commonMessageTitle, MULTISET_INDEX_KEY, i));
                            dataSet.put(MULTISET_INDEX_KEY, i);
                            if (maxLevel != 0) {
                                validateByIndex(copiedValidation, 1, maxLevel, (DataSet) ((DataList) dataSet.get(multiSetId)).get(i));
                            } else {
                                validateField(copiedValidation, fails);
                            }
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            throw new N2oException("Failed copy validation", e);
                        }
                    }
                    dataSet.remove(MULTISET_INDEX_KEY);
                } else {
                    validateByIndex(v, 1, maxLevel, dataSet);
                }
            } else {
                validateField(v, fails);
            }
        }
    }

    /**
     * Получить максимально возможный индекс для мультисетов с префиксом
     *
     * @param fieldId идетификатор поля
     */
    private int getMaxLevel(String fieldId) {
        if (fieldId.contains(MULTISET_INDEX_PREFIX)) {
            String[] splitByPrefix = fieldId.split(MULTISET_INDEX_PREFIX_ESC);
            return Integer.parseInt(splitByPrefix[splitByPrefix.length - 1].substring(0, splitByPrefix[splitByPrefix.length - 1].lastIndexOf("]")));
        }
        return 0;
    }

    private void validateByIndex(Validation validation, int level, int maxLevel, DataSet currentDs) {
        String placeholder = MULTISET_INDEX_PREFIX + level;
        if (validation.getFieldId().contains(placeholder)) {
            String placeholderEsc = MULTISET_INDEX_PREFIX_ESC + level;
            String multiSetId = getInnerMultiSetId(validation.getFieldId(), level);
            int count = currentDs.containsKey(multiSetId) ? ((DataList) currentDs.get(multiSetId)).size() : 0;
            for (int i = 0; i < count; i++) {
                try {
                    Validation copiedValidation = validation.getClass().getConstructor(validation.getClass()).newInstance(validation);
                    copiedValidation.setFieldId(replaceIndex(validation.getFieldId(), placeholderEsc, i));
                    copiedValidation.setMessage(replaceIndex(validation.getMessage(), placeholderEsc, i));
                    copiedValidation.setMessageTitle(replaceIndex(validation.getMessageTitle(), placeholderEsc, i));
                    dataSet.put(placeholder, i);
                    if (level == maxLevel) {
                        validateField(copiedValidation, fails);
                        dataSet.remove(placeholder);
                        return;
                    }
                    validateByIndex(copiedValidation, level + 1, maxLevel, (DataSet) ((DataList) currentDs.get(multiSetId)).get(i));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new N2oException("Failed copy validation", e);
                }
            }
        }
        if (level != maxLevel)
            validateByIndex(validation, level + 1, maxLevel, currentDs);
    }

    private String replaceIndex(String text, String placeholder, int i) {
        return text == null
                ? null
                : text.replaceAll("\\[" + placeholder + "]", "[" + i + "]");
    }

    private void validateField(Validation v, List<FailInfo> fails) {
        v.validate(dataSet, serviceProvider, message -> {
            FailInfo failInfo = new FailInfo();
            failInfo.setMoment(v.getMoment());
            failInfo.setValidationId(v.getId());
            failInfo.setValidationClass(v.getClass().getSimpleName());
            failInfo.setSeverity(v.getSeverity());
            failInfo.setFieldId(v.isForField() ? v.getFieldId() : null);
            failInfo.setMessage(StringUtils.resolveLinks(v.getMessage(), dataSet));
            failInfo.setMessageTitle(StringUtils.resolveLinks(v.getMessageTitle(), dataSet));
            fails.add(failInfo);
            afterFail(v);
        }, domainProcessor);
    }

    private String getMultiSetId(String fieldId) {
        return fieldId.split("\\[" + MULTISET_INDEX_KEY + "\\].")[0];
    }

    private String getInnerMultiSetId(String fieldId, int index) {
        String first = fieldId.split("\\[" + MULTISET_INDEX_PREFIX_ESC + index + "\\].")[0];
        return first.substring(first.lastIndexOf('.') + 1);
    }

    private boolean isMultiSet(String fieldId) {
        return fieldId != null && (fieldId.contains("[" + MULTISET_INDEX_KEY + "]") || fieldId.contains("[" + MULTISET_INDEX_PREFIX));
    }

    private void afterFail(Validation v) {
        if (SeverityTypeEnum.danger.equals(v.getSeverity()))
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
        if (SeverityTypeEnum.danger.equals(validation.getSeverity()))
            return true;

        return !afterDanger;
    }

    private boolean checkRequiredConstraint(Validation validation) {
        if (validation instanceof ConstraintValidation v && v.getInParametersList() != null) {
            for (AbstractParameter inParam : v.getInParametersList()) {
                if (inParam.getRequired() != null
                        && inParam.getRequired()
                        && v.getRequiredFields().contains(inParam.getId())
                        && !dataSet.containsKey(inParam.getId())) {
                    return false;
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

        public Builder addMoment(N2oValidation.ServerMomentEnum moment) {
            Validator.this.moment = moment;

            return this;
        }

        public Builder addValidations(List<Validation> validations) {
            if (validations == null)
                return this;
            if (validationList == null)
                validationList = new ArrayList<>();
            validationList.addAll(validations);

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
         * Сортирует валидации по SeverityTypeEnum.
         * С одинаковым SeverityTypeEnum располагает сначала валидации полей, потом валидации виджета.
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
            if (validationList == null)
                return;
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
            validationList.sort(comparator);
        }
    }
}
