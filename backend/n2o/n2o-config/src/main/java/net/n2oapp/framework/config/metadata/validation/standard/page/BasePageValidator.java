package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.datasource.ValidatorDataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.validation.standard.ValidatorDatasourceIdsScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.config.metadata.validation.standard.PageValidationUtil.fillDatasourceIdsScopeByInlineDatasource;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkOnFailAction;
import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор "Исходной" модели страницы
 */
@Component
public class BasePageValidator implements SourceValidator<N2oBasePage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBasePage.class;
    }

    @Override
    public void validate(N2oBasePage page, SourceProcessor p) {
        N2oAbstractDatasource[] datasources = page.getDatasources();
        ActionBar[] actions = page.getActions();
        N2oToolbar[] toolbars = page.getToolbars();
        List<N2oWidget> widgets = page.getWidgets();

        p.checkIdsUnique(datasources,
                String.format("Источник данных '%s' встречается более чем один раз в метаданной страницы %s", "%s",
                        getIdOrEmptyString(page.getId())));
        p.checkIdsUnique(actions,
                String.format("Действие '%s' встречается более чем один раз в метаданной страницы %s", "%s",
                        getIdOrEmptyString(page.getId())));
        p.checkIdsUnique(widgets,
                String.format("Виджет '%s' встречается более чем один раз на странице %s", "%s",
                        getIdOrEmptyString(page.getId())));

        PageScope pageScope = new PageScope();
        pageScope.setPageId(page.getId());
        pageScope.setWidgetIds(p.safeStreamOf(widgets).map(N2oMetadata::getId).collect(Collectors.toSet()));
        ValidatorDataSourcesScope dataSourcesScope = new ValidatorDataSourcesScope(
                p.safeStreamOf(datasources).collect(Collectors.toMap(N2oAbstractDatasource::getId, Function.identity())));
        ValidatorDatasourceIdsScope datasourceIdsScope = new ValidatorDatasourceIdsScope(
                p.safeStreamOf(datasources).map(N2oAbstractDatasource::getId).collect(Collectors.toSet())
        );
        MetaActions actionBarScope = new MetaActions(
                p.safeStreamOf(actions).collect(Collectors.toMap(ActionBar::getId, Function.identity()))
        );
        if (actions != null)
            Stream.of(actions).forEach(actionbar ->
                    checkOnFailAction(actionbar.getN2oActions()));

        checkDuplicateWidgetIdsInDatasources(widgets, datasourceIdsScope);
        fillDatasourceIdsScopeByInlineDatasource(widgets, datasourceIdsScope, p);

        p.safeStreamOf(toolbars)
                .forEach(n2oToolbar -> p.safeStreamOf(n2oToolbar.getAllActions())
                        .forEach(action -> p.validate(action, pageScope, datasourceIdsScope, dataSourcesScope)));
        p.safeStreamOf(toolbars).map(N2oToolbar::getItems).filter(Objects::nonNull)
                .flatMap(Arrays::stream).forEach(button -> p.validate(button, datasourceIdsScope, dataSourcesScope,
                        actionBarScope, new ComponentScope(page)));
        p.safeStreamOf(actions).flatMap(actionBar -> p.safeStreamOf(actionBar.getN2oActions()))
                .forEach(action -> p.validate(action, pageScope, datasourceIdsScope, dataSourcesScope));

        p.safeStreamOf(datasources).forEach(datasource -> p.validate(datasource, pageScope, datasourceIdsScope));
        p.safeStreamOf(page.getEvents()).forEach(event -> p.validate(event, pageScope, datasourceIdsScope, dataSourcesScope, actionBarScope));

        checkEmptyToolbar(toolbars);
    }

    private static void checkEmptyToolbar(N2oToolbar[] toolbars) {
        if (nonNull(toolbars))
            for (N2oToolbar toolbar : toolbars) {
                if (toolbar.getItems() == null && toolbar.getGenerate() == null)
                    throw new N2oMetadataValidationException("Не заданы элементы или атрибут 'generate' в тулбаре страницы");
            }
    }

    private void checkDuplicateWidgetIdsInDatasources(List<N2oWidget> widgets, ValidatorDatasourceIdsScope datasourceIdsScope) {
        widgets.forEach(n2oWidget -> {
            if (datasourceIdsScope.contains(n2oWidget.getId()))
                throw new N2oMetadataValidationException(
                        String.format("Идентификатор виджета %s не должен использоваться в качестве идентификатора источника данных",
                                getIdOrEmptyString(n2oWidget.getId())));
            if (n2oWidget.getDatasource() != null && datasourceIdsScope.contains(n2oWidget.getDatasource().getId()))
                throw new N2oMetadataValidationException(
                        String.format("Идентификатор %s внутреннего источника данных виджета %s уже существует среди источников данных страницы",
                                getIdOrEmptyString(n2oWidget.getDatasource().getId()),
                                getIdOrEmptyString(n2oWidget.getId())));
        });
    }
}
