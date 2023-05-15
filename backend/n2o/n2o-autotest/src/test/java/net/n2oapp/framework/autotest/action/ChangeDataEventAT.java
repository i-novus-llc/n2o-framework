package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
 * Автотест на события при изменении данных на странице
 */

public class ChangeDataEventAT extends AutoTestBase {

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
    }

    @Test
    public void test() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/change_data_event/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RadioGroup radioGroup1 = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("При изменении состояния поля будет показано success сообщение").control(RadioGroup.class);

        RadioGroup radioGroup2 = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .fields().field("При изменении состояния поля будет показано info сообщение").control(RadioGroup.class);

        InputText text = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .fields().field("test").control(InputText.class);

        radioGroup1.check("two");
        page.alerts(Alert.Placement.top).alert(0).shouldHaveColor(Colors.SUCCESS);
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Событие при изменение ds1");

        text.click();
        text.setValue("test");
        page.alerts(Alert.Placement.top).alert(0).shouldNotExists();
        radioGroup2.check("two");
        page.alerts(Alert.Placement.top).alert(0).shouldHaveColor(Colors.INFO);
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Событие при изменение ds2");
        text.shouldHaveValue("5");
    }

    @Test
    public void testIfElseAndShowModal() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/change_data_event/if_else_and_show_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/change_data_event/if_else_and_show_modal/modal.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText test = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("test").control(InputText.class);

        test.click();
        test.setValue("5");
        N2oSelenide.modal().shouldExists();
        N2oSelenide.modal().close();
        N2oSelenide.modal().shouldNotExists();
        test.click();
        test.setValue("3");
        test.shouldHaveValue("3");
        page.alerts(Alert.Placement.top).alert(0).shouldExists();
    }

    @Test
    public void testSwitchCase() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/change_data_event/switch_case/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RadioGroup radioGroup = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("target field").control(RadioGroup.class);

        radioGroup.check("check 2");
        page.alerts(Alert.Placement.top).alert(0).shouldExists();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("2");

        radioGroup.check("check 3");
        page.alerts(Alert.Placement.top).alert(0).shouldExists();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("3");

        radioGroup.check("check 1");
        page.alerts(Alert.Placement.top).alert(0).shouldExists();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("1");
    }
}
