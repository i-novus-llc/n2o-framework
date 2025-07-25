package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Автотест для действия открытия drawer окна
 */
class OpenDrawerAT extends AutoTestBase {

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
        setResourcePath("net/n2oapp/framework/autotest/action/drawer/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/simple/drawer.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/simple/test.query.xml"));
    }

    @Test
    void testDrawerPage() {
        SimplePage page = open(SimplePage.class);
        page.breadcrumb().crumb(0).shouldHaveLabel("drawer-tests");
        page.shouldExists();

        TableWidget.Rows rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        rows.shouldBeSelected(0);
        rows.row(1).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(1);
        page.widget(TableWidget.class).toolbar().topLeft().button("openLeft").click();
        Drawer drawerPage = N2oSelenide.drawer();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.shouldHavePlacement(Drawer.PlacementEnum.LEFT);
        drawerPage.shouldHaveWidth("250");
        drawerPage.shouldHaveHeight("250");
        drawerPage.shouldNotHaveFixedFooter();
        SimplePage pg = drawerPage.content(SimplePage.class);
        pg.shouldExists();
        Fields fields = pg.widget(FormWidget.class).fields();
        fields.field("id").control(InputText.class).shouldHaveValue("22");
        drawerPage.scrollDown();
        fields.field("name").control(InputText.class).shouldHaveValue("test200");
        drawerPage.toolbar().bottomRight().button("Button").shouldExists();
        drawerPage.close();
        drawerPage.shouldNotExists();

        checkRow2(page, rows, drawerPage, pg, fields);

        checkRow3(page, rows, drawerPage, pg, fields);

        checkRow0(page, rows, drawerPage, pg, fields);
    }

    private static void checkRow2(SimplePage page, TableWidget.Rows rows, Drawer drawerPage, SimplePage pg, Fields fields) {
        rows.row(2).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(2);
        page.widget(TableWidget.class).toolbar().topLeft().button("openTop").click();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.shouldHavePlacement(Drawer.PlacementEnum.TOP);
        drawerPage.shouldHaveWidth("300");
        drawerPage.shouldHaveHeight("300");
        drawerPage.shouldHaveFixedFooter();
        pg.shouldExists();
        fields.field("id").control(InputText.class).shouldHaveValue("33");
        drawerPage.scrollDown();
        fields.field("name").control(InputText.class).shouldHaveValue("test300");
        drawerPage.closeByEsc();
        drawerPage.shouldNotExists();
    }

    private static void checkRow3(SimplePage page, TableWidget.Rows rows, Drawer drawerPage, SimplePage pg, Fields fields) {
        rows.row(3).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(3);
        page.widget(TableWidget.class).toolbar().topLeft().button("openRight").click();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.shouldHavePlacement(Drawer.PlacementEnum.RIGHT);
        pg.shouldExists();
        fields.field("id").control(InputText.class).shouldHaveValue("44");
        fields.field("name").control(InputText.class).shouldHaveValue("test400");
        drawerPage.closeByEsc();
        drawerPage.shouldExists();
        drawerPage.close();
        drawerPage.shouldNotExists();
    }

    private static void checkRow0(SimplePage page, TableWidget.Rows rows, Drawer drawerPage, SimplePage pg, Fields fields) {
        rows.row(0).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(0);
        page.widget(TableWidget.class).toolbar().topLeft().button("openBottom").click();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.shouldHavePlacement(Drawer.PlacementEnum.BOTTOM);
        pg.shouldExists();
        fields.field("id").control(InputText.class).shouldHaveValue("11");
        fields.field("name").control(InputText.class).shouldHaveValue("test100");
    }
}
