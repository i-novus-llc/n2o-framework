package net.n2oapp.framework.autotest.widget.list;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;
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
 * Автотест для виджета Список
 */
public class ListAT extends AutoTestBase {
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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testList() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/form.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/testList.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        ListWidget listWidget = page.widget(ListWidget.class);
        listWidget.shouldHaveSize(10);
        listWidget.content(0).body(TextCell.class).textShouldHave("body1");
        listWidget.content(0).leftTop(ImageCell.class).srcShouldBe(getBaseUrl() + "/favicon.ico");
        listWidget.content(0).leftBottom(TextCell.class).textShouldHave("leftBottom1");
        listWidget.content(0).subHeader(BadgeCell.class).colorShouldBe(Colors.SUCCESS);
        listWidget.paging().totalElementsShouldBe(11);
        listWidget.paging().selectNext();
        listWidget.shouldHaveSize(1);
        listWidget.paging().selectPrev();
        listWidget.shouldHaveSize(10);

        listWidget.content(0).click();
        SimplePage openPage = N2oSelenide.page(SimplePage.class);
        openPage.shouldExists();
        FormWidget form = openPage.widget(FormWidget.class);
        form.shouldExists();
        form.fields().field("body").control(InputText.class).shouldHaveValue("body1");
    }

    @Test
    public void testPaging() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/list/paging/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/list/paging/testListPaging.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        ListWidget list = page.regions().region(0, SimpleRegion.class).content().widget(ListWidget.class);
        Paging paging = list.paging();
        paging.totalElementsShouldBe(8);
        paging.prevShouldNotExist();
        paging.nextShouldNotExist();
        paging.firstShouldExist();
        paging.firstShouldHaveIcon("fa-angle-double-left");
        paging.lastShouldNotExist();

        paging.activePageShouldBe("1");
        list.content(0).body(TextCell.class).textShouldHave("test1");
        paging.selectPage("3");
        paging.activePageShouldBe("3");
        list.content(0).body(TextCell.class).textShouldHave("test7");
        paging.selectFirst();
        paging.activePageShouldBe("1");


        ListWidget list2 = page.regions().region(0, SimpleRegion.class).content().widget(1, ListWidget.class);
        paging = list2.paging();
        paging.totalElementsShouldNotExist();
        paging.prevShouldExist();
        paging.prevShouldHaveLabel("Prev");
        paging.prevShouldHaveIcon("fa-angle-down");
        paging.nextShouldExist();
        paging.nextShouldHaveLabel("Next");
        paging.nextShouldHaveIcon("fa-angle-up");
        paging.firstShouldExist();
        paging.firstShouldHaveLabel("First");
        paging.firstShouldHaveIcon("fa-angle-double-down");
        paging.lastShouldExist();
        paging.lastShouldHaveLabel("Last");
        paging.lastShouldHaveIcon("fa-angle-double-up");

        paging.activePageShouldBe("1");
        list2.content(0).body(TextCell.class).textShouldHave("test1");
        paging.selectNext();
        paging.activePageShouldBe("2");
        list2.content(0).body(TextCell.class).textShouldHave("test4");
        paging.selectPrev();
        paging.activePageShouldBe("1");
        paging.selectLast();
        list2.content(0).body(TextCell.class).textShouldHave("test7");
    }
}
