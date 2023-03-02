package net.n2oapp.framework.sandbox.autotest.cases;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"server.servlet.context-path=/"},
        classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Case4AT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"),
                new CompileInfo("cases/7.0/case4/index.page.xml"));
    }

    @Test
    public void hideButtonTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Скрытие кнопки dropdown при скрытии последнего подменю");

        InputText input = page.widget(FormWidget.class).fields().field("Введите 'test'").control(InputText.class);
        input.shouldBeEmpty();

        Toolbar toolbar = page.widget(FormWidget.class).toolbar().topLeft();
        toolbar.shouldHaveSize(1);
        toolbar.dropdown().shouldExists();
        toolbar.dropdown().element().shouldNotBe(Condition.visible);

        input.val("test");
        toolbar.dropdown().element().shouldBe(Condition.visible);
        toolbar.dropdown().click();
        toolbar.dropdown().shouldHaveItems(1);
        toolbar.dropdown().menuItem("Внутреннее меню").shouldExists();

        input.clear();
        toolbar.dropdown().element().shouldNotBe(Condition.visible);
    }

}
