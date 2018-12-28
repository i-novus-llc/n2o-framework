package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author operehod
 * @since 03.04.2015
 */
public class ValidationUtil {

    private static final String YES_CHOICE = "confirmation.yes";


    public static boolean isValidatable(CompiledQuery query, Validation validation) {
        Set<String> validationFields = validation.getRequiredFields();
        Set<String> queryFilters = query.getFilterFieldsMap().values().stream().map(N2oQuery.Filter::getFilterField).collect(Collectors.toSet());
        return checkInSubSet(validationFields, queryFilters);
    }

    private static boolean checkInSubSet(Set<String> validationFields, Set<String> actionFields) {
        if (validationFields == null) return false;
        for (String field : validationFields) {
            if (!actionFields.contains(field)) return false;
        }
        return true;
    }


    public static void checkForConfirmation(CompiledObject.Operation operation, DataSet dataSet, String choice) {
        checkForConfirmation(Collections::emptyList, operation, dataSet, choice);
    }

    public static void checkForConfirmation(Supplier<List<String>> warnings, CompiledObject.Operation operation, DataSet dataSet, String choice) {

        //По сути тут должна быть проверка типа choice.equals(YES_CHOICE), но давайте подумаем...
        //1. null сюда придти не может. Потому что если пользователь выбрал null, но клиент ничего не пошлет.
        //2. Если сюда пришел другой чойс, то он явно из какой-то "левой" логики. Не будем прерывать чужой диалог
        if (choice != null)
            return;
        List<String> warningMessages = warnings.get();
        if (!warningMessages.isEmpty())
            failWithWarnings(warningMessages, operation, dataSet);
    }

    private static void failWithWarnings(List<String> warningsMessages, CompiledObject.Operation operation, DataSet dataSet) {
        String resultWarningMessage = "";
        for (String message : warningsMessages) {
            resultWarningMessage += String.format("<div class=\"alert alert-warning\">%s</div>", message);
        }
        String userSummaryMessage = StringUtils.resolveLinks(resultWarningMessage
                + String.format("<div class=\"n2o-dialog-choice__text\">%s</div>", (dataSet.get("$bulk.count") == null ? operation.getConfirmationText() : operation.getBulkConfirmationText())), dataSet);

        N2oUserException exception = new N2oUserException(userSummaryMessage);
        Map<String, String> choiceMap = new HashMap<>();
        choiceMap.put("net.n2oapp.framework.ui.dialogs.button.yes", YES_CHOICE);
        choiceMap.put("net.n2oapp.framework.ui.dialogs.button.no", null);
        exception.setChoice(choiceMap);
        throw exception;
    }

    public static List<Validation> calculateVisibleValidation(List<Validation> validations, DataSet dataSet) {
        if (validations == null)
            return Collections.emptyList();
        List<Validation> res = new ArrayList<>();
        for (Validation n2oField : validations) {
            if (isVisible(n2oField, dataSet))
                res.add(n2oField);
        }
        return res;
    }

    private static boolean isVisible(Validation validation, DataSet dataSet) {
        boolean fieldIsVisible = true;
        if (validation.getFieldVisibilityCondition() != null) {
            fieldIsVisible = ScriptProcessor.evalForBoolean(validation.getFieldVisibilityCondition().getCondition(), dataSet);
        }
        boolean fieldSetIsVisible = true;
        if (validation.getFieldSetVisibilityCondition() != null) {
            fieldSetIsVisible = ScriptProcessor.evalForBoolean(validation.getFieldSetVisibilityCondition(), dataSet);
        }
        return fieldIsVisible && fieldSetIsVisible;
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
    public static void sort(List<Validation> validations) {
        if (validations == null) return;
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
        Collections.sort(validations, comparator);
    }
}
