package net.n2oapp.framework.sandbox.autotest.attributes;

import lombok.SneakyThrows;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(properties = {
        "n2o.engine.test.classpath=/access/attributes/fields/",
        "n2o.sandbox.project-id=access_attributes_fields"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FieldsAT extends SandboxAutotestBase {

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
        builder.packs(new AccessSchemaPack());
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
        super.configure(builder);
        builder.sources(
                new CompileInfo("META-INF/conf/default.access.xml"));
    }

    @Test
    public void testAdminAccess() {
        setUserInfo(loadUser());

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Доступ к полям по sec атрибутам");

        Fields fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(3);
        fields.field("Доступно всем").shouldExists();
        fields.field("Доступно всем").control(InputText.class).shouldBeEnabled();
        fields.field("Только с ролью admin").shouldExists();
        fields.field("Только с ролью admin").control(InputText.class).shouldBeEnabled();
        fields.field("Доступно всем").shouldExists();
        fields.field("Доступно всем").control(InputText.class).shouldBeEnabled();
        fields.field("Только анонимам").shouldNotExists();
    }

    @Test
    public void testAnonymousAccess() {
        setUserInfo(null);

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Доступ к полям по sec атрибутам");

        Fields fields = page.widget(FormWidget.class).fields();
        fields.field("Доступно всем").shouldExists();
        fields.field("Доступно всем").control(InputText.class).shouldBeEnabled();
        fields.field("Только с ролью admin").shouldNotExists();
        fields.field("Только с правом edit").shouldNotExists();
        fields.field("Только анонимам").shouldExists();
        fields.field("Только анонимам").control(InputText.class).shouldBeEnabled();
    }

    @SneakyThrows
    private Map<String, Object> loadUser() {
        Map<String, Object> user = new HashMap<>();
        user.put("username", "Admin");
        user.put("roles", Collections.singletonList("admin"));
        user.put("permissions", Collections.singletonList("edit"));
        return user;
    }

}
