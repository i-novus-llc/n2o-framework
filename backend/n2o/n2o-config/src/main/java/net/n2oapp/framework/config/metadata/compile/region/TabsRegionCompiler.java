package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static net.n2oapp.framework.api.StringUtils.*;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция региона в виде вкладок
 */
@Component
public class TabsRegionCompiler extends BaseRegionCompiler<TabsRegion, N2oTabsRegion> {

    private static final String ALWAYS_REFRESH = "n2o.api.region.tabs.always_refresh";
    private static final String LAZY = "n2o.api.region.tabs.lazy";
    private static final String SCROLLBAR = "n2o.api.region.tabs.scrollbar";
    private static final String MAX_HEIGHT = "n2o.api.region.tabs.max_height";
    private static final String HIDE_SINGLE_TAB = "n2o.api.region.tabs.hide_single_tab";
    private static final String ROUTABLE = "n2o.api.region.tabs.routable";

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.tabs.src";
    }

    @Override
    public Class<N2oTabsRegion> getSourceClass() {
        return N2oTabsRegion.class;
    }

    @Override
    public TabsRegion compile(N2oTabsRegion source, PageContext context, CompileProcessor p) {
        TabsRegion region = new TabsRegion();
        build(region, source, p);
        region.setItems(initItems(source, context, p));
        region.setAlwaysRefresh(
                p.cast(source.getAlwaysRefresh(), () -> p.resolve(property(ALWAYS_REFRESH), Boolean.class))
        );
        region.setLazy(
                p.cast(source.getLazy(), () -> p.resolve(property(LAZY), Boolean.class))
        );
        region.setScrollbar(
                p.cast(source.getScrollbar(), () -> p.resolve(property(SCROLLBAR), Boolean.class))
        );
        region.setMaxHeight(prepareSizeAttribute(
                p.cast(source.getMaxHeight(), () -> p.resolve(property(MAX_HEIGHT), String.class)))
        );
        region.setHideSingleTab(
                p.cast(source.getHideSingleTab(), () -> p.resolve(property(HIDE_SINGLE_TAB), Boolean.class))
        );
        region.setActiveTabFieldId(source.getActiveTabFieldId());
        region.setDatasource(getClientDatasourceId(source.getDatasourceId(), p));
        region.setActiveParam(source.getActiveParam());
        compileRoute(source, region.getId(), ROUTABLE, p);

        return region;
    }

    @Override
    protected String createId(CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String regionName = getDefaultId(pageScope, "tabs");

        return createId(regionName, p);
    }


    protected List<TabsRegion.Tab> initItems(N2oTabsRegion source, PageContext context, CompileProcessor p) {
        List<TabsRegion.Tab> items = new ArrayList<>();
        if (source.getTabs() != null)
            for (N2oTabsRegion.Tab t : source.getTabs()) {
                TabsRegion.Tab tab = new TabsRegion.Tab();
                tab.setId(createTabId(t.getId(), source.getAlias(), p));
                tab.setLabel(t.getName());
                tab.setProperties(p.mapAttributes(t));
                tab.setContent(initContent(t.getContent(), context, p, t));
                // opened only first tab
                tab.setOpened(items.isEmpty());
                if (t.getDatasource() == null)
                    t.setDatasource(source.getDatasourceId());
                tab.setDatasource(getClientDatasourceId(t.getDatasource(), p));
                compileLinkConditions(t, tab, p);
                items.add(tab);
            }

        return items;
    }

    private void compileLinkConditions(N2oTabsRegion.Tab source, TabsRegion.Tab tab, CompileProcessor p) {
        if (isLink(source.getVisible()))
            compileLink(tab, ValidationType.visible, source.getVisible(), ReduxModel.resolve);
        else
            tab.setVisible(p.resolveJS(source.getVisible(), Boolean.class));

        if (isLink(source.getEnabled()))
            compileLink(tab, ValidationType.enabled, source.getEnabled(), ReduxModel.resolve);
        else
            tab.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
    }

    private void compileLink(TabsRegion.Tab tab, ValidationType type, String linkCondition, ReduxModel model) {
        if (tab.getDatasource() == null)
            throw new N2oException(
                    String.format("Для вкладки '%s' не может быть реализовано условие visible/enabled, т.к. не задан datasource",
                            tab.getLabel()));
        Condition condition = new Condition();
        condition.setExpression(unwrapLink(linkCondition));
        condition.setModelLink(new ModelLink(model, tab.getDatasource()).getBindLink());
        tab.getConditions().put(type, List.of(condition));
    }

    private String createTabId(String regionId, String alias, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String regionName = getDefaultId(pageScope, alias);
        String id = p.cast(regionId, createId(regionName, p));

        //проверяем id на уникальность
        if (pageScope != null) {
            if (pageScope.getTabIds() == null)
                pageScope.setTabIds(new HashSet<>());
            else if (pageScope.getTabIds().contains(id))
                throw new N2oException(String.format("Вкладка с идентификатором '%s' уже существует", id));
            pageScope.getTabIds().add(id);
        }

        return id;
    }

    private String getDefaultId(PageScope scope, String id) {
        return (scope == null || "_".equals(scope.getPageId())) ?
                id :
                scope.getPageId().concat("_").concat(id);
    }
}
