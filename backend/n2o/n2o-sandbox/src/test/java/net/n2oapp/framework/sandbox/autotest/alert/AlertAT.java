package net.n2oapp.framework.sandbox.autotest.alert;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = {"n2o.engine.test.classpath=/cases/7.23/alerts_handle/"},
        classes = AlertATConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("cases/7.23/alerts_handle/alert.object.xml"),
                new CompileInfo("cases/7.23/alerts_handle/index.page.xml"));
    }

    @Test
    public void testStack() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Button send = page.widget(FormWidget.class).toolbar().bottomRight().button("Отправить уведомления");

        MultiFieldSet multiFieldSet = page.widget(FormWidget.class).fieldsets().fieldset(MultiFieldSet.class);
        multiFieldSet.clickAddButton();
        InputText text = multiFieldSet.item(0).fields().field("Текст сообщения").control(InputText.class);
        InputSelect position = multiFieldSet.item(0).fields().field("Позиция уведомления").control(InputSelect.class);

        text.click();
        text.setValue("Алерт 1");
        position.select(0);

        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 1");

        text.click();
        text.setValue("Алерт 2");
        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 2");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 1");

        text.click();
        text.setValue("Алерт 3");
        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 3");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 2");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 1");

        text.click();
        text.setValue("Алерт 4");
        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 3");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 2");

        multiFieldSet.clickAddButton();
        multiFieldSet.item(1).fields().field("Текст сообщения").control(InputText.class).click();
        multiFieldSet.item(1).fields().field("Текст сообщения").control(InputText.class).setValue("Алерт 1-2");
        multiFieldSet.item(1).fields().field("Позиция уведомления").control(InputSelect.class).select(1);

        multiFieldSet.clickAddButton();
        multiFieldSet.item(2).fields().field("Текст сообщения").control(InputText.class).click();
        multiFieldSet.item(2).fields().field("Текст сообщения").control(InputText.class).setValue("Алерт 1-3");
        multiFieldSet.item(2).fields().field("Позиция уведомления").control(InputSelect.class).select(2);

        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 3");
        page.alerts(Alert.Placement.bottom).alert(0).shouldHaveText("Алерт 1-2");
        page.alerts(Alert.Placement.topLeft).alert(0).shouldHaveText("Алерт 1-3");

        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.bottom).alert(0).shouldHaveText("Алерт 1-2");
        page.alerts(Alert.Placement.bottom).alert(1).shouldHaveText("Алерт 1-2");
        page.alerts(Alert.Placement.topLeft).alert(0).shouldHaveText("Алерт 1-3");
        page.alerts(Alert.Placement.topLeft).alert(1).shouldHaveText("Алерт 1-3");

        send.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 4");
        page.alerts(Alert.Placement.bottom).alert(0).shouldHaveText("Алерт 1-2");
        page.alerts(Alert.Placement.bottom).alert(1).shouldHaveText("Алерт 1-2");
        page.alerts(Alert.Placement.bottom).alert(2).shouldHaveText("Алерт 1-2");
        page.alerts(Alert.Placement.topLeft).alert(0).shouldHaveText("Алерт 1-3");
        page.alerts(Alert.Placement.topLeft).alert(1).shouldHaveText("Алерт 1-3");
        page.alerts(Alert.Placement.topLeft).alert(2).shouldHaveText("Алерт 1-3");
    }

}
