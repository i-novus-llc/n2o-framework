package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.PhoneField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для поля {@code <phone>}
 */
public class PhoneFieldAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    void test() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/phone/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        PhoneField phoneField = page.widget(FormWidget.class).fields().field("phone").control(PhoneField.class);
        phoneField.shouldBeVisible();
        phoneField.shouldHaveValue("+7 (999) 123-45-67");
        phoneField.shouldHaveInvalidText(Condition.empty);
        phoneField.setValue("123");
        page.widget(FormWidget.class).fields().field("input").control(InputText.class).click();
        phoneField.shouldHaveInvalidText(Condition.text("Невалидный номер телефона"));
    }
}
