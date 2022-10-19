package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование источника данных родительской страницы
 */
public class ParentDatasourceTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    public void testParentDatasource() {
        PageContext pageContext = new PageContext("testParentDatasource", "/p");
        pageContext.setParentDatasourceIds(List.of("ds", "ds2", "ds3"));
        compile("net/n2oapp/framework/config/metadata/compile/datasource/testParentDatasource.page.xml",
                "net/n2oapp/framework/config/metadata/compile/datasource/testParentDatasourceModal.page.xml")
                .get(pageContext);

        StandardPage page = (StandardPage) routeAndGet("/p/modal1", Page.class);
        CopyAction submit = (CopyAction) page.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getSource().getKey(), is("p_modal1_ds2"));
        assertThat(submit.getPayload().getTarget().getKey(), is("p_ds3"));

        Map<String, AbstractDatasource> datasources = page.getDatasources();
        assertThat(datasources.size(), is(2));
        assertThat(datasources.get("p_modal1_w1"), instanceOf(StandardDatasource.class));
        assertThat(datasources.get("p_modal1_ds2"), instanceOf(BrowserStorageDatasource.class));


        page = (StandardPage) routeAndGet("/p/modal2", Page.class);
        submit = (CopyAction) page.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getSource().getKey(), is("p_ds3"));
        assertThat(submit.getPayload().getTarget().getKey(), is("p_ds2"));
    }
}
