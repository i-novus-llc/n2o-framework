package net.n2oapp.framework.autotest.run;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Configuration.headless;

/**
 * Базовый класс для автотестов
 */
@SpringBootTest(classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AutoTestBase extends N2oTestBase {
    @LocalServerPort
    protected int port;

    private ApplicationContext context;

    public static void configureSelenide() {
        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");
        headless = false;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context.getBean(N2oController.class).setBuilder(builder);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    protected <T extends Page> T open(Class<T> clazz) {
        return N2oSelenide.open(getBaseUrl(), clazz);
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
