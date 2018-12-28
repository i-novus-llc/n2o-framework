package net.n2oapp.framework.api.data.validation;

import java.util.List;

/**
 * @author operehod
 * @since 02.04.2015
 */
public interface ValidationSupplier {

    List<Validation> getValidations();

    default String getFieldId() {
        return null;
    }

    default boolean isFieldAware() {
        return getFieldId() != null;
    }
}
