package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для обновления виджета после закрытия модального окна
 */
public class RefreshAfterCloseModalAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/modal.page.xml"));
    }

    @Test
    public void testModal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/modal/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/modal/test.object.xml"));

        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Обновление виджета после закрытия модального окна");
        page.shouldExists();

        TableWidget.Rows rows = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        StandardButton modalBtn = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).toolbar().topLeft().button("Modal");
        modalBtn.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();

        FormWidget modalForm = modalPage.content(SimplePage.class).widget(FormWidget.class);
        StandardButton saveBtn = modalForm.toolbar().bottomLeft().button("Save");
        StandardButton closeBtn = modalForm.toolbar().bottomLeft().button("Close");
        InputText inputText = modalForm.fields().field("name").control(InputText.class);

        // close by button
        inputText.val("new1");
        saveBtn.click();
        closeBtn.click();

        rows.shouldHaveSize(5);
        rows.row(0).cell(1).textShouldHave("new1");

        // close by cross icon
        modalBtn.click();
        modalPage.shouldExists();
        inputText.val("new2");
        saveBtn.click();
        modalPage.close();

        rows.shouldHaveSize(6);
        rows.row(0).cell(1).textShouldHave("new2");

        // close by ESC button
        modalBtn.click();
        modalPage.shouldExists();
        inputText.val("new3");
        saveBtn.click();
        modalPage.closeByEsc();

        rows.shouldHaveSize(7);
        rows.row(0).cell(1).textShouldHave("new3");

        // close by click backdrop
        modalBtn.click();
        modalPage.shouldExists();
        inputText.val("new4");
        saveBtn.click();
        modalPage.clickBackdrop();

        rows.shouldHaveSize(8);
        rows.row(0).cell(1).textShouldHave("new4");
    }

    @Test
    public void testDrawer() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/drawer/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/close/refresh/drawer/test.object.xml"));

        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Обновление виджета после закрытия модального окна");
        page.shouldExists();

        TableWidget.Rows rows = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        StandardButton drawerBtn = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).toolbar().topLeft().button("Drawer");
        drawerBtn.click();
        Drawer drawerPage = N2oSelenide.drawer();
        drawerPage.shouldExists();

        FormWidget modalForm = drawerPage.content(SimplePage.class).widget(FormWidget.class);
        StandardButton saveBtn = modalForm.toolbar().bottomLeft().button("Save");
        StandardButton closeBtn = modalForm.toolbar().bottomLeft().button("Close");
        InputText inputText = modalForm.fields().field("name").control(InputText.class);

        // close by button
        inputText.val("new1");
        saveBtn.click();
        closeBtn.click();

        rows.shouldHaveSize(5);
        rows.row(0).cell(1).textShouldHave("new1");

        // close by cross icon
        drawerBtn.click();
        drawerPage.shouldExists();
        inputText.val("new2");
        saveBtn.click();
        drawerPage.close();

        rows.shouldHaveSize(6);
        rows.row(0).cell(1).textShouldHave("new2");

        // close by ESC button
        drawerBtn.click();
        drawerPage.shouldExists();
        inputText.val("new3");
        saveBtn.click();
        drawerPage.closeByEsc();

        rows.shouldHaveSize(7);
        rows.row(0).cell(1).textShouldHave("new3");

        // close by click backdrop
        drawerBtn.click();
        drawerPage.shouldExists();
        inputText.val("new4");
        saveBtn.click();
        drawerPage.clickBackdrop();

        rows.shouldHaveSize(8);
        rows.row(0).cell(1).textShouldHave("new4");
    }
}
