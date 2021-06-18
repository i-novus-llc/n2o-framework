package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест компонента вывода однострочного текста
 */
public class OutputTextAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/output_text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testOutputText() {
        OutputText output = page.widget(FormWidget.class).fields().field("Output1")
                .control(OutputText.class);
        output.shouldExists();

        output.shouldHaveValue("123,46");
    }

    @Test
    public void testOutputTextWithIcon() {
        OutputText output = page.widget(FormWidget.class).fields().field("Output2")
                .control(OutputText.class);
        output.shouldExists();

        output.shouldHaveValue("test");
        output.shouldHaveIcon("fa fa-plus");
    }

    @Test
    public void testIconWithoutText() {
        OutputText output = page.widget(FormWidget.class).fields().field("Output3")
                .control(OutputText.class);
        output.shouldExists();

        output.shouldBeEmpty();
        output.shouldHaveIcon("fa fa-plus");
    }

    @Test
    public void testDefaultValue() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/output_text/default/index.page.xml"));
        page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();

        OutputText output1 = fields.field("moment").control(OutputText.class);
        output1.shouldHaveValue("06.02.2019");
        OutputText output2 = fields.field("numeral").control(OutputText.class);
        output2.shouldHaveValue("1,50");
        OutputText output3 = fields.field("lodash").control(OutputText.class);
        output3.shouldHaveValue("a~b~c");
    }
}
