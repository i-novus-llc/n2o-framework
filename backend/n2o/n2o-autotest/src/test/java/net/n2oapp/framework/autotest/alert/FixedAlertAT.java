package net.n2oapp.framework.autotest.alert;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование фиксированного сообщения об ошибке
 */
public class FixedAlertAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/alert/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/alert/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void fixedAlertTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        StandardButton openModalBtn = table.toolbar().topLeft().button("Добавить пользователя");
        openModalBtn.click();

        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldExists();
        modal.shouldHaveTitle("Добавление пользователя");

        StandardButton saveBtn = modal.toolbar().bottomRight().button("Сохранить");
        saveBtn.click();

        page.alerts().shouldHaveSize(1);
        Alerts.Alert alert = page.alerts().alert(0);
        alert.shouldHaveText("Ошибка");
        alert.shouldHaveColor(Colors.DANGER);
        alert.shouldHavePosition(Alerts.Alert.Position.FIXED);
        alert.shouldHavePlacement(Alerts.Alert.Placement.BOTTOM);
    }
}
