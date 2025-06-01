package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Тестирование мультидействия
 */
class MultiActionAT extends AutoTestBase {

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
    void testMulti() {
        setResourcePath("net/n2oapp/framework/autotest/action/multi/simple");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/simple/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/simple/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");

        InputText name = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("name").control(InputText.class);

        table.columns().rows().row(1).click();
        button.click();
        name.shouldHaveValue("test2");
        Selenide.back();

        table.columns().rows().row(3).click();
        button.click();
        name.shouldHaveValue("test4");
        Selenide.back();
    }

    @Test
    void testActionAfterFail() {
        setResourcePath("net/n2oapp/framework/autotest/action/multi/action_after_fail");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi/action_after_fail/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/action_after_fail/test.object.xml"));

        StandardPage page = open(StandardPage.class);
        InputText inputText = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields().field("input").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).toolbar().topLeft().button("Кнопка");

        inputText.setValue("test");
        button.click();
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldExists();
        inputText.shouldHaveValue("test");

        inputText.setValue("123");
        button.click();
        inputText.shouldBeEmpty();
    }

    @Test
    void testChainBreakAfterPageAction() {
        setResourcePath("net/n2oapp/framework/autotest/action/multi/chain_break_after_page_action");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi/chain_break_after_page_action/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi/chain_break_after_page_action/modal.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        Checkbox checkbox = page.widget(FormWidget.class).fields().field("check").control(Checkbox.class);
        checkbox.shouldBeChecked();

        StandardButton button = form.toolbar().topLeft().button("show-modal");
        button.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Страница 2");
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldNotExists();
        modalPage.close();
        modalPage.shouldNotExists();
        checkbox.setChecked(false);
        button.click();
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("Модальное окно не будет открыто");

        checkbox.setChecked(true);
        button = form.toolbar().topLeft().button("open-page");
        button.click();
        SimplePage openPage = N2oSelenide.page(SimplePage.class);
        openPage.shouldExists();
        page.breadcrumb().crumb(1).shouldHaveLabel("Страница 2");
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldNotExists();
        page.breadcrumb().crumb(0).click();
        checkbox.setChecked(false);
        button.click();
        alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("Страница не будет открыта");

        checkbox.setChecked(true);
        button = form.toolbar().topLeft().button("open-drawer");
        button.click();
        Drawer drawerPage = N2oSelenide.drawer();
        drawerPage.shouldExists();
        drawerPage.shouldHaveTitle("Страница 2");
        page.alerts(Alert.PlacementEnum.TOP).alert(0).shouldNotExists();
        drawerPage.close();
        checkbox.setChecked(false);
        button.click();
        alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("Выдвижное окно не будет открыто");
    }
}
