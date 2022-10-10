package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для компонента поля alert
 */
public class AlertFieldAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testSimpleAlert() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/alert/simple/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Alert alert = page.widget(FormWidget.class).fields().field(Alert.class);
        alert.shouldExists();

        alert.shouldHaveText("Read this message!");
        alert.shouldHaveTitle("Warning");
        alert.shouldHaveColor(Colors.WARNING);
        alert.shouldHaveUrl("http://example.org/");
        alert.closeButton().shouldExists();
        alert.closeButton().click();
        alert.shouldNotExists();
    }

    @Test
    public void testCloseButtonIndependence() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/alert/close_button_independence/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Тестирование независимости закрытия алертов по кнопке");

        Alert first = page.widget(FormWidget.class).fields().field(0, Alert.class);
        first.shouldExists();
        first.shouldHaveTitle("Заголовок первого алерта");
        first.shouldHaveText("Текст первого алерта");
        first.closeButton().shouldExists();

        Alert second = page.widget(FormWidget.class).fields().field(1, Alert.class);
        second.closeButton().shouldExists();
        second.shouldExists();
        second.shouldHaveTitle("Заголовок второго алерта");
        second.shouldHaveText("Текст второго алерта");

        first.closeButton().click();
        first.shouldExists();
        first.shouldHaveTitle("Заголовок второго алерта");
        first.shouldHaveText("Текст второго алерта");
    }
}
