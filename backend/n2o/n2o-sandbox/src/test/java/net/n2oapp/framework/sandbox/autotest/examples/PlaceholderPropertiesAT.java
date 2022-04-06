package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Text;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.engine.test.classpath=/examples/placeholder_properties/",
        "n2o.sandbox.project-id=examples_placeholder_properties"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/examples/placeholder_properties/application.properties")
@EnableCaching
public class PlaceholderPropertiesAT extends SandboxAutotestBase {

    @Value("${name}")
    private String name;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void PlaceholderPropertiesTest() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("name", name);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Placeholder properties");

        page.widget(FormWidget.class).fields().field(0, Text.class).shouldHaveText("Hello, Joe!");
    }

}
