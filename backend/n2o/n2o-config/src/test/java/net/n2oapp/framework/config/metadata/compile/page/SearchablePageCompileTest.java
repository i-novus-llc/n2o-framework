package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SearchablePage;
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
    public void testSearchablePage() {
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
        assertThat(query.getOnGet().getPayload().get("prefix"), is("filter"));
        assertThat(query.getOnGet().getPayload().get("key"), is("table"));
        assertThat(query.getOnGet().getPayload().get("field"), is("name"));
        assertThat(query.getOnGet().getPayload().get("value"), is(":table_name"));
        assertThat(query.getOnSet().getValue(), is("`name`"));
        assertThat(query.getOnSet().getBindLink(), is("models.filter['table']"));
        assertThat(((ModelLink) query.getOnSet()).getModel(), is(ReduxModel.FILTER));
        assertThat(((ModelLink) query.getOnSet()).getWidgetId(), is("table"));

        BindLink bindLink = page.getWidgets().get("testSearchablePage_table").getDataProvider().getQueryMapping().get("name");
        assertThat(bindLink.getValue(), is("`name`"));
        assertThat(bindLink.getBindLink(), is("models.filter['table']"));
    }
}
