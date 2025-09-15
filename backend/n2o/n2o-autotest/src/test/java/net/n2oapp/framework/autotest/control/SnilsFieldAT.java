package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.SnilsField;
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
 * Автотест для поля {@code <snils>}
 */
public class SnilsFieldAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/snils/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        SnilsField snilsField = page.widget(FormWidget.class).fields().field("snils").control(SnilsField.class);
        snilsField.shouldBeVisible();
        snilsField.shouldHaveValue("424-225-277 48");
        snilsField.shouldHaveInvalidText(Condition.empty);
        snilsField.setValue("123");
        page.widget(FormWidget.class).fields().field("input").control(InputText.class).click();
        snilsField.shouldHaveInvalidText(Condition.text("Невалидный формат данных СНИЛС"));
    }
}