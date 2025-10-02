package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.UuidField;
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
 * Автотест для поля {@code <uuid>}
 */
public class UuidFieldAT extends AutoTestBase {

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
        setResourcePath("net/n2oapp/framework/autotest/control/uuid");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/uuid/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/uuid/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/uuid/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        StandardButton saveButton = page.toolbar().bottomLeft().button("Сохранить");
        saveButton.shouldBeEnabled();

        UuidField uuidField = page.widget(FormWidget.class).fields().field("uuid").control(UuidField.class);
        uuidField.shouldBeVisible();
        uuidField.shouldHaveValue("dfd11111-1111-1111-8111-111111111111");
        uuidField.setValue("");
        saveButton.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldHaveText("Данные сохранены");
        uuidField.shouldHaveInvalidText(Condition.empty);

        uuidField.setValue("123");
        saveButton.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldNotExists();
        uuidField.shouldHaveInvalidText(Condition.text("Невалидный формат UUID"));
    }
}