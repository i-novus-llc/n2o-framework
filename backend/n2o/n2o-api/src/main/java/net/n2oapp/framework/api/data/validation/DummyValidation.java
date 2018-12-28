package net.n2oapp.framework.api.data.validation;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;

/**
 * @author operehod
 * @since 27.04.2015
 */
public class DummyValidation extends Validation {
    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        //ничего
    }

    @Override
    public String getType() {
        return null;
    }
}
