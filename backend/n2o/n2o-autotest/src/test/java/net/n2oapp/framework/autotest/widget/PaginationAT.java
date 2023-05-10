package net.n2oapp.framework.autotest.widget;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
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
 * Автотест для пагинации
 */
public class PaginationAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testTableSimplePaging() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/paging");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        Paging paging = table.paging();
        paging.shouldHaveTotalElements(30);
        paging.countButtonShouldNotBeVisible();
        paging.prevButtonShouldBeDisabled();
        paging.nextButtonShouldBeEnabled();
        paging.shouldHaveLast();
        paging.lastShouldHavePage("10");
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.pageNumberButtonShouldBeVisible("5");
        paging.pageNumberButtonShouldNotBeVisible("6");

        paging.shouldHaveActivePage("1");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");
        paging.selectNext();
        paging.shouldHaveActivePage("2");
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.prevButtonShouldBeEnabled();
        paging.nextButtonShouldBeEnabled();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test4");
        paging.selectPage("3");
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.shouldHaveActivePage("3");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test7");
        paging.selectNext();
        paging.shouldHaveActivePage("4");
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldBeVisible("3");

        paging.selectNext();
        paging.firstPageShouldHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.shouldHaveActivePage("5");
        paging.pageNumberButtonShouldBeVisible("4");
        paging.pageNumberButtonShouldBeVisible("6");
        paging.pageNumberButtonShouldNotBeVisible("3");
        paging.pageNumberButtonShouldNotBeVisible("7");

        paging.selectNext();
        paging.selectNext();
        paging.shouldHaveActivePage("7");
        paging.firstPageShouldHaveEllipsis();
        paging.lastPageShouldNotHaveEllipsis();

        paging.selectFirst();
        paging.shouldHaveActivePage("1");
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");
        paging.selectLast();
        paging.shouldHaveActivePage("10");
        paging.prevButtonShouldBeEnabled();
        paging.nextButtonShouldBeDisabled();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test28");
    }

    @Test
    public void testShowCountNever() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/paging");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/simple_never/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        Paging paging = table.paging();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");
        paging.shouldHaveActivePage("1");
        paging.shouldNotHaveTotalElements();
        paging.selectLast();
        paging.shouldHaveActivePage("10");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test28");
        paging.shouldNotHaveTotalElements();
    }

    @Test
    public void testTableInfinitePaging() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/paging/infinite");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/infinite/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/infinite/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        Paging paging = table.paging();
        paging.shouldNotHaveTotalElements();
        paging.countButtonShouldNotBeVisible();
        paging.prevButtonShouldBeDisabled();
        paging.nextButtonShouldBeEnabled();
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldNotBeVisible("3");

        paging.shouldHaveActivePage("1");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");
        paging.selectNext();

        paging.shouldHaveActivePage("2");
        paging.prevButtonShouldBeEnabled();
        paging.nextButtonShouldBeEnabled();
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.pageNumberButtonShouldBeVisible("3");
        paging.pageNumberButtonShouldNotBeVisible("4");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test4");

        paging.selectNext();
        paging.shouldHaveActivePage("3");
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldBeVisible("4");
        paging.pageNumberButtonShouldNotBeVisible("5");
        paging.lastPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test7");

        paging.selectPage("4");
        paging.shouldHaveActivePage("4");
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldBeVisible("3");
        paging.pageNumberButtonShouldBeVisible("5");
        paging.pageNumberButtonShouldNotBeVisible("6");
        paging.lastPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test10");

        paging.selectPage("5");
        paging.shouldHaveActivePage("5");
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.pageNumberButtonShouldBeVisible("4");
        paging.pageNumberButtonShouldBeVisible("6");
        paging.pageNumberButtonShouldNotBeVisible("3");
        paging.pageNumberButtonShouldNotBeVisible("7");
        paging.firstPageShouldHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test13");

        paging.selectPage("6");
        paging.shouldHaveActivePage("6");
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.pageNumberButtonShouldBeVisible("5");
        paging.pageNumberButtonShouldBeVisible("7");
        paging.pageNumberButtonShouldNotBeVisible("4");
        paging.pageNumberButtonShouldNotBeVisible("8");
        paging.firstPageShouldHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test16");

        paging.selectNext();
        paging.shouldHaveActivePage("7");
        paging.prevButtonShouldBeEnabled();
        paging.nextButtonShouldBeDisabled();
        paging.shouldHaveFirst();
        paging.shouldHaveLast();
        paging.lastShouldHavePage("7");
        paging.pageNumberButtonShouldBeVisible("5");
        paging.pageNumberButtonShouldBeVisible("6");
        paging.pageNumberButtonShouldNotBeVisible("8");
        paging.lastPageShouldNotHaveEllipsis();
        paging.firstPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test19");

        paging.selectFirst();
        paging.shouldNotHaveLast();
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldNotBeVisible("3");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");
    }

    @Test
    public void testShowCountByRequest() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/paging/infinite_by_request");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/infinite_by_request/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/infinite_by_request/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        Paging paging = table.paging();
        paging.shouldNotHaveTotalElements();
        paging.countButtonShouldBeVisible();
        paging.prevButtonShouldBeDisabled();
        paging.nextButtonShouldBeEnabled();
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldNotBeVisible("3");
        paging.shouldHaveActivePage("1");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");

        paging.countButtonClick();
        paging.shouldHaveTotalElements(18);
        paging.countButtonShouldNotBeVisible();
        paging.shouldHaveActivePage("1");
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldNotBeVisible("3");
        paging.shouldNotHaveLast();
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test1");

        Selenide.refresh();
        paging.countButtonShouldBeVisible();
        paging.selectNext();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test4");
        paging.selectNext();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test7");
        paging.shouldHaveActivePage("3");
        paging.countButtonClick();
        paging.shouldHaveTotalElements(18);
        paging.countButtonShouldNotBeVisible();
        paging.shouldHaveActivePage("3");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test7");
        paging.shouldHaveFirst();
        paging.shouldNotHaveLast();
        paging.firstPageShouldNotHaveEllipsis();
        paging.lastPageShouldHaveEllipsis();
        paging.pageNumberButtonShouldBeVisible("2");
        paging.pageNumberButtonShouldBeVisible("4");
        paging.pageNumberButtonShouldNotBeVisible("5");

        Selenide.refresh();
        paging.selectNext();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test4");
        paging.selectNext();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test7");
        paging.selectNext();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test10");
        paging.selectNext();
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test13");
        paging.selectNext();
        paging.shouldHaveActivePage("6");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test16");
        paging.countButtonClick();
        paging.shouldHaveTotalElements(18);
        paging.shouldHaveActivePage("6");
        table.columns().rows().row(0).cell(0, TextCell.class).shouldHaveText("test16");
        paging.pageNumberButtonShouldBeVisible("4");
        paging.pageNumberButtonShouldBeVisible("5");
        paging.pageNumberButtonShouldNotBeVisible("3");
    }

    @Test
    public void testPrevNextButtonsLabel() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/paging/");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/prev_next_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/paging/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        Paging paging = table.paging();

        paging.shouldNotHavePrev();
        paging.shouldNotHaveNext();

        table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        paging = table.paging();
        paging.shouldHavePrev();
        paging.prevShouldHaveLabel("Лево");
        paging.prevShouldHaveIcon("fa fa-angle-left");
        paging.shouldHaveNext();
        paging.nextShouldHaveLabel("Право");
        paging.nextShouldHaveIcon("fa fa-trash");

    }
}
