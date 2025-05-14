package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Автотест проверяет работу on-fail
 */
class OnFailActionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void testOnFail() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/on_fail/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/on_fail/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        InputText inputText = form.fields().field("Введите имя").control(InputText.class);
        inputText.setValue("Петя");
        StandardButton saveBtn = form.toolbar().bottomLeft().button("Сохранить");
        saveBtn.click();

        Alert alert = page.alerts(Alert.PlacementEnum.top).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("Действие завершилось ошибкой");
        alert.shouldHaveColor(ColorsEnum.DANGER);
    }
}
