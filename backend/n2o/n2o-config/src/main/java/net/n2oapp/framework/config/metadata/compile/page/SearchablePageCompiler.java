package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SearchablePage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.SearchBarScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        page.setSearchModelPrefix(ReduxModel.FILTER.getId());
        page.setSearchWidgetId(source.getSearchBar().getSearchWidgetId());
        page.setSearchModelKey(source.getSearchBar().getSearchFilterId());

        SearchablePage.SearchBar searchBar = new SearchablePage.SearchBar();
        searchBar.setClassName(source.getSearchBar().getClassName());
        searchBar.setTrigger(SearchablePage.SearchBar.TriggerType.valueOf(p.resolve(property("n2o.api.page.searchable.trigger"), String.class)));
        searchBar.setPlaceholder(source.getSearchBar().getPlaceholder());
        if (SearchablePage.SearchBar.TriggerType.BUTTON.equals(searchBar.getTrigger())) {
            searchBar.setButton(new SearchablePage.SearchBar.Button());
        } else if (SearchablePage.SearchBar.TriggerType.CHANGE.equals(searchBar.getTrigger())) {
            searchBar.setThrottleDelay(p.resolve(property("n2o.api.page.searchable.throttle-delay"), Integer.class));
        }
        page.setSearchBar(searchBar);

        SearchBarScope searchBarScope = new SearchBarScope(page.getSearchWidgetId(), page.getSearchModelKey());
        compilePage(source, page, context, p, source.getRegions(), searchBarScope);
        if (page.getSearchWidgetId() == null)
            page.setSearchWidgetId(searchBarScope.getWidgetId());

        compileSearchBarRoute(page, source.getSearchBar().getSearchParam());
        return page;
    }

    @Override
    protected void initRegions(N2oSearchablePage source, SearchablePage page, CompileProcessor p,
                               PageContext context, PageScope pageScope, PageRoutes pageRoutes) {
        Map<String, List<Region>> regionMap = new HashMap<>();
        if (source.getRegions() != null) {
            IndexScope index = new IndexScope();
            for (N2oRegion n2oRegion : source.getRegions()) {
                Region region = p.compile(n2oRegion, context, index, pageScope);
                String place = p.cast(n2oRegion.getPlace(), "single");
                if (regionMap.get(place) != null) {
                    regionMap.get(place).add(region);
                } else {
                    List<Region> regionList = new ArrayList<>();
                    regionList.add(region);
                    regionMap.put(place, regionList);
                }
            }
            page.setRegions(regionMap);
        }
    }

    private void compileSearchBarRoute(SearchablePage page, String param) {
        ReduxModel model = ReduxModel.valueOf(page.getSearchModelPrefix().toUpperCase());
        if (param == null)
            param = page.getSearchWidgetId() + "_" + page.getSearchModelKey();
        ModelLink modelLink = new ModelLink(model, page.getSearchWidgetId());
        modelLink.setFieldValue(page.getSearchModelKey());
        page.getRoutes().addQueryMapping(param, Redux.dispatchUpdateModel(page.getSearchWidgetId(), model, page.getSearchModelKey(), colon(param)), modelLink);
    }

    @Override
    protected String getPropertyPageSrc() {
        return "n2o.api.page.searchable.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSearchablePage.class;
    }
}