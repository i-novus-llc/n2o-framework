package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест вызова операции
 */
public class InvokeActionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/invoke/form_param/test.object.xml"));
    }

    @Test
    public void testFormParam() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/invoke/form_param/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget firstForm = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        InputText firstValue = firstForm.fields().field("value").control(InputText.class);
        firstValue.val("1");
        FormWidget secondForm = page.regions().region(1, SimpleRegion.class).content().widget(0, FormWidget.class);
        InputText secondValue = secondForm.fields().field("value").control(InputText.class);
        InputText secondValue2 = secondForm.fields().field("value2").control(InputText.class);
        secondValue.val("2");
        secondValue2.val("3");

        StandardButton btn = page.toolbar().bottomRight().button("Сохранить");
        btn.click();
        page.alerts().alert(0).shouldHaveColor(Colors.SUCCESS);
        page.alerts().alert(0).shouldHaveText("form_value=1 second_form.value=2 second_form.value2=3");

    }
}
