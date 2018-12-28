package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Список валидаций для определенной модели виджета (обертка над ValidationList)
 */
public class ValidationScope {
    private String widgetId;
    private ReduxModel model;
    private ValidationList validations;

    public ValidationScope(String widgetId, ReduxModel model, ValidationList validations) {
        this.widgetId = widgetId;
        this.model = model;
        this.validations = validations;
    }

    public void add(Validation validation) {
        putIfAbsent();
        validations.getValidations().get(model).get(widgetId).add(validation);
    }

    public void addAll(List<Validation> validationList) {
        putIfAbsent();
        validations.getValidations().get(model).get(widgetId).addAll(validationList);
    }

    private void putIfAbsent() {
        validations.getValidations().putIfAbsent(model, new HashMap<>());
        validations.getValidations().get(model).putIfAbsent(widgetId, new ArrayList<>());
    }

    public String getWidgetId() {
        return widgetId;
    }

    public ReduxModel getModel() {
        return model;
    }
}

