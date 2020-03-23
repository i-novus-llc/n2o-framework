package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Html;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Автотест для компонента ввода html
 */
public class HtmlAT extends AutoTestBase {

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testHtml() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/html/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Html html = page.single().widget(FormWidget.class).fields().field("Html")
                .control(Html.class);
        html.shouldExists();

        Map<String, String> attributes = new HashMap<>();
        attributes.put("style", "margin: 10px;");
        html.shouldHaveElementWithAttributes("h3.class1", attributes);
    }
}
