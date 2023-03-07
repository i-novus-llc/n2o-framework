package net.n2oapp.framework.sandbox.autotest.examples;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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

@SpringBootTest(classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ButtonsVisibilityAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("/examples/buttons_visibility/index.page.xml"));
    }

    @Test
    public void buttonsVisibilityTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Скрытие кнопок");

        RadioGroup radioGroup = page.widget(FormWidget.class).fields().field("Пол")
                .control(RadioGroup.class);
        radioGroup.shouldExists();

        Button button = page.widget(FormWidget.class).toolbar().bottomLeft()
                .button("Указать девичью фамилию");

        radioGroup.shouldBeEmpty();
        button.element().shouldBe(Condition.hidden);

        radioGroup.check("Женский");
        radioGroup.shouldBeChecked("Женский");
        button.shouldExists();
        button.element().shouldBe(Condition.visible);

        radioGroup.check("Мужской");
        radioGroup.shouldBeChecked("Мужской");
        button.element().shouldBe(Condition.hidden);
    }

}
