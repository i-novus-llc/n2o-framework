package net.n2oapp.framework.engine.validation.engine.info;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.Collections;
import java.util.List;

import static net.n2oapp.framework.engine.validation.engine.ValidationUtil.calculateVisibleValidation;

/**
 * @author operehod
 * @since 02.04.2015
 */
@Getter
public class FieldsValidationInfo {

    private CompiledObject object;
    private List<CompiledObject.Operation> operations;
    private DataSet dataSet;
    private List<Validation> visibleValidations;
    private String messageForm;

    public FieldsValidationInfo(CompiledObject object, CompiledObject.Operation action, DataSet dataSet, String messageForm) {
        this.object = object;
        this.operations = Collections.singletonList(action);
        this.dataSet = dataSet;
        this.visibleValidations = calculateVisibleValidation(object.getFieldValidations(), dataSet);
        this.messageForm = messageForm;
    }

    public FieldsValidationInfo(CompiledObject object, DataSet dataSet, String messageForm) {
        this.object = object;
        this.operations = Collections.emptyList();
        this.dataSet = dataSet;
        this.visibleValidations = calculateVisibleValidation(object.getFieldValidations(), dataSet);
        this.messageForm = messageForm;
    }

}
