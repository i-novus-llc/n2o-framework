package net.n2oapp.framework.sandbox.autotest.cases;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Автотест фильтрации списковых полей
 */
@SpringBootTest(properties = {"server.servlet.context-path=/", "n2o.sandbox.project-id=cases_7.0_case11"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case11AT extends SandboxAutotestBase {

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
        builder.transformers(new TestEngineQueryTransformer());
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"),
                new CompileInfo("META-INF/conf/test.object.xml"),
                new CompileInfo("META-INF/conf/test.query.xml"));
    }

    @Test
    public void testListFieldFiltration() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();

        InputText type = fields.field("type").control(InputText.class);
        type.shouldHaveValue("1");

        InputSelect filteredByLink = fields.field("Фильтр по type").control(InputSelect.class);
        InputSelect filteredByConstant = fields.field("Фильтр по константе").control(InputSelect.class);
        InputSelect filteredByLinkWithCache = fields.field("Фильтр по ссылке с кэшированием").control(InputSelect.class);
        RadioGroup radioGroup = fields.field("Фильтр по радио кнопкам").control(RadioGroup.class);
        CheckboxGroup checkboxGroup = fields.field("Фильтр по чекбоксам").control(CheckboxGroup.class);

        filteredByLink.shouldHaveOptions("test1", "test2");
        filteredByConstant.shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.shouldHaveOptions("test1", "test2");
        radioGroup.shouldHaveOptions("test1", "test2");
        checkboxGroup.shouldHaveOptions("test1", "test2");

        type.val("2");
        filteredByLink.shouldHaveOptions("test3", "test4");
        filteredByConstant.shouldHaveOptions("test1", "test2");
        filteredByLinkWithCache.shouldHaveOptions("test3", "test4");
        radioGroup.shouldHaveOptions("test3", "test4");
        checkboxGroup.shouldHaveOptions("test3", "test4");
    }
}