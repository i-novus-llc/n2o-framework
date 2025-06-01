package net.n2oapp.framework.autotest.alert;

import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.ColorsEnum;
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
class AlertPositionAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        setResourcePath("net/n2oapp/framework/autotest/alert");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/alert/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/simple/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/simple/modal2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/simple/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/simple/test.object.xml"));
    }

    @Test
    void alertTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        Toolbar toolbar = form.toolbar().topLeft();

        // success
        toolbar.button("Успех").click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText("Данные сохранены");
        alert.shouldHaveColor(ColorsEnum.SUCCESS);

        // fail
        toolbar.button("Ошибка валидации").click();
        alert = page.alerts(Alert.PlacementEnum.BOTTOM).alert(0);
        alert.shouldHaveText("Ошибка");
        alert.shouldHaveColor(ColorsEnum.DANGER);

        // in modal
        toolbar.button("Успех/Ошибка в модальном окне").click();
        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно");

        Toolbar modalToolbar = modal.content(SimplePage.class).widget(FormWidget.class).toolbar().topLeft();
        modalToolbar.button("Успех").click();
        Alert modalAlert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        modalAlert.shouldHaveText("Данные сохранены");
        modalAlert.shouldHaveColor(ColorsEnum.SUCCESS);

        modalToolbar.button("Ошибка валидации").click();
        modalAlert.shouldHaveText("Ошибка в модальном окне");
        modalAlert.shouldHaveColor(ColorsEnum.DANGER);
        modal.close();
        modal.shouldNotExists();

        // fail with stacktrace
        toolbar.button("Ошибка со стектрейсом").click();
        modal.shouldExists();
        modal.shouldHaveTitle("Модальное окно 2");
        modalAlert.shouldHaveColor(ColorsEnum.DANGER);
        modalAlert.shouldHaveStacktrace();
        modalAlert.shouldHaveText("Произошла внутренняя ошибка");
    }
}
