package net.n2oapp.framework.autotest.run;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.N2oTestBase;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Configuration.pageLoadTimeout;
import static com.codeborne.selenide.Configuration.timeout;
import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Configuration.browserCapabilities;

/**
 * Базовый класс для автотестов
 */
@SpringBootTest(classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutoTestBase extends N2oTestBase {
    @LocalServerPort
    protected int port;

    @Autowired
    private TestDataProviderEngine provider;

    private N2oController n2oController;
    protected Logs logs;

    public static void configureSelenide() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");
        headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "true"));
        timeout = Long.parseLong(System.getProperty("selenide.timeout", "9000"));
        pageLoadTimeout = Long.parseLong(System.getProperty("selenide.pageLoadTimeout", "9000"));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences loggingPreferences = new LoggingPreferences();

        loggingPreferences.enable(LogType.BROWSER, Level.ALL);

        capabilities.setCapability("goog:loggingPrefs", loggingPreferences);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        browserCapabilities = capabilities;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        n2oController.setUp(builder);
    }

    @AfterEach
    void outputBrowserLog() {
        if (Objects.isNull(logs))
            return;
        for (LogEntry log : logs.get(LogType.BROWSER)) {
            System.out.println("BROWSER LOG:" + " " + log.getLevel() + " " + log.getMessage());
        }
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.transformers(new TestEngineQueryTransformer());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"));
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    protected <T extends Page> T open(Class<T> clazz) {
        T open = N2oSelenide.open(getBaseUrl(), clazz);
        logs = WebDriverRunner.getWebDriver().manage().logs();
        return open;
    }

    protected <T extends Page> T open(Class<T> clazz, String pageUrl, Map<String, String> queryParams) {
        if (pageUrl == null) pageUrl = "/";
        if (queryParams != null && !queryParams.isEmpty()) {
            pageUrl += "?" + queryParams.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
        }
        return N2oSelenide.open(getBaseUrl() + "/#" + pageUrl, clazz);
    }

    protected void setUserInfo(Map<String, Object> user) {
        n2oController.addConfigProperty("user", user);
    }

    protected void setJsonPath(String classpath) {
        provider.setClasspathResourcePath(classpath);
    }

    @Autowired
    public void setN2oController(N2oController n2oController) {
        this.n2oController = n2oController;
    }
}
