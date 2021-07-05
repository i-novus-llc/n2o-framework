package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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
 * Автотест компонента радиокнопок
 */
public class RadioGroupAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testRadioGroup() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/radio_group/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup radio = fields.field("Radio1").control(RadioGroup.class);
        radio.shouldExists();
        radio.shouldHaveType(RadioGroup.RadioType.DEFAULT);
        radio.shouldHaveOptions("One", "Two", "Three");
        radio.shouldBeEmpty();
        radio.check("Three");
        radio.shouldBeChecked("Three");
        radio.check("Two");
        radio.shouldBeChecked("Two");

        RadioGroup btnRadio = fields.field("BtnRadio").control(RadioGroup.class);
        btnRadio.shouldExists();
        btnRadio.shouldHaveType(RadioGroup.RadioType.BTN);
        btnRadio.shouldHaveOptions("One", "Two", "Three");
        btnRadio.check("Three");
        btnRadio.shouldBeChecked("Three");
        btnRadio.check("Two");
        btnRadio.shouldBeChecked("Two");

        RadioGroup tabsRadio = fields.field("TabsRadio").control(RadioGroup.class);
        tabsRadio.shouldExists();
        tabsRadio.shouldHaveType(RadioGroup.RadioType.TABS);
        tabsRadio.shouldHaveOptions("One", "Two", "Three");
        tabsRadio.check("Three");
        tabsRadio.shouldBeChecked("Three");
        tabsRadio.check("Two");
        tabsRadio.shouldBeChecked("Two");
    }
}
