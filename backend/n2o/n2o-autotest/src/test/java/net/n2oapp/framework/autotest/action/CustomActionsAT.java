package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
 * Автотест для проверки кастомного действия
 */

class CustomActionsAT extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/custom/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/custom/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/custom/test.object.xml"));
    }

    @Test
    void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница для автотеста по проверке кастомного действия");

        StandardButton customInvoke = page.widget(FormWidget.class).toolbar().topLeft().button("Custom invoke");
        customInvoke.click();

        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldHaveText("Данные сохранены");

        page.breadcrumb().crumb(1).shouldHaveLabel("Страница для редиректа");
    }
}
