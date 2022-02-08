package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Configuration.headless;

public class AlertActionAT extends AutoTestBase {

    private StandardWidget.WidgetToolbar toolbar;
    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        headless = false;

        page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Всплывающие уведомления");
        toolbar = page.widget(FormWidget.class).toolbar();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/alert/test.page.xml"));
    }

    @Test
    public void testSimpleAlert() {
        toolbar.topLeft().button("Простое уведомление").click();

        Alert alert = page.alerts().alert(0);
        alert.shouldExists();
        alert.shouldHaveTitle("Простое уведомление");
        alert.shouldHaveText("Привет, мир!");
        alert.shouldHaveColor(Colors.LIGHT);
        alert.shouldHavePlacement(Alert.Placement.topLeft);
        alert.shouldHaveTimestamp("Только что");
        alert.shouldHaveCloseButton();
    }

    @Test
    public void testAlertTimeOut() throws InterruptedException {
        toolbar.topLeft().button("Уведомление с таймаутом").click();

        Alert alert = page.alerts().alert(0);
        alert.shouldHaveTitle("Уведомление с таймаутом");
        alert.shouldHaveText("Это сообщение пропадет через 2 секунды");
        alert.shouldHaveColor(Colors.WARNING);
        alert.shouldHavePlacement(Alert.Placement.topRight);
        alert.shouldHaveTimestamp("Только что");
        alert.shouldHaveCloseButton();
        alert.shouldExists();
        Thread.sleep(2000);
        alert.shouldNotExists();
    }

    @Test
    public void testAlertUrl() {
        toolbar.topLeft().button("Кликабельное уведомление").click();

        Alert alert = page.alerts().alert(0);
        alert.shouldExists();
        alert.shouldHaveTitle("Кликабельное уведомление");
        alert.shouldHaveText("Нажмите на сообщение для перехода по ссылке");
        alert.shouldHaveColor(Colors.SUCCESS);
        alert.shouldHavePlacement(Alert.Placement.bottomRight);
        alert.shouldHaveTimestamp("Только что");
        alert.shouldHaveCloseButton();
    }
}
