package net.n2oapp.framework.autotest.alert;

import net.n2oapp.framework.autotest.api.component.button.Button;
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
 * Тестирование отображение алертов в стэке
 */
public class AlertStackAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/alert/stack/index.page.xml"));
    }

    @Test
    public void testComponent() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Button alert1 = page.widget(FormWidget.class).toolbar().topLeft().button("Алерт 1");
        Button alert2 = page.widget(FormWidget.class).toolbar().topLeft().button("Алерт 2");

        alert1.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 1");

        alert2.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 2");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 1");

        alert2.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 2");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 2");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 1");

        alert1.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Алерт 1");
        page.alerts(Alert.Placement.top).alert(1).shouldHaveText("Алерт 2");
        page.alerts(Alert.Placement.top).alert(2).shouldHaveText("Алерт 2");
    }
}
