package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.UpdateModelPayload;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SearchablePage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SearchablePageCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testSearchablePageDefault() {
        SearchablePage page = (SearchablePage) compile("net/n2oapp/framework/config/metadata/compile/page/testSearchablePage.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml")
                .get(new PageContext("testSearchablePage"));

        assertThat(page.getSrc(), is("SearchablePage"));
        assertThat(page.getSearchBar().getClassName(), is("n2o-search-bar"));
        assertThat(page.getSearchBar().getPlaceholder(), is("Поиск по имени"));
        assertThat(page.getSearchBar().getTrigger(), is(SearchablePage.SearchBar.TriggerType.CHANGE));
        assertThat(page.getSearchBar().getThrottleDelay(), is(1000));
        assertThat(page.getSearchModelPrefix(), is("filter"));
        assertThat(page.getSearchWidgetId(), is("table"));
        assertThat(page.getSearchModelKey(), is("name"));

        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("table_name");
        assertThat(query.getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getPrefix(), is("filter"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getKey(), is("table"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getField(), is("name"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getValue(), is(":table_name"));
        assertThat(query.getOnSet().getValue(), is("`name`"));
        assertThat(query.getOnSet().getBindLink(), is("models.filter['table']"));
        assertThat(((ModelLink) query.getOnSet()).getModel(), is(ReduxModel.FILTER));
        assertThat(((ModelLink) query.getOnSet()).getWidgetId(), is("table"));

        BindLink bindLink = ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getDataProvider().getQueryMapping().get("name");
        assertThat(bindLink.getValue(), is("`name`"));
        assertThat(bindLink.getBindLink(), is("models.filter['table']"));
    }

    @Test
    public void testSearchablePage() {
        SearchablePage page = (SearchablePage) compile("net/n2oapp/framework/config/metadata/compile/page/testSearchablePage2.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml")
                .get(new PageContext("testSearchablePage2"));

        assertThat(page.getSrc(), is("SearchablePage"));
        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("name");
        assertThat(query.getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getPrefix(), is("filter"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getKey(), is("table"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getField(), is("name"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getValue(), is(":name"));
        assertThat(query.getOnSet().getValue(), is("`name`"));
        assertThat(query.getOnSet().getBindLink(), is("models.filter['table']"));
        assertThat(((ModelLink) query.getOnSet()).getModel(), is(ReduxModel.FILTER));
        assertThat(((ModelLink) query.getOnSet()).getWidgetId(), is("table"));

        BindLink bindLink = ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getDataProvider().getQueryMapping().get("name");
        assertThat(bindLink.getValue(), is("`name`"));
        assertThat(bindLink.getBindLink(), is("models.filter['table']"));
        assertThat(page.getClassName(), is("testClass"));
        assertThat(page.getStyle().size(), is(2));
    }
}
