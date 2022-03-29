package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
 * Автотест разрешения ссылок в заголовках страниц
 */
public class PageTitleLinkResolveAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/title/params/test.query.xml"));
    }
    
    @Test
    public void testPathParam() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/page/title/params/path_params/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/title/params/path_params/masterWidget.widget.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/title/params/path_params/page.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems content = page.regions().region(0, SimpleRegion.class).content();
        TableWidget table = content.widget(TableWidget.class);
        table.shouldExists();
        FormWidget form = content.widget(1, FormWidget.class);
        form.shouldExists();

        table.columns().rows().row(2).click();
        InputText name = form.fields().field("name").control(InputText.class);
        name.shouldHaveValue("test3");

        Toolbar tableToolbar = table.toolbar().topLeft();
        tableToolbar.button("Open page").click();

        // test title in open page
        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().titleShouldHaveText("Page name=test3 type=type2");
        open.breadcrumb().clickLink("Start page");

        page.breadcrumb().titleShouldHaveText("Start page");
        table.columns().rows().row(2).click();
        tableToolbar.button("Modal").click();

        // test title in modal
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Page name=test3 type=type2");
        modal.close();
        modal.shouldNotExists();

        // test title in open page (opened in dependent widget with master widget-id)
        table.columns().rows().row(1).click();
        name.shouldHaveValue("test2");
        Toolbar formToolbar = form.toolbar().bottomLeft();
        formToolbar.button("Open page from master").click();
        open.shouldExists();
        open.breadcrumb().titleShouldHaveText("Page name=test2 type=type1");
        open.breadcrumb().clickLink("Start page");

        // test title in modal (opened from dependent widget)
        page.breadcrumb().titleShouldHaveText("Start page");
        table.columns().rows().row(1).click();
        formToolbar.button("Modal from detail").click();
        modal.shouldExists();
        modal.shouldHaveTitle("Page name=test2 type=type1");
        modal.close();
        modal.shouldNotExists();
    }

    @Test
    public void testQueryParams() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/page/title/params/query_params/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/title/params/query_params/page.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems content = page.regions().region(0, SimpleRegion.class).content();
        TableWidget table = content.widget(TableWidget.class);
        table.shouldExists();
        FormWidget form = content.widget(1, FormWidget.class);
        form.shouldExists();

        table.columns().rows().row(2).click();
        InputText name = form.fields().field("name").control(InputText.class);
        name.shouldHaveValue("test3");

        Toolbar tableToolbar = table.toolbar().topLeft();
        tableToolbar.button("Open page").click();

        // test title in open page
        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().titleShouldHaveText("Page id=3 name=test3 type=type2");
        open.breadcrumb().clickLink("Start page");

        page.breadcrumb().titleShouldHaveText("Start page");
        // lost query parameter after returning from open page
        table.columns().rows().row(2).click();
        tableToolbar.button("Modal").click();

        // test title in modal
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Page id=3 name=test3 type=type2");
        modal.close();
        modal.shouldNotExists();

        // test title in open page (opened in dependent widget with master widget-id)
        table.columns().rows().row(1).click();
        name.shouldHaveValue("test2");
        Toolbar formToolbar = form.toolbar().bottomLeft();
        formToolbar.button("Open page from master").click();
        open.shouldExists();
        open.breadcrumb().titleShouldHaveText("Page id=2 name=test2 type=type1");
        open.breadcrumb().clickLink("Start page");

        // test title in modal (opened from dependent widget)
        page.breadcrumb().titleShouldHaveText("Start page");
        // lost query parameter after returning from open page
        table.columns().rows().row(1).click();
        formToolbar.button("Modal from detail").click();
        modal.shouldExists();
        modal.shouldHaveTitle("Page id=2 name=test2 type=type1");
        modal.close();
        modal.shouldNotExists();
    }
}
