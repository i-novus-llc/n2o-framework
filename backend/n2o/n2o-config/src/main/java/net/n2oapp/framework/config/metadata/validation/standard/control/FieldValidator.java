package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
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
    public void validate(N2oField source, SourceProcessor p) {
        if (p.getScope(FieldsScope.class) != null) {
            FieldsScope scope = p.getScope(FieldsScope.class);
            Boolean sameFieldIdHasDependency = scope.get(source.getId());
            if (sameFieldIdHasDependency != null && (sameFieldIdHasDependency || source.getDependencies() != null))
                throw new N2oMetadataValidationException(
                        String.format("Поле %s встречается более одного раза", source.getId()));
            scope.put(source.getId(), source.getDependencies() != null);
        }
        checkDefaultValues(source);
        checkDependencies(source);
        DatasourceIdsScope datasourceIdsScope = p.getScope(DatasourceIdsScope.class);
        checkRefDatasource(source, datasourceIdsScope);
    }

    /**
     * Если в поле указаны ссылки на другой виджет, то значение по умолчанию обязательно должно быть ссылкой
     *
     * @param source Поле
     */
    private void checkDefaultValues(N2oField source) {
        if ((source.getRefPage() != null || source.getRefDatasource() != null || source.getRefModel() != null)
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
            } else if (!StringUtils.isLink(source.getDefaultValue()) ) {
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
        if (list.getDefValue() != null && list.getDefValue().values().stream().filter(StringUtils::isLink).findFirst().isEmpty())
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

    /**
     * Проверка существования ссылки на источник данных поля
     * @param source           Поле
     * @param datasourceIdsScope Скоуп источников данных
     */
    private void checkRefDatasource(N2oField source, DatasourceIdsScope datasourceIdsScope) {
        if (source.getRefDatasource() != null && N2oField.Page.THIS.equals(source.getRefPage())) {
            ValidationUtils.checkForExistsDatasource(source.getRefDatasource(), datasourceIdsScope,
                    String.format("В ссылке на источник данных поля %s содержится несуществующий источник данных '%s'",
                            source.getId(), source.getRefDatasource()));
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oField.class;
    }
}
