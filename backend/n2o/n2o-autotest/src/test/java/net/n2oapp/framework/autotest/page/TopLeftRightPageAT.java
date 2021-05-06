package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.TopLeftRightPage;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
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
 * Автотест для страницы с тремя регионами
 */
public class TopLeftRightPageAT extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/page/top_left_right/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/page/top_left_right/openPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/simple/test.header.xml"));
    }

    @Test
    public void testTopLeftRightPage() {
        TopLeftRightPage page = open(TopLeftRightPage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница с тремя регионами");
        page.shouldHaveCssClass("page_class");
        page.shouldHaveStyle("background: blue;");

        Regions topRegions = page.top();
        topRegions.shouldHaveSize(1);
        topRegions.region(0, PanelRegion.class).shouldHaveTitle("Сверху");

        Regions leftRegions = page.left();
        leftRegions.shouldHaveSize(1);
        leftRegions.region(0, PanelRegion.class).shouldHaveTitle("Слева");

        Regions rightRegions = page.right();
        rightRegions.shouldHaveSize(5);
        rightRegions.region(4, PanelRegion.class).shouldHaveTitle("Справа");

        // проверка кнопки скролла в начало страницы
        page.scrollToTopButtonShouldNotExists();
        page.scrollDown();
        page.scrollToTopButtonShouldExists();
        page.clickScrollToTopButton();
        page.scrollToTopButtonShouldNotExists();

        // открытие страницы с хлебными крошками
        FormWidget widget = topRegions.region(0, PanelRegion.class).content().widget(FormWidget.class);
        StandardButton btn = widget.toolbar().topLeft().button("Открыть");
        btn.shouldBeEnabled();
        btn.click();

        SimplePage open = N2oSelenide.page(SimplePage.class);
        open.shouldExists();
        open.breadcrumb().parentTitleShouldHaveText("Страница с тремя регионами");
        open.breadcrumb().titleShouldHaveText("Вторая страница");
    }
}
