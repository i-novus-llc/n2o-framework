package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.action.N2oSwitchAction;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * Валидатор действия switch
 */
@Component
public class SwitchActionValidator extends TypedMetadataValidator<N2oSwitchAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSwitchAction.class;
    }

    @Override
    public void validate(N2oSwitchAction source, SourceProcessor p) {
        if (source.getValueFieldId() == null)
            throw new N2oMetadataValidationException("В действии <switch> не указан 'value-field-id'");
        ValidationUtils.checkDatasourceExistence(source.getDatasourceId(), p, "<switch>");
        N2oSwitchAction.AbstractCase[] cases = source.getCases();
        if (isEmpty(cases))
            return;

        if (source.getDefaultCase() != null && !(cases[cases.length - 1] instanceof N2oSwitchAction.DefaultCase)) {
            throw new N2oMetadataValidationException("В действии <switch> после <default> указан <case>");
        }

        source.getValueCases().forEach(this::checkValue);
        Arrays.stream(cases).forEach(p::validate);
    }

    private void checkValue(N2oSwitchAction.Case valueCase) {
        if (valueCase.getValue() == null)
            throw new N2oMetadataValidationException("В <case> действия <switch> не указан атрибут 'value'");
    }
}
