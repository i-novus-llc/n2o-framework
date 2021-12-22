package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SearchablePage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.SearchBarScope;
import net.n2oapp.framework.config.util.CompileUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция страницы с регионами и поисковой строкой
 */
@Component
public class SearchablePageCompiler extends BasePageCompiler<N2oSearchablePage, SearchablePage> {

    @Override
    public SearchablePage compile(N2oSearchablePage source, PageContext context, CompileProcessor p) {
        SearchablePage page = new SearchablePage();
        initDefaults(source, context, p);
        SearchBarScope searchBarScope = new SearchBarScope(source.getSearchBar().getDatasource(), source.getSearchBar().getSearchFilterId());
        searchBarScope.setParam(source.getSearchBar().getSearchParam());
        page = compilePage(source, page, context, p, source.getItems(), searchBarScope);

        page.setSearchBar(compileSearchBar(source, page, p));

        compileSearchBarRoute(page, source.getSearchBar().getSearchParam());
        return page;
    }

    private void initDefaults(N2oSearchablePage source, PageContext context, CompileProcessor p) {
        source.setSearchBar(initSearchBar(source.getSearchBar(), p));
    }

    private N2oSearchablePage.N2oSearchBar initSearchBar(N2oSearchablePage.N2oSearchBar source, CompileProcessor p) {
        N2oSearchablePage.N2oSearchBar result = source;
        if (result == null)
            result = new N2oSearchablePage.N2oSearchBar();
        result.setSearchParam(p.cast(result.getSearchParam(), result.getDatasource() + "_" + result.getSearchFilterId()));
        return result;
    }

    @Override
    protected Map<String, List<Region>>  initRegions(N2oSearchablePage source, SearchablePage page, CompileProcessor p, PageContext context,
                               PageScope pageScope, PageRoutes pageRoutes, Object... scopes) {
        Map<String, List<Region>> regions = new HashMap<>();
        initRegions(source.getItems(), regions, "single", context, p, pageScope, pageRoutes, new IndexScope(), scopes);
        return regions;
    }

    protected SearchablePage.SearchBar compileSearchBar(N2oSearchablePage source, SearchablePage page, CompileProcessor p) {
        SearchablePage.SearchBar searchBar = new SearchablePage.SearchBar();
        searchBar.setClassName(source.getSearchBar().getClassName());
        searchBar.setTrigger(SearchablePage.SearchBar.TriggerType.valueOf(p.resolve(property("n2o.api.page.searchable.trigger"), String.class)));
        searchBar.setPlaceholder(source.getSearchBar().getPlaceholder());
        if (SearchablePage.SearchBar.TriggerType.BUTTON.equals(searchBar.getTrigger())) {
            searchBar.setButton(new SearchablePage.SearchBar.Button());
        } else if (SearchablePage.SearchBar.TriggerType.CHANGE.equals(searchBar.getTrigger())) {
            searchBar.setThrottleDelay(p.resolve(property("n2o.api.page.searchable.throttle-delay"), Integer.class));
        }
        searchBar.setFieldId(source.getSearchBar().getSearchFilterId());
        searchBar.setDatasource(CompileUtil.generateWidgetId(page.getId(), source.getSearchBar().getDatasource()));
        return searchBar;
    }

    private void compileSearchBarRoute(SearchablePage page, String param) {
        ReduxModel model = ReduxModel.FILTER;
        ModelLink modelLink = new ModelLink(model, page.getSearchBar().getDatasource());
        modelLink.setFieldValue(page.getSearchBar().getFieldId());
        page.getRoutes().addQueryMapping(param, Redux.dispatchUpdateModel(page.getSearchBar().getDatasource(), model, page.getSearchBar().getFieldId(), colon(param)), modelLink);
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.searchable.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchablePage.class;
    }
}