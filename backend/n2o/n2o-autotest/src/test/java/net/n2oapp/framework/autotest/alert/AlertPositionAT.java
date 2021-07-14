package net.n2oapp.framework.autotest.alert;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
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
 * Тестирование положения сообщения об ошибке/успехе
 */
public class AlertPositionAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/modal2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void alertTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        Toolbar toolbar = form.toolbar().topLeft();

        // success
        toolbar.button("Успех").click();
        Alerts.Alert alert = page.alerts().alert(0);
        alert.shouldHaveText("Данные сохранены");
        alert.shouldHaveColor(Colors.SUCCESS);
        alert.shouldHavePosition(Alerts.Alert.Position.RELATIVE);

        // fail
        toolbar.button("Ошибка валидации").click();
        alert.shouldHaveText("Ошибка");
        alert.shouldHaveColor(Colors.DANGER);
        alert.shouldHavePosition(Alerts.Alert.Position.FIXED);
        alert.shouldHavePlacement(Alerts.Alert.Placement.BOTTOM);

        // in modal
        toolbar.button("Успех/Ошибка в модальном окне").click();
        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно");

        Toolbar modalToolbar = modal.content(SimplePage.class).widget(FormWidget.class).toolbar().topLeft();
        modalToolbar.button("Успех").click();
        Alerts.Alert modalAlert = modal.content(SimplePage.class).alerts().alert(0);
        modalAlert.shouldHaveText("Данные сохранены");
        modalAlert.shouldHaveColor(Colors.SUCCESS);
        modalAlert.shouldHavePosition(Alerts.Alert.Position.FIXED);
        modalAlert.shouldHavePlacement(Alerts.Alert.Placement.TOP);

        modalToolbar.button("Ошибка валидации").click();
        modalAlert.shouldHaveText("Ошибка в модальном окне");
        modalAlert.shouldHaveColor(Colors.DANGER);
        modalAlert.shouldHavePosition(Alerts.Alert.Position.FIXED);
        modalAlert.shouldHavePlacement(Alerts.Alert.Placement.TOP);

        // alert in parent page don't disappear after close modal
        modal.close();
        alert = page.alerts().alert(0);
        alert.shouldExists();
        alert.shouldHaveText("Ошибка");

        // fail with stacktrace
        toolbar.button("Ошибка со стектрейсом").click();
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно 2");
        modalAlert.shouldHaveColor(Colors.DANGER);
        modalAlert.shouldHaveStacktrace();
        modalAlert.shouldHaveText("Произошла внутренняя ошибка");
        modalAlert.shouldHavePosition(Alerts.Alert.Position.FIXED);
        modalAlert.shouldHavePlacement(Alerts.Alert.Placement.TOP);
    }
}
