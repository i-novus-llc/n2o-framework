package net.n2oapp.framework.autotest.alert;

import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование положения сообщения об ошибке/успехе
 */
public class AlertPositionAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        setJsonPath("net/n2oapp/framework/autotest/alert");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/modal2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/test.object.xml"));
    }

    @Test
    public void alertTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        Toolbar toolbar = form.toolbar().topLeft();

        // success
        toolbar.button("Успех").click();
        Alert alert = page.alerts(Alert.Placement.top).alert(0);
        alert.shouldHaveText("Данные сохранены");
        alert.shouldHaveColor(Colors.SUCCESS);

        // fail
        toolbar.button("Ошибка валидации").click();
        alert = page.alerts(Alert.Placement.bottom).alert(0);
        alert.shouldHaveText("Ошибка");
        alert.shouldHaveColor(Colors.DANGER);

        // in modal
        toolbar.button("Успех/Ошибка в модальном окне").click();
        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно");

        Toolbar modalToolbar = modal.content(SimplePage.class).widget(FormWidget.class).toolbar().topLeft();
        modalToolbar.button("Успех").click();
        Alert modalAlert = page.alerts(Alert.Placement.top).alert(0);
        modalAlert.shouldHaveText("Данные сохранены");
        modalAlert.shouldHaveColor(Colors.SUCCESS);

        modalToolbar.button("Ошибка валидации").click();
        modalAlert.shouldHaveText("Ошибка в модальном окне");
        modalAlert.shouldHaveColor(Colors.DANGER);
        modal.close();

        // fail with stacktrace
        toolbar.button("Ошибка со стектрейсом").click();
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно 2");
        modalAlert.shouldHaveColor(Colors.DANGER);
        modalAlert.shouldHaveStacktrace();
        modalAlert.shouldHaveText("Произошла внутренняя ошибка");
    }
}
