package net.n2oapp.framework.tutorial.helloworld;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.framework.tutorial.helloworld.model.HelloPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static com.codeborne.selenide.Configuration.browserSize;
import static com.codeborne.selenide.Configuration.headless;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldAT {
    @LocalServerPort
    private int port;

    private HelloPage helloPage;

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
        helloPage = Selenide.open("http://localhost:" + port, HelloPage.class);
    }

    @Test
    public void checkHelloText() {
        helloPage.helloShouldHaveText("Привет Мир!");
    }
}
