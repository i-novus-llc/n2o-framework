package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест заголовков страниц
 */
public class PageTitleAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/page/title/title/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/title/title/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/simple/test.header.xml"));
    }

    @Test
    public void testTitle() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Тестирование заголовков страницы");
        page.titleShouldHaveText("Заголовок страницы");

        Toolbar toolbar = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).toolbar().topLeft();
        StandardButton openPageBtn = toolbar.button("Open");
        StandardButton modalBtn = toolbar.button("Modal");
        StandardButton drawerBtn = toolbar.button("Drawer");

        openPageBtn.click();
        SimplePage openPage = N2oSelenide.page(SimplePage.class);
        openPage.breadcrumb().titleShouldHaveText("Вторая страница");
        openPage.titleShouldHaveText("Заголовок второй страницы");

        openPage.breadcrumb().clickLink("Тестирование заголовков страницы");
        page.breadcrumb().titleShouldHaveText("Тестирование заголовков страницы");

        modalBtn.click();
        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldHaveTitle("Модальное окно");
        modal.close();

        drawerBtn.click();
        Drawer drawer = N2oSelenide.drawer(Drawer.class);
        drawer.shouldHaveTitle("Drawer");
        drawer.close();
    }
}
