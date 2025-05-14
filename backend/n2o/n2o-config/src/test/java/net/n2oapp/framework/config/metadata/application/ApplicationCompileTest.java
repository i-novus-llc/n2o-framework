package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.application.StompDatasource;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.meta.event.Event;
import net.n2oapp.framework.api.metadata.meta.event.StompEvent;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.PropertyResolver;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции приложения
 */
class ApplicationCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        builder.getEnvironment().getContextProcessor().set("username", "test");
        builder.properties("n2o.homepage.id=index", "url=http://example.org/?userId=123", "empty=");
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(),
                new N2oQueriesPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/application/person.query.xml"));
    }

    @Test
    void application() {
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
    void properties() {
        PropertyResolver systemProperties = builder.getEnvironment().getSystemProperties();
        assertThat(systemProperties.getProperty("n2o.homepage.id"), is("index"));
        assertThat(systemProperties.getProperty("url"), is("http://example.org/?userId=123"));
        assertThat(systemProperties.getProperty("empty"), is(StringUtils.EMPTY));
    }

    @Test
    void footer() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/footer.application.xml")
                .get(new ApplicationContext("footer"));
        assertThat(application.getFooter().getSrc(), is("Footer"));
        assertThat(application.getFooter().getTextRight(), is("`'RightText\\n'+name`"));
        assertThat(application.getFooter().getTextLeft(), is("`'LeftText\\n'+name`"));
        assertThat(application.getFooter().getClassName(), is("footer-class"));
        assertThat(application.getFooter().getStyle().get("backgroundColor"), is("primary"));
        assertThat(application.getFooter().getProperties().get("attr"), is("extAttr"));
    }

    @Test
    void stompDatasources() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/stompDatasources.application.xml")
                .get(new ApplicationContext("stompDatasources"));

        assertThat(application.getDatasources().size(), is(2));

        StompDatasource datasource = (StompDatasource) application.getDatasources().get("taskCount");
        assertThat(datasource.getProvider().getType(), is("stomp"));
        assertThat(datasource.getProvider().getDestination(), is("/task/count"));
        assertThat(datasource.getValues(), nullValue());

        datasource = (StompDatasource) application.getDatasources().get("notifCount");
        assertThat(datasource.getProvider().getType(), is("stomp"));
        assertThat(datasource.getProvider().getDestination(), is("/notif/count"));
        assertThat(datasource.getValues(), notNullValue());
        assertThat(datasource.getValues().get(0), is(Map.of("count", 0)));
        assertThat(datasource.getValues().get(1), is(Map.of("notifCount", "99+")));
    }

    @Test
    void events() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/events.application.xml")
                .get(new ApplicationContext("events"));

        assertThat(application.getEvents().size(), is(2));

        Event event = application.getEvents().get(0);
        assertThat(event.getId(), is("showNotif"));
        assertThat(((StompEvent) event).getDestination(), is("/notifications"));

        event = application.getEvents().get(1);
        assertThat(event.getId(), is("showTask"));
        assertThat(((StompEvent) event).getDestination(), is("/task"));
    }

    @Test
    void sidebarWithDatasource() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/sidebarDatasource.application.xml")
                .get(new ApplicationContext("sidebarDatasource"));
        assertThat(application.getWsPrefix(), nullValue());

        Sidebar sidebar = application.getSidebars().get(0);
        assertThat(sidebar.getSrc(), is("Sidebar"));
        assertThat(sidebar.getPath(), is("/person/:person_id/**"));
        assertThat(sidebar.getDatasource(), is("person"));

        MenuItem menuItem = sidebar.getMenu().getItems().get(0);
        assertThat(menuItem.getId(), is("mi1"));
        assertThat(menuItem.getTitle(), is("Документы"));
        assertThat(menuItem.getHref(), is("/person/:person_id/docs"));
        assertThat(menuItem.getLinkType(), is(MenuItem.LinkTypeEnum.inner));
        assertThat(menuItem.getSrc(), is("LinkMenuItem"));
        assertThat(menuItem.getPathMapping().get("person_id").getValue(), is(":person_id"));
        assertThat(menuItem.getQueryMapping().get("fio").getLink(), is("models.resolve['person']"));
        assertThat(menuItem.getQueryMapping().get("fio").getValue(), is("`fio`"));

        StandardDatasource datasource = (StandardDatasource) application.getDatasources().get("person");
        assertThat(datasource.getId(), is("person"));
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/person"));
        assertThat(datasource.getProvider().getQueryMapping().get("person_id").getValue(), is(":person_id"));
    }

    @Test
    void inlineSidebarDatasource() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/inlineSidebarDatasource.application.xml")
                .get(new ApplicationContext("inlineSidebarDatasource"));
        assertThat(application.getWsPrefix(), nullValue());

        StandardDatasource datasource = (StandardDatasource) application.getDatasources().get("person");
        assertThat(datasource.getId(), is("person"));
        assertThat(datasource.getProvider().getUrl(), is("n2o/data/person"));
        assertThat(datasource.getProvider().getQueryMapping().get("person_id").getValue(), is(":person_id"));

        datasource = (StandardDatasource) application.getDatasources().get("home");
        assertThat(datasource.getId(), is("home"));
    }

    @Test
    void datasourceWithDependencies() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/datasourceWithDependencies.application.xml")
                .get(new ApplicationContext("datasourceWithDependencies"));


        StandardDatasource ds1 = (StandardDatasource) application.getDatasources().get("ds1");
        assertThat(ds1.getId(), is("ds1"));

        StandardDatasource ds2 = (StandardDatasource) application.getDatasources().get("ds2");
        assertThat(ds2.getId(), is("ds2"));
        assertThat(ds2.getDependencies().get(0).getOn(), is("models.resolve['ds1'].out"));

        InheritedDatasource ds3 = (InheritedDatasource) application.getDatasources().get("ds3");
        assertThat(ds3.getId(), is("ds3"));
        assertThat(ds3.getProvider().getSourceDs(), is("ds1"));
        assertThat(ds3.getDependencies().get(0).getOn(), is("models.resolve['ds1']"));

    }

    @Test
    void datasourceSizeWithHeaderSidebar() {
        Application application = compile("net/n2oapp/framework/config/metadata/application/datasourceSizeWithHeaderSidebar.application.xml")
                .get(new ApplicationContext("datasourceSizeWithHeaderSidebar"));
        StandardDatasource ds = (StandardDatasource) application.getDatasources().get("ds1");
        assertThat(ds.getId(), is("ds1"));
        assertThat(ds.getProvider().getSize(), is(1));

        ds = (StandardDatasource) application.getDatasources().get("ds2");
        assertThat(ds.getId(), is("ds2"));
        assertThat(ds.getProvider().getSize(), is(1));
    }
}
