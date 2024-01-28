package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import net.n2oapp.framework.config.metadata.validation.standard.widget.FieldsScope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkId;

/**
 * Валидатор поля
 */
@Component
public class FieldValidator implements SourceValidator<N2oField>, SourceClassAware {

    @Override
    public void validate(N2oField source, SourceProcessor p) {
        checkId(source, p);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (p.getScope(FieldsScope.class) != null) {
            FieldsScope scope = p.getScope(FieldsScope.class);
            Boolean sameFieldIdHasDependency = scope.get(source.getId());
            if (sameFieldIdHasDependency != null && (sameFieldIdHasDependency || source.getDependencies() != null))
                throw new N2oMetadataValidationException(
                        String.format("Поле %s встречается более одного раза",
                                ValidationUtils.getIdOrEmptyString(source.getId())));
            scope.put(source.getId(), source.getDependencies() != null);
        }
        checkDefaultValues(source);
        checkDependencies(source);
        checkRefDatasource(source, p);
        if (widgetScope != null)
            checkWhiteListValidation(source, widgetScope, p);
        if (source.getToolbar() != null)
            p.safeStreamOf(source.getToolbar().getItems()).forEach(p::validate);

        checkEmptyToolbar(source);
    }

    private static void checkEmptyToolbar(N2oField source) {
        if (source.getToolbar() != null
                && source.getToolbar().getItems() == null)
            throw new N2oMetadataValidationException(
                    String.format("Не заданы элементы или атрибут 'generate' в тулбаре поля %s",
                            ValidationUtils.getIdOrEmptyString(source.getId())));
    }

    /**
     * Если в поле указаны ссылки на другой виджет, то значение по умолчанию обязательно должно быть ссылкой
     *
     * @param source Поле
     */
    private void checkDefaultValues(N2oField source) {
        if ((source.getRefPage() != null || source.getRefDatasourceId() != null || source.getRefModel() != null)
                && source.getRefFieldId() == null)
            if (source instanceof N2oListField) {
                N2oListField list = (N2oListField) source;
                checkListFieldDefaultValues(list);
            } else if (source instanceof N2oSimpleIntervalField) {
                N2oSimpleIntervalField interval = (N2oSimpleIntervalField) source;
                if (interval.getBegin() == null && interval.getEnd() == null)
                    throw new N2oMetadataValidationException(
                            String.format("У поля %s default-value не задан", ValidationUtils.getIdOrEmptyString(source.getId())));
                if (!StringUtils.isLink(interval.getBegin()) && !StringUtils.isLink(interval.getEnd()))
                    throw new N2oMetadataValidationException(
                            String.format("У поля %s default-value не является ссылкой", ValidationUtils.getIdOrEmptyString(source.getId())));
            } else if (!StringUtils.isLink(source.getDefaultValue())) {
                throw new N2oMetadataValidationException(
                        String.format("У поля %s атрибут default-value не является ссылкой или не задан: '%s'",
                                ValidationUtils.getIdOrEmptyString(source.getId()), source.getDefaultValue()));
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
                    String.format("У поля %s default-value не является ссылкой", ValidationUtils.getIdOrEmptyString(list.getId())));
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
                            String.format("В поле %s повторяются зависимости одного типа", ValidationUtils.getIdOrEmptyString(source.getId())));
            }
        }
    }

    /**
     * Проверка существования ссылки на источник данных поля
     *
     * @param source             Поле
     */
    private void checkRefDatasource(N2oField source, SourceProcessor p) {
        if (source.getRefDatasourceId() != null && PageRef.THIS.equals(source.getRefPage())) {
            ValidationUtils.checkDatasourceExistence(source.getRefDatasourceId(), p,
                    String.format("В ссылке на источник данных поля %s содержится несуществующий источник данных '%s'",
                            ValidationUtils.getIdOrEmptyString(source.getId()), source.getRefDatasourceId()));
        }
    }

    /**
     * Проверка наличия ссылки на корректный источник данных у виджета при наличии white-list валидации на поле
     *
     * @param source      Поле с white-list валидацией
     * @param widgetScope Скоуп виджета, в котором находится поле
     * @param p           Процессор исходных метаданных
     */
    private void checkWhiteListValidation(N2oField source, WidgetScope widgetScope, SourceProcessor p) {
        N2oField.Validations validations = source.getValidations();
        if (validations != null && validations.getWhiteList() != null) {
            if (widgetScope.getInLineDatasource() != null) {
                checkInlineDatasource(source, widgetScope);
                return;
            }
            checkDatasource(source, widgetScope, p);
        }
    }

    /**
     * Проверка внутреннего источника данных у виджета, в котором находится поле
     *
     * @param source      Поле
     * @param widgetScope Скоуп виджета, в котором находится поле
     */
    private void checkInlineDatasource(N2oField source, WidgetScope widgetScope) {
        checkDatasourceObject(source, widgetScope.getInLineDatasource(), widgetScope);
    }

    /**
     * Проверка наличия ссылки на источник данных у виджета, в котором находится поле
     *
     * @param source      Поле
     * @param widgetScope Скоуп виджета, в котором находится поле
     * @param p           Процессор исходных метаданных
     */
    private void checkDatasource(N2oField source, WidgetScope widgetScope, SourceProcessor p) {
        if (widgetScope.getDatasourceId() == null)
            throw new N2oMetadataValidationException(
                    String.format("Для компиляции поля %s необходимо указать атрибут 'datasource' или ввести внутренний источник данных виджета %s",
                            ValidationUtils.getIdOrEmptyString(source.getId()),
                            ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId()))
            );
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        if (dataSourcesScope != null && dataSourcesScope.get(widgetScope.getDatasourceId()) instanceof N2oStandardDatasource)
            checkDatasourceObject(source, (N2oStandardDatasource) dataSourcesScope.get(widgetScope.getDatasourceId()), widgetScope);
    }

    /**
     * Проверка наличия атрибута object у указанного в виджете источника данных
     *
     * @param source      Поле
     * @param datasource  Источник данных
     * @param widgetScope Скоуп виджета, в котором находится поле
     */
    private void checkDatasourceObject(N2oField source, N2oStandardDatasource datasource, WidgetScope widgetScope) {
        if (datasource.getObjectId() == null)
            throw new N2oMetadataValidationException(
                    String.format("Для компиляции поля %s виджета %s необходимо указать объект источника данных %s",
                            ValidationUtils.getIdOrEmptyString(source.getId()),
                            ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId()),
                            ValidationUtils.getIdOrEmptyString(datasource.getId())));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oField.class;
    }
}
