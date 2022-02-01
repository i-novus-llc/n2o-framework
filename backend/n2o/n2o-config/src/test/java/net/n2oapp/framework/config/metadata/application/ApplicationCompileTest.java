package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.StompDatasource;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции приложения
 */
public class ApplicationCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        builder.getEnvironment().getContextProcessor().set("username", "test");
        builder.properties("n2o.homepage.id=index");
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oQueriesPack(), new N2oActionsPack());
    }

    @Test
    public void application() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/testPage.page.xml",
                "net/n2oapp/framework/config/metadata/application/simpleApplication.application.xml")
                .get(new ApplicationContext("simpleApplication"));
        assertThat(application.getLayout().getFixed(), is(true));
        assertThat(application.getLayout().getFullSizeHeader(), is(false));
        assertThat(application.getHeader(), notNullValue());
        PageContext pageContext = (PageContext) route("/", Page.class);
        assertThat(pageContext.getSourceId(null), is("testPage"));
    }

    @Test
    public void footer() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/footer.application.xml")
                .get(new ApplicationContext("footer"));
        assertThat(application.getFooter().getSrc(), is("Footer"));
        assertThat(application.getFooter().getTextRight(), is("RightText"));
        assertThat(application.getFooter().getTextLeft(), is("LeftText"));
        assertThat(application.getFooter().getClassName(), is("footer-class"));
        assertThat(application.getFooter().getStyle().get("backgroundColor"), is("primary"));
    }

    @Test
    public void datasources() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/datasources.application.xml")
                .get(new ApplicationContext("datasources"));

        assertThat(application.getDatasources().size(), is(3));
        AbstractDatasource datasource = application.getDatasources().get("ds1");
        assertThat(datasource, notNullValue());

        datasource = application.getDatasources().get("taskCount");
        assertThat(((StompDatasource) datasource).getProvider().getType(), is("stomp"));
        assertThat(((StompDatasource) datasource).getProvider().getDestination(), is("/task/count"));
        assertThat(((StompDatasource) datasource).getValues(), nullValue());

        datasource = application.getDatasources().get("notifCount");
        assertThat(((StompDatasource) datasource).getProvider().getType(), is("stomp"));
        assertThat(((StompDatasource) datasource).getProvider().getDestination(), is("/notif/count"));
        assertThat(((StompDatasource) datasource).getValues(), notNullValue());
        assertThat(((StompDatasource) datasource).getValues().get(0), is(Map.of("count", 0)));
        assertThat(((StompDatasource) datasource).getValues().get(1), is(Map.of("notifCount", "99+")));
    }

}
