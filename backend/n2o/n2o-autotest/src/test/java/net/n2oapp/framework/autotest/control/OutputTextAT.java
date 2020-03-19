package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oOutputText;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест поля вывода текста
 */
public class OutputTextAT extends AutoTestBase {

    private N2oSimplePage page;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/output_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(N2oSimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testOutputText() {
        N2oOutputText output = page.single().widget(FormWidget.class).fields().field("Output1")
                .control(N2oOutputText.class);
        output.shouldExists();

        output.shouldHaveValue("123,46");
    }

    @Test
    public void testOutputTextWithIcon() {
        N2oOutputText output = page.single().widget(FormWidget.class).fields().field("Output2")
                .control(N2oOutputText.class);
        output.shouldExists();

        output.shouldHaveValue("test");
        output.shouldHaveIcon("fa fa-plus");
    }

    @Test
    public void testIconWithoutText() {
        N2oOutputText output = page.single().widget(FormWidget.class).fields().field("Output3")
                .control(N2oOutputText.class);
        output.shouldExists();

        output.shouldBeEmpty();
        output.shouldHaveIcon("fa fa-plus");
    }
}
