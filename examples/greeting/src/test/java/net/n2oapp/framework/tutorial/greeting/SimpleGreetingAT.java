package net.n2oapp.framework.tutorial.greeting;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.codeborne.selenide.Configuration.browserSize;
import static com.codeborne.selenide.Configuration.headless;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleGreetingAT {
    @LocalServerPort
    private int port;

    private SimplePage page;

    /**
     * Задание настроек для Selenide
     */
    @BeforeAll
    public static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");
        headless = true;
        browserSize = "1920x1200";
    }

    /**
     * Открытие страницы
     */
    @BeforeEach
    public void openProtoPage() {
        page = N2oSelenide.open("http://localhost:" + port, SimplePage.class);
    }

    @Test
    public void greetingTest() {
        InputText inputText = page.widget(FormWidget.class).fields().field("Имя").control(InputText.class);
        inputText.val("Иван");
        page.widget(FormWidget.class).toolbar().bottomLeft().button("Отправить").click();
        page.alerts().alert(0).shouldHaveText("Привет, Иван");
    }
}
