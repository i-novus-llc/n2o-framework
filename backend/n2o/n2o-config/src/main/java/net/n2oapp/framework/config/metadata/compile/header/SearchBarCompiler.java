package net.n2oapp.framework.config.metadata.compile.header;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.header.N2oSearchBar;
import net.n2oapp.framework.api.metadata.header.SearchBar;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.QueryContextUtil.prepareQueryContextForRouteRegister;

/**
 * Компиляция панели поиска
 */
@Component
public class SearchBarCompiler implements BaseSourceCompiler<SearchBar, N2oSearchBar, ApplicationContext>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchBar.class;
    }

    @Override
    public SearchBar compile(N2oSearchBar source, ApplicationContext context, CompileProcessor p) {
        SearchBar searchBar = new SearchBar();
        searchBar.setUrlFieldId(source.getUrlFieldId());
        searchBar.setLabelFieldId(source.getLabelFieldId());
        searchBar.setIconFieldId(source.getIconFieldId());
        searchBar.setDescrFieldId(source.getDescriptionFieldId());

        searchBar.setSearchPageLocation(initPageLocation(source));
        searchBar.setDataProvider(initDataProvider(source, p));

        return searchBar;
    }

    private SearchBar.SearchPageLocation initPageLocation(N2oSearchBar source) {
        SearchBar.SearchPageLocation pageLocation = new SearchBar.SearchPageLocation();
        pageLocation.setUrl(source.getAdvancedUrl());
        pageLocation.setSearchQueryName(source.getAdvancedParam());
        pageLocation.setLinkType(source.getAdvancedTarget() == Target.newWindow ? SearchBar.LinkType.outer : SearchBar.LinkType.inner);
        return pageLocation;
    }

    private ClientDataProvider initDataProvider(N2oSearchBar source, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        QueryContext queryContext = new QueryContext(source.getQueryId());
        CompiledQuery query = p.getCompiled(queryContext);
        p.addRoute(prepareQueryContextForRouteRegister(query));
        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + query.getRoute());
        dataProvider.setQuickSearchParam(source.getFilterFieldId());
        return dataProvider;
    }
}
