package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.ColorsEnum;
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
class AlertActionAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oApplicationPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oActionsPack()
        );
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/alert/test.page.xml")
        );
    }

    @Test
    void testAlertAction() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        page.breadcrumb().crumb(0).shouldHaveLabel("Всплывающие уведомления");
        StandardWidget.WidgetToolbar toolbar = page.widget(FormWidget.class).toolbar();

        toolbar.topLeft().button("Тестирование текста и заголовка").click();
        Alert alert = page.alerts(Alert.PlacementEnum.topLeft).alert(0);
        alert.shouldExists();
        alert.shouldHaveTitle("Простое уведомление");
        alert.shouldHaveText("Привет, мир!");
        alert.shouldHaveColor(ColorsEnum.SECONDARY);
        alert.closeButton().shouldExists();

        toolbar.topLeft().button("Тестирование таймаута").click();
        alert = page.alerts(Alert.PlacementEnum.topRight).alert(0);
        alert.shouldHaveColor(ColorsEnum.INFO);
        alert.closeButton().shouldNotExists();
        alert.shouldNotExists();

        toolbar.topLeft().button("Тестирование кнопки закрыть").click();
        alert = page.alerts(Alert.PlacementEnum.bottomRight).alert(0);
        alert.shouldHaveColor(ColorsEnum.LIGHT);
        alert.closeButton().shouldExists();
        alert.closeButton().click();
        alert.shouldNotExists();

        toolbar.topLeft().button("Тестирование ссылки").click();
        alert = page.alerts(Alert.PlacementEnum.bottomLeft).alert(0);
        alert.shouldExists();
        alert.shouldHaveColor(ColorsEnum.WARNING);
        alert.closeButton().shouldExists();
        alert.shouldHaveUrl(getBaseUrl() + "/#/test");
        alert.shouldHaveText("Привет, мир!");
    }
}
