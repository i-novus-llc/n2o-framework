package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SearchablePage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.SearchablePageElementIOv2;
import net.n2oapp.framework.config.io.region.CustomRegionIOv1;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.region.CustomRegionCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.TableCompiler;
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
        builder.ios(new SearchablePageElementIOv2(), new CustomRegionIOv1(), new TableElementIOV4())
                .compilers(new SearchablePageCompiler(), new CustomRegionCompiler(), new TableCompiler());
    }

    @Test
    public void searchablePageWithoutButton() {
        SearchablePage page = (SearchablePage) compile("net/n2oapp/framework/config/metadata/compile/page/testSearchablePage.page.xml")
                .get(new PageContext("testSearchablePage"));

        assertThat(page.getSrc(), is("SearchablePage"));
        assertThat(page.getSearchBar().getClassName(), is("n2o-search-bar"));
        assertThat(page.getSearchBar().getPlaceholder(), is("Поиск по имени"));
        assertThat(page.getSearchBar().getTrigger(), is(SearchablePage.SearchBar.Trigger.CHANGE));
        assertThat(page.getSearchBar().getThrottleDelay(), is(1000));
        assertThat(page.getSearchModelPrefix(), is("filter"));
        assertThat(page.getSearchWidgetId(), is("table1"));
        assertThat(page.getSearchModelKey(), is("name"));

        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("name");
        assertThat(query.getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(query.getOnGet().getPayload().get("prefix"), is("filter"));
        assertThat(query.getOnGet().getPayload().get("key"), is("table1"));
        assertThat(query.getOnGet().getPayload().get("field"), is("name"));
        assertThat(query.getOnGet().getPayload().get("value"), is(":name"));
        assertThat(((ModelLink) query.getOnSet()).getModel(), is(ReduxModel.FILTER));
        assertThat(((ModelLink) query.getOnSet()).getWidgetId(), is("table1"));
        assertThat(((ModelLink) query.getOnSet()).getFieldId(), is("name"));
        assertThat(((ModelLink) query.getOnSet()).getBindLink(), is("models.filter['table1'].name"));
    }
}
