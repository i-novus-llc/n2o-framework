package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Html;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Автотест для компонента ввода html
 */
public class HtmlAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
    }

    @Test
    public void testHtml() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/html/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Html html = page.widget(FormWidget.class).fields().field(Html.class);
        html.shouldExists();

        html.shouldHaveElement("h3.class1");
        html.shouldHaveText("Hello, World!");

        Map<String, String> attributes = new HashMap<>();
        attributes.put("style", "color: red;");
        html.shouldHaveElementWithAttributes("h3.class1", attributes);
    }

    @Test
    public void testHtmlPlaceholder() {
        setJsonPath("net/n2oapp/framework/autotest/control/html/html_with_placeholder");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/html/html_with_placeholder/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/html/html_with_placeholder/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        Html html1 = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fields().field(Html.class);
        html1.shouldExists();
        html1.shouldHaveText("Mike");

        Html html2 = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class).fields().field(Html.class);
        html2.shouldExists();
        html2.shouldHaveText("-1- Mike");

        Html html3 = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class).fields().field(Html.class);
        html3.shouldExists();
        html3.shouldHaveText("Mike -2-");

        Html html4 = page.regions().region(0, SimpleRegion.class).content().widget(3, FormWidget.class).fields().field(Html.class);
        html4.shouldExists();
        html4.shouldHaveText("-3- Mike -3-");

        Html html5 = page.regions().region(0, SimpleRegion.class).content().widget(4, FormWidget.class).fields().field(Html.class);
        html5.shouldExists();
        html5.shouldHaveText("Mike");
        Map<String, String> attributes = new HashMap<>();
        attributes.put("style", "color: red;");
        html5.shouldHaveElementWithAttributes(".n2o-html-snippet > div", attributes);

        Html html6 = page.regions().region(0, SimpleRegion.class).content().widget(5, FormWidget.class).fields().field(Html.class);
        html6.shouldExists();
        html6.shouldHaveText("-5- Mike");

        Html html7 = page.regions().region(0, SimpleRegion.class).content().widget(6, FormWidget.class).fields().field(Html.class);
        html7.shouldExists();
        html7.shouldHaveText("Mike -6-");

        Html html8 = page.regions().region(0, SimpleRegion.class).content().widget(7, FormWidget.class).fields().field(Html.class);
        html8.shouldExists();
        html8.shouldHaveText("-7- Mike -7-");

        Html html9 = page.regions().region(0, SimpleRegion.class).content().widget(8, FormWidget.class).fields().field(Html.class);
        html9.shouldExists();
        html9.shouldHaveText("-8- Mike -8-");

        Html html10 = page.regions().region(0, SimpleRegion.class).content().widget(9, FormWidget.class).fields().field(Html.class);
        html10.shouldExists();
        html10.shouldHaveText("-9- Mike");

        Html html11 = page.regions().region(0, SimpleRegion.class).content().widget(10, FormWidget.class).fields().field(Html.class);
        html11.shouldExists();
        html11.shouldHaveText("-10- Mike");
    }
}
