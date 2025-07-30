package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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
 * Тестирование действия switch-case
 */
public class SwitchCaseActionAT extends AutoTestBase {

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
        builder.packs(
                new N2oAllPagesPack(),
                new N2oApplicationPack(),
                new N2oAllDataPack()
        );
    }


    @Test
    void testSwitchCaseInButton() {
        setJsonPath("net/n2oapp/framework/autotest/action/switch_case/button");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/button/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/button/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/button/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/button/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        TableWidget table = page.widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");

        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(1).click();
        button.click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/type1");
        page.breadcrumb().crumb(0).click();

        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(3).click();
        button.click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/type2");
        page.breadcrumb().crumb(0).click();

        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(2).click();
        button.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveTitle("Сообщение");
    }

    @Test
    void testSwitchCaseInRowClick() {
        setJsonPath("net/n2oapp/framework/autotest/action/switch_case/row_click");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/row_click/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/row_click/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/switch_case/row_click/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();

        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(2).cell(1).shouldExists();
        table.columns().rows().row(2).cell(1).shouldHaveText("2");
        table.columns().rows().row(2).click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveTitle("Алерт");

        table.columns().rows().row(1).cell(1).shouldHaveText("1");
        table.columns().rows().row(1).click();
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/type1");
        page.breadcrumb().crumb(1).shouldHaveLabel("Страница с type 1");
    }
}