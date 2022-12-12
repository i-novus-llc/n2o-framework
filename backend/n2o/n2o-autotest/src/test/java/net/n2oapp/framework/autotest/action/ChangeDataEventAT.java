package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.Colors;
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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/change_data_event/index.page.xml"));
    }

    @Test
    public void test() {
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

        text.val("test");
        page.alerts(Alert.Placement.top).alert(0).shouldNotExists();
        radioGroup2.check("two");
        page.alerts(Alert.Placement.top).alert(0).shouldHaveColor(Colors.INFO);
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Событие при изменение ds2");
        text.shouldHaveValue("5");
    }
}
