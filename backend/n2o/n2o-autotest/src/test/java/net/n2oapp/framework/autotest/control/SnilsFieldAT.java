package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.SnilsField;
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
        builder.packs(new N2oApplicationPack(), new N2oAllPack());
    }

    @Test
    void test() {
        setResourcePath("net/n2oapp/framework/autotest/control/snils");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/snils/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/snils/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/snils/test.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        StandardButton saveButton = page.toolbar().bottomLeft().button("Сохранить");
        saveButton.shouldBeEnabled();

        SnilsField snilsField = page.widget(FormWidget.class).fields().field("snils").control(SnilsField.class);
        snilsField.shouldBeVisible();
        snilsField.shouldHaveValue("424-225-277 48");
        snilsField.setValue("");
        saveButton.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Данные сохранены");
        snilsField.shouldHaveInvalidText(Condition.empty);

        snilsField.setValue("123");
        saveButton.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldNotExists();
        snilsField.shouldHaveInvalidText(Condition.text("Невалидный формат данных СНИЛС"));
    }
}