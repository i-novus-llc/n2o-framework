package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.exception.N2oException;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

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
    public void testCopyActionBackwardCompatibility() {
        PageContext pageContext = new PageContext("testCopyActionFromParent", "/p");
        pageContext.setParentDatasourceIdsMap(Map.of("ds", "p_ds", "ds2", "p_ds2", "ds3", "p_ds3"));
        compile("net/n2oapp/framework/config/metadata/compile/datasource/parent/testCopyActionFromParent.page.xml",
                "net/n2oapp/framework/config/metadata/compile/datasource/parent/testCopyActionFromParentModal.page.xml")
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

    @Test
    public void testParentDatasource() {
        PageContext pageContext = new PageContext("testParentDatasource", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/datasource/parent/testParentDatasource.page.xml")
                .get(pageContext);

        // route page
        StandardPage routePage = (StandardPage) routeAndGet("/p", Page.class);

        Map<String, AbstractDatasource> datasources = routePage.getDatasources();
        assertThat(datasources.size(), is(2));
        assertThat(datasources.get("p_ds1"), instanceOf(StandardDatasource.class));
        assertThat(datasources.get("p_ds2"), instanceOf(StandardDatasource.class));

        // modal page
        PageContext modalPageContext = (PageContext) route("/p/modal", Page.class);
        Map<String, String> parentDatasourceIdsMap = modalPageContext.getParentDatasourceIdsMap();
        assertThat(parentDatasourceIdsMap.size(), is(2));
        assertThat(parentDatasourceIdsMap.get("ds1"), is("p_ds1"));
        assertThat(parentDatasourceIdsMap.get("ds2"), is("p_ds2"));

        StandardPage modalPage = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/datasource/parent/testParentDatasourceModal.page.xml")
                .get(modalPageContext);

        datasources = modalPage.getDatasources();
        assertThat(datasources.size(), is(1));

        // modal2 page
        PageContext modal2PageContext = (PageContext) route("/p/modal/modal2", Page.class);
        parentDatasourceIdsMap = modal2PageContext.getParentDatasourceIdsMap();
        assertThat(parentDatasourceIdsMap.size(), is(2));
        assertThat(parentDatasourceIdsMap.get("ds1"), is("p_ds1"));
        assertThat(parentDatasourceIdsMap.get("ds3"), is("p_modal_ds3"));

        StandardPage modal2Page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/datasource/parent/testParentDatasourceModal2.page.xml")
                .get(modal2PageContext);

        datasources = modal2Page.getDatasources();
        assertThat(datasources.size(), is(1));
        assertThat(datasources.get("p_modal_modal2_ds3"), instanceOf(StandardDatasource.class));
    }

    @Test
    public void testExceptions() {
        PageContext context = new PageContext("testNonExistParentPage", "/p");
        try {
            compile("net/n2oapp/framework/config/metadata/compile/datasource/parent/testNonExistParentPage.page.xml")
                    .get(context);
            fail();
        } catch (N2oException e) {
            assertThat(e.getMessage(), is("На странице задан \"<parent-datasource>\", при этом она не имеет родительской страницы"));
        }

        context = new PageContext("testNonExistDatasourceInParentPage", "/p/modal");
        context.setParentDatasourceIdsMap(new HashMap<>());
        try {
            compile("net/n2oapp/framework/config/metadata/compile/datasource/parent/testNonExistDatasourceInParentPage.page.xml")
                    .get(context);
            fail();
        } catch (N2oException e) {
            assertThat(e.getMessage(), is("Элемент \"<parent-datasource>\" ссылается на несуществующий источник или \"<app-datasource>\" родительской страницы"));
        }
    }
}
