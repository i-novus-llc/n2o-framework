package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
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
                p.checkIdsUnique(scope, "Поле {0} встречается более одного раза.");
        }
        if ((source.getRefPage() != null || source.getRefWidgetId() != null || source.getRefModel() != null) &&
                !StringUtils.isLink(source.getDefaultValue())) {
            throw new N2oMetadataValidationException(p.getMessage(
                    "Атрибут default-value не является ссылкой или не задан: " + source.getDefaultValue()));
        }

        checkDependencies(source, p);
    }

    private void checkDependencies(N2oField source, ValidateProcessor p) {
        if (source.getDependencies() != null) {
            Set<Class<?>> dependencyClasses = new HashSet<>();
            for (N2oField.Dependency dependency : source.getDependencies()) {
                if (!dependencyClasses.contains(dependency.getClass()))
                    dependencyClasses.add(dependency.getClass());
                else
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
