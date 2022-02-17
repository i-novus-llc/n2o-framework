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

/**
 * Автотест для действия уведомления
 */
public class AlertActionAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/alert/test.page.xml"));
    }

    @Test
    public void testAlertAction() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.breadcrumb().titleShouldHaveText("Всплывающие уведомления");
        StandardWidget.WidgetToolbar toolbar = page.widget(FormWidget.class).toolbar();

        toolbar.topLeft().button("Тестирование текста и заголовка").click();
        Alert alert = page.alerts().alert(0);
        alert.shouldExists();
        alert.shouldHaveTitle("Простое уведомление");
        alert.shouldHaveText("Привет, мир!");
        alert.shouldHaveColor(Colors.SECONDARY);
        alert.shouldHavePlacement(Alert.Placement.topLeft);
        alert.closeButton().shouldExists();

        toolbar.topLeft().button("Тестирование таймаута").click();
        alert = page.alerts().alert(0);
        alert.shouldHaveColor(Colors.INFO);
        alert.shouldHavePlacement(Alert.Placement.topRight);
        alert.closeButton().shouldNotExists();
        alert.shouldNotExists();

        toolbar.topLeft().button("Тестирование кнопки закрыть").click();
        alert = page.alerts().alert(0);
        alert.shouldHaveColor(Colors.LIGHT);
        alert.shouldHavePlacement(Alert.Placement.bottomRight);
        alert.closeButton().shouldExists();
        alert.closeButton().click();
        alert.shouldNotExists();

        toolbar.topLeft().button("Тестирование ссылки").click();
        alert = page.alerts().alert(0);
        alert.shouldExists();
        alert.shouldHaveColor(Colors.WARNING);
        alert.shouldHavePlacement(Alert.Placement.bottomLeft);
        alert.closeButton().shouldExists();
        alert.shouldHaveUrl(getBaseUrl() + "/#/test");
        alert.shouldHaveText("Привет, мир!");
        alert.click();
        page.shouldExists();
        page.breadcrumb().titleByIndexShouldHaveText("Тест", 1);
    }
}
