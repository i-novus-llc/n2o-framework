package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;

import java.util.List;

/**
 * Список валидаций для определенной модели виджета (обертка над ValidationList)
 */
public class ValidationScope {
    private final String datasourceId;
    private final ReduxModel model;
    private final ValidationList validations;

    public ValidationScope(N2oDatasource datasource, ReduxModel model, ValidationList validations) {
        this.datasourceId = datasource.getId();
        this.model = model;
        this.validations = validations;
    }

    public void add(Validation validation) {
        validations.add(datasourceId, model, validation);
    }

    public void addAll(List<Validation> validationList) {
        validations.addAll(datasourceId, model, validationList);
    }

    public List<Validation> getAll() {
        return validations.get(datasourceId, model);
    }
}

