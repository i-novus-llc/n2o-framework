package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.SubPageRegion;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.SubPageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции региона `<sub-page>`
 */
class SubPageRegionCompilerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    void testSubPageRegion() {
        PageContext parentPageContext = new PageContext("testSubPageRegion", "/user/:parentId");
        Map<String, ModelLink> parentPathMapping = new HashMap<>();
        parentPathMapping.put("parentId", new ModelLink(ReduxModel.resolve, "main", "id"));
        parentPageContext.setPathRouteMapping(parentPathMapping);

        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testSubPageRegion.page.xml")
                .get(parentPageContext);

        List<String> subRoutes = page.getRoutes().getSubRoutes();
        assertThat(subRoutes, is(List.of("./route1", "./route2", "./route3/subroute", "./route4")));
        assertThat(page.getRoutes().getPath(), is("/user/:parentId"));

        SubPageRegion region = (SubPageRegion) page.getRegions().get("single").get(0);
        assertThat(region.getSrc(), is("SubPage"));
        assertThat(region.getDefaultPageId(), is("user_subpage2"));

        List<SubPageRegion.Page> pages = region.getPages();
        assertThat(pages.size(), is(4));
        assertThat(pages.get(0).getId(), is("user_subpage1"));
        assertThat(pages.get(0).getRoute(), is("./route1"));
        assertThat(pages.get(0).getUrl(), is("/user/:parentId/subpage1"));

        assertThat(pages.get(1).getId(), is("user_subpage2"));
        assertThat(pages.get(1).getRoute(), is("./route2"));
        assertThat(pages.get(1).getUrl(), is("/user/:parentId/subpage2"));

        assertThat(pages.get(2).getId(), is("user_subpage3"));
        assertThat(pages.get(2).getRoute(), is("./route3/subroute"));
        assertThat(pages.get(2).getUrl(), is("/user/:parentId/subpage3"));

        assertThat(pages.get(3).getId(), is("user_subpage4"));
        assertThat(pages.get(3).getRoute(), is("./route4"));
        assertThat(pages.get(3).getUrl(), is("/user/:parentId/subpage4"));

        // Проверка построения контекста первой подстраницы
        checkPageContext();
        assertThat(parentPageContext.getSubRoutes(), is(List.of(
                "/user/*/route1", "/user/*/route2",
                "/user/*/route3/subroute", "/user/*/route4"))
        );
    }

    private void checkPageContext() {
        PageContext pageContext = (SubPageContext) route("/user/:parentId/subpage1", Page.class);
        assertThat(pageContext.getParentRoute(), is("/user/:parentId"));
        assertThat(pageContext.getParentClientPageId(), is("user"));
        assertThat(pageContext.getParentRoutes().size(), is(1));
        assertThat(pageContext.getParentRoutes().get(0), is("/user/:parentId"));

        Map<String, ModelLink> pathMappings = pageContext.getPathRouteMapping();
        assertThat(pathMappings.size(), is(1));
        assertThat(pathMappings.get("parentId").getBindLink(), is("models.resolve['main'].id"));
        assertThat(pathMappings.get("parentId").getValue(), nullValue());
        Map<String, ModelLink> queryMappings = pageContext.getQueryRouteMapping();
        assertThat(queryMappings.size(), is(0));

        Map<String, String> parentDatasourceIdsMap = pageContext.getParentDatasourceIdsMap();
        assertThat(parentDatasourceIdsMap.size(), is(1));
        assertThat(parentDatasourceIdsMap.get("ds1"), is("user_ds1"));
        List<N2oAbstractDatasource> datasources = pageContext.getDatasources();
        assertThat(datasources.size(), is(1));
        assertThat(datasources.get(0), instanceOf(N2oInheritedDatasource.class));

        List<Breadcrumb> breadcrumbs = pageContext.getBreadcrumbs();
        assertThat(pageContext.getBreadcrumbFromParent(), is(true));
        assertThat(breadcrumbs.size(), is(2));
        assertThat(breadcrumbs.get(0).getLabel(), is("Первая страница"));
        assertThat(breadcrumbs.get(0).getPath(), is("../"));
        assertThat(breadcrumbs.get(1).getLabel(), is("Вторая страница"));
        assertThat(breadcrumbs.get(1).getPath(), is("#"));

        Map<String, ActionBar> actions = pageContext.getActions();
        assertThat(actions.size(), is(1));
        assertThat(actions.get("saveAction").getN2oActions()[0], instanceOf(N2oInvokeAction.class));

        List<N2oToolbar> toolbars = pageContext.getToolbars();
        assertThat(toolbars.size(), is(1));
        assertThat(toolbars.get(0).getPlace(), is("bottomRight"));
        assertThat(((N2oButton) toolbars.get(0).getItems()[0]).getLabel(), is("Сохранить"));
        assertThat(((N2oButton) toolbars.get(0).getItems()[0]).getActionId(), is("saveAction"));
    }
}
