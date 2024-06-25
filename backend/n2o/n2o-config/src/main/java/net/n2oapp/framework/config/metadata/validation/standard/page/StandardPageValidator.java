package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.datasource.ValidatorDataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.validation.standard.ValidatorDatasourceIdsScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.metadata.validation.standard.PageValidationUtil.fillDatasourceIdsScopeByInlineDatasource;

/**
 * Валидатор модели стандартной страницы
 */
@Component
public class StandardPageValidator implements SourceValidator<N2oStandardPage>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardPage.class;
    }

    @Override
    public void validate(N2oStandardPage page, SourceProcessor p) {
        N2oAbstractDatasource[] datasources = page.getDatasources();
        ActionBar[] actions = page.getActions();
        List<N2oWidget> widgets = page.getWidgets();
        PageScope pageScope = new PageScope();
        pageScope.setWidgetIds(p.safeStreamOf(widgets).map(N2oMetadata::getId).collect(Collectors.toSet()));

        ValidatorDataSourcesScope dataSourcesScope = new ValidatorDataSourcesScope(
                p.safeStreamOf(datasources).collect(Collectors.toMap(N2oAbstractDatasource::getId, Function.identity())));
        ValidatorDatasourceIdsScope datasourceIdsScope = new ValidatorDatasourceIdsScope(
                p.safeStreamOf(datasources).map(N2oAbstractDatasource::getId).collect(Collectors.toSet())
        );
        fillDatasourceIdsScopeByInlineDatasource(widgets, datasourceIdsScope, p);

        MetaActions actionBarScope = new MetaActions(
                p.safeStreamOf(actions).collect(Collectors.toMap(ActionBar::getId, Function.identity()))
        );

        SourceComponent[] items = page.getItems();
        p.safeStreamOf(items).forEach(item -> p.validate(item, pageScope, datasourceIdsScope, dataSourcesScope, actionBarScope));
    }
}
