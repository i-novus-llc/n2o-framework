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
        assertThat(page.getSearchBar().getDatasource(), is("testSearchablePage_table"));
        assertThat(page.getSearchBar().getFieldId(), is("name"));

        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("table_name");
        assertThat(query.getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getPrefix(), is("filter"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getKey(), is("testSearchablePage_table"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getField(), is("name"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getValue(), is(":table_name"));
        assertThat(query.getOnSet().normalizeLink(), is("models.filter['testSearchablePage_table'].name"));
        assertThat(((ModelLink) query.getOnSet()).getModel(), is(ReduxModel.FILTER));
        assertThat(((ModelLink) query.getOnSet()).getDatasource(), is("testSearchablePage_table"));

        BindLink bindLink = page.getDatasources().get("testSearchablePage_table").getProvider().getQueryMapping().get("table_name");
        assertThat(bindLink.normalizeLink(), is("models.filter['testSearchablePage_table'].name"));
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
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getKey(), is("testSearchablePage2_table"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getField(), is("name"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getValue(), is(":name"));
        assertThat(query.getOnSet().normalizeLink(), is("models.filter['testSearchablePage2_table'].name"));
        assertThat(((ModelLink) query.getOnSet()).getModel(), is(ReduxModel.FILTER));
        assertThat(((ModelLink) query.getOnSet()).getDatasource(), is("testSearchablePage2_table"));

        BindLink bindLink = page.getDatasources().get("testSearchablePage2_table").getProvider().getQueryMapping().get("name");
        assertThat(bindLink.normalizeLink(), is("models.filter['testSearchablePage2_table'].name"));
        assertThat(page.getClassName(), is("testClass"));
        assertThat(page.getStyle().size(), is(2));
    }
}
