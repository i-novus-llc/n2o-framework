package net.n2oapp.framework.config.metadata.application;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oQueriesPack());
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

}
