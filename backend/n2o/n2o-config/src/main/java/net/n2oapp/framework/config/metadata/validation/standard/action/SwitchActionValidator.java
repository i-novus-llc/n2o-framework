package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oSwitchAction;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkCloseInMultiAction;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailActionNotExist;
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
            throw new N2oMetadataValidationException("В действии \"<switch>\" не указан 'value-field-id'");
        ValidationUtils.checkDatasourceExistenceInTag(source.getDatasourceId(), p, "\"<switch>\"");
        N2oSwitchAction.AbstractCase[] cases = source.getCases();
        if (isEmpty(cases))
            return;

        List<N2oSwitchAction.Case> valueCases = source.getValueCases();
        valueCases.forEach(this::checkValue);
        checkValueCasesUnique(valueCases);
        checkDefaultUnique(cases);

        if (source.getDefaultCase() != null && !(cases[cases.length - 1] instanceof N2oSwitchAction.DefaultCase)) {
            throw new N2oMetadataValidationException("В действии \"<switch>\" после \"<default>\" указан \"<case>\"");
        }

        Arrays.stream(cases).forEach(c -> {
            checkOnFailActionNotExist(c.getActions(), "\"<switch>\"");
            checkCloseInMultiAction(c.getActions());
            p.validate(c);
        });
    }

    private void checkValue(N2oSwitchAction.Case valueCase) {
        if (valueCase.getValue() == null)
            throw new N2oMetadataValidationException("В \"<case>\" действия \"<switch>\" не указан атрибут 'value'");
    }

    /**
     * Значение атрибута 'value' у тегов {@code <case>} должно быть уникальным
     */
    private void checkValueCasesUnique(List<N2oSwitchAction.Case> valueCases) {
        Set<String> seen = new HashSet<>();
        for (N2oSwitchAction.Case c : valueCases) {
            if (!seen.add(c.getValue()))
                throw new N2oMetadataValidationException(String.format(
                        "В действии \"<switch>\" значение '%s' у \"<case>\" встречается более чем один раз", c.getValue()));
        }
    }

    /**
     * В действии {@code <switch>} может быть только один {@code <default>}
     */
    private void checkDefaultUnique(N2oSwitchAction.AbstractCase[] cases) {
        long defaultsCount = Arrays.stream(cases)
                .filter(N2oSwitchAction.DefaultCase.class::isInstance)
                .count();
        if (defaultsCount > 1)
            throw new N2oMetadataValidationException(
                    "В действии \"<switch>\" может быть только один \"<default>\"");
    }
}