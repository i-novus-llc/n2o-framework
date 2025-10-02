package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.PhoneField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
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
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
    }

    @Test
    void test() {
        setResourcePath("net/n2oapp/framework/autotest/control/phone");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/phone/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/phone/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/phone/test.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        StandardButton saveButton = page.toolbar().bottomLeft().button("Сохранить");
        saveButton.shouldBeEnabled();

        PhoneField phoneField = page.widget(FormWidget.class).fields().field("phone").control(PhoneField.class);
        phoneField.shouldBeVisible();
        phoneField.shouldHaveValue("+7 (999) 123-45-67");
        phoneField.setValue("+7 (999) 123-45-00");
        saveButton.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Данные сохранены");
        phoneField.shouldHaveInvalidText(Condition.empty);

        phoneField.setValue("123");
        saveButton.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldNotExists();
        phoneField.shouldHaveInvalidText(Condition.text("Невалидный номер телефона"));
    }
}
