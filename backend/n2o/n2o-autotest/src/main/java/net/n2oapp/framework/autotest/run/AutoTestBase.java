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
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.properties.OverrideProperties;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Configuration.browserCapabilities;
import static com.codeborne.selenide.Configuration.headless;
import static net.n2oapp.framework.autotest.run.AutoTestUtil.checkChromeDriver;
import static net.n2oapp.properties.reader.PropertiesReader.getPropertiesFromURI;
import static org.openqa.selenium.remote.CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Базовый класс для автотестов
 */
@SpringBootTest(
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutoTestBase extends N2oTestBase {

    @LocalServerPort
    protected int port;
    protected Logs logs;
    @Autowired
    private TestDataProviderEngine provider;
    private N2oController n2oController;

    public static void configureSelenide() {
        checkChromeDriver();
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");
        System.setProperty("selenide.timeout", "20000");
        headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "true"));

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences loggingPreferences = new LoggingPreferences();

        loggingPreferences.enable(LogType.BROWSER, Level.ALL);

        capabilities.setCapability("goog:loggingPrefs", loggingPreferences);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability(UNHANDLED_PROMPT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);

        browserCapabilities = capabilities;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        n2oController.setUp(builder);
        resolveIndividualProperties();
    }

    @Autowired
    public void setN2oController(N2oController n2oController) {
        this.n2oController = n2oController;
    }

    @AfterEach
    void outputBrowserLog() {
        if (Objects.isNull(logs))
            return;

        for (LogEntry log : logs.get(LogType.BROWSER))
            if (log.getLevel() == Level.SEVERE)
                System.out.println("BROWSER LOG:" + " " + log.getLevel() + " " + log.getMessage());
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

    protected void setResourcePath(String classpath) {
        provider.setClasspathResourcePath(classpath);
    }

    protected void resolveIndividualProperties(){
        String resourcePath = provider.getClasspathResourcePath();
        OverrideProperties overriddenProperties = getPropertiesFromURI((resourcePath.endsWith("/") ? resourcePath : resourcePath + "/") + "application.properties");
        if (!isEmpty(overriddenProperties))
            overriddenProperties.forEach((k, v) -> ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty(((String) k), v));
    }
}
