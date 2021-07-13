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
public class OpenDrawerAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/drawer/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/drawer.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/test.query.xml"));
    }

    @Test
    public void testDrawerPage() {
        SimplePage page = open(SimplePage.class);
        page.breadcrumb().titleShouldHaveText("drawer-tests");
        page.shouldExists();

        TableWidget.Rows rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        rows.shouldBeSelected(0);
        rows.row(1).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(1);
        page.widget(TableWidget.class).toolbar().topLeft().button("openLeft").click();
        Drawer drawerPage = N2oSelenide.drawer();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.placementShouldBe(Drawer.Placement.left);
        drawerPage.widthShouldBe("250");
        drawerPage.heightShouldBe("250");
        drawerPage.footerShouldNotBeFixed();
        SimplePage pg = drawerPage.content(SimplePage.class);
        pg.shouldExists();
        Fields fields = pg.widget(FormWidget.class).fields();
        fields.field("id").control(InputText.class).shouldHaveValue("22");
        drawerPage.scrollDown();
        fields.field("name").control(InputText.class).shouldHaveValue("test200");
        drawerPage.toolbar().bottomRight().button("Button").shouldExists();
        drawerPage.close();
        drawerPage.shouldNotExists();

        rows.row(2).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(2);
        page.widget(TableWidget.class).toolbar().topLeft().button("openTop").click();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.placementShouldBe(Drawer.Placement.top);
        drawerPage.widthShouldBe("300");
        drawerPage.heightShouldBe("300");
        drawerPage.footerShouldBeFixed();
        pg.shouldExists();
        fields.field("id").control(InputText.class).shouldHaveValue("33");
        drawerPage.scrollDown();
        fields.field("name").control(InputText.class).shouldHaveValue("test300");
        drawerPage.closeByEsc();
        drawerPage.shouldNotExists();

        rows.row(3).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(3);
        page.widget(TableWidget.class).toolbar().topLeft().button("openRight").click();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.placementShouldBe(Drawer.Placement.right);
        pg.shouldExists();
        fields.field("id").control(InputText.class).shouldHaveValue("44");
        fields.field("name").control(InputText.class).shouldHaveValue("test400");
        drawerPage.closeByEsc();
        drawerPage.shouldExists();
        drawerPage.close();
        drawerPage.shouldNotExists();

        rows.row(0).cell(0, TextCell.class).element().click();
        rows.shouldBeSelected(0);
        page.widget(TableWidget.class).toolbar().topLeft().button("openBottom").click();
        drawerPage.shouldHaveTitle("drawer-page");
        drawerPage.placementShouldBe(Drawer.Placement.bottom);
        pg.shouldExists();
        fields.field("id").control(InputText.class).shouldHaveValue("11");
        fields.field("name").control(InputText.class).shouldHaveValue("test100");
    }
}
