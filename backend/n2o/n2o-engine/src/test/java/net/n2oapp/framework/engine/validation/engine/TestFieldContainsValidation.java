package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.data.validation.ValidationFailureCallback;

import java.util.Collections;
import java.util.HashSet;

/**
 * @author operehod
 * @since 02.04.2015
 */
public class TestFieldContainsValidation extends Validation {

    private String field;

    public TestFieldContainsValidation(String field) {
        this.field = field;
        setFields(new HashSet<>(Collections.singletonList(field)));
    }

    @Override
    public String getId() {
        return "canResolved." + field;
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        if (dataSet.get(field) == null)
            callback.onFail(String.format("%s is missing", field));
    }

    @Override
    public String getType() {
        return null;
    }
}
