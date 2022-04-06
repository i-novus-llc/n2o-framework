package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.engine.test.classpath=/examples/placeholder_context/",
        "n2o.sandbox.project-id=examples_placeholder_context"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/examples/placeholder_context/user.properties")
public class PlaceholderContextAT extends SandboxAutotestBase {

    @Value("${username}")
    private String username;
    @Value("${email}")
    private String email;
    @Value("${roles}")
    private String roles;

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
        builder.getEnvironment().getContextProcessor().set("username", username);
        builder.getEnvironment().getContextProcessor().set("email", email);
        builder.getEnvironment().getContextProcessor().set("roles", roles);
    }

    @Test
    public void PlaceholderContextTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Placeholder context");

        OutputText email = page.widget(FormWidget.class).fields().field("email")
                .control(OutputText.class);
        email.shouldExists();
        email.shouldHaveValue(this.email);

        OutputText roles = page.widget(FormWidget.class).fields().field("roles")
                .control(OutputText.class);
        roles.shouldExists();
        roles.shouldHaveValue(this.roles);
    }

}
