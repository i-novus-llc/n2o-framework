package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FieldsScope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Валидатор поля
 */
@Component
public class FieldValidator implements SourceValidator<N2oField>, SourceClassAware {

    @Override
    public void validate(N2oField source, ValidateProcessor p) {
        if (p.getScope(FieldsScope.class) != null) {
            FieldsScope scope = p.getScope(FieldsScope.class);
            scope.add(source);
            if (source.getDependencies() != null)
                scope.setHasDependencies(true);
            if (scope.isHasDependencies())
                p.checkIdsUnique(scope, "Поле {0} встречается более одного раза");
        }
        checkDefaultValues(source);
        checkDependencies(source);
    }

    /**
     * Если в поле указаны ссылки на другой виджет, то значение по умолчанию обязательно должно быть ссылкой
     *
     * @param source Поле
     */
    private void checkDefaultValues(N2oField source) {
        if ((source.getRefPage() != null || source.getRefWidgetId() != null || source.getRefModel() != null)
                && source.getRefFieldId() == null)
            if (source instanceof N2oListField) {
                N2oListField list = (N2oListField) source;
                checkListFieldDefaultValues(list);
            } else if (source instanceof N2oSimpleIntervalField) {
                N2oSimpleIntervalField interval = (N2oSimpleIntervalField) source;
                if (interval.getBegin() == null && interval.getEnd() == null)
                    throw new N2oMetadataValidationException(
                            String.format("У поля %s default-value не задан", source.getId()));
                if (!StringUtils.isLink(interval.getBegin()) && !StringUtils.isLink(interval.getEnd()))
                    throw new N2oMetadataValidationException(
                            String.format("У поля %s default-value не является ссылкой", source.getId()));
            } else if (!StringUtils.isLink(source.getDefaultValue())) {
                throw new N2oMetadataValidationException(
                        String.format("У поля %s атрибут default-value не является ссылкой или не задан: %s",
                                source.getId(), source.getDefaultValue()));
            }
    }

    /**
     * Проверка значений по умолчанию спискового поля
     *
     * @param list Списковое поле
     */
    private void checkListFieldDefaultValues(N2oListField list) {
        if (list.getDefValue() == null)
            throw new N2oMetadataValidationException(
                    String.format("У поля %s default-value не задан", list.getId()));
        if (list.getDefValue().values().stream().filter(StringUtils::isLink).findFirst().isEmpty())
            throw new N2oMetadataValidationException(
                    String.format("У поля %s default-value не является ссылкой", list.getId()));
    }

    /**
     * Проверка зависимостей поля
     *
     * @param source Поле
     */
    private void checkDependencies(N2oField source) {
        if (source.getDependencies() != null) {
            Set<Class<?>> dependencyClasses = new HashSet<>();
            for (N2oField.Dependency dependency : source.getDependencies()) {
                if (!N2oField.SetValueDependency.class.equals(dependency.getClass()) &&
                        !dependencyClasses.add(dependency.getClass()))
                    throw new N2oMetadataValidationException(
                            String.format("В поле %s повторяются зависимости одного типа", source.getId()));
            }
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oField.class;
    }
}
