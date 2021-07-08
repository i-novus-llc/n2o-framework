package net.n2oapp.framework.autotest.widget.tiles;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.tiles.Tile;
import net.n2oapp.framework.autotest.api.component.widget.tiles.TilesWidget;
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
 * Автотест для виджета Плитки
 */
public class TilesAT extends AutoTestBase {

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
    public void testTiles() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TilesWidget tiles = page.widget(TilesWidget.class);
        tiles.shouldExists();

        Tile tile1 = tiles.tile(0);
        tile1.shouldHaveWidth(200);
        tile1.shouldHaveHeight(250);
        tile1.blocks().cell(0, ImageCell.class).imageShouldBe(getBaseUrl() + "/favicon.ico");
        tile1.blocks().cell(1, TextCell.class).textShouldHave("text1");
        IconCell iconCell = tile1.blocks().cell(2, IconCell.class);
        iconCell.iconShouldBe("fa-plus");
        iconCell.textShouldHave("icon1");
        iconCell.hover();
        page.tooltip().shouldHaveText("icon1");

        BadgeCell badgeCell = tile1.blocks().cell(3, BadgeCell.class);
        badgeCell.colorShouldBe(Colors.INFO);
        badgeCell.textShouldHave("alert");
        tile1.blocks().cell(4, ProgressBarCell.class).valueShouldBe("50");
        tile1.blocks().cell(5, RatingCell.class).valueShouldBe("4");
        tile1.blocks().cell(6, CheckboxCell.class).shouldBeChecked();
        StandardButton open = tile1.blocks().cell(7, ToolbarCell.class).toolbar().button("Open");
        open.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.close();


        Tile tile2 = tiles.tile(1);
        tile2.blocks().cell(1, TextCell.class).textShouldHave("text2");
        iconCell = tile2.blocks().cell(2, IconCell.class);
        iconCell.iconShouldBe("fa-minus");
        iconCell.textShouldHave("icon2");
        tile2.blocks().cell(4, ProgressBarCell.class).valueShouldBe("70");
        tile2.blocks().cell(6, CheckboxCell.class).shouldBeUnchecked();
    }

    @Test
    public void testPaging() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/paging/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/paging/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TilesWidget tiles = page.regions().region(0, SimpleRegion.class).content().widget(TilesWidget.class);
        tiles.shouldExists();

        Paging paging = tiles.paging();
        paging.totalElementsShouldBe(8);
        paging.shouldHaveLayout(Paging.Layout.SEPARATED);
        paging.prevShouldNotExist();
        paging.nextShouldNotExist();
        paging.firstShouldExist();
        paging.firstShouldHaveIcon("fa-angle-double-left");
        paging.lastShouldNotExist();

        paging.activePageShouldBe("1");
        tiles.tile(0).blocks().cell(0, TextCell.class).textShouldHave("test1");
        tiles.shouldHaveItems(3);
        paging.selectPage("3");
        paging.activePageShouldBe("3");
        tiles.shouldHaveItems(2);
        tiles.tile(0).blocks().cell(0, TextCell.class).textShouldHave("test7");
        paging.selectFirst();
        paging.activePageShouldBe("1");
        tiles.tile(0).blocks().cell(0, TextCell.class).textShouldHave("test1");


        TilesWidget tiles2 = page.regions().region(0, SimpleRegion.class).content().widget(1, TilesWidget.class);
        tiles2.shouldExists();

        paging = tiles2.paging();
        paging.totalElementsShouldNotExist();
        paging.shouldHaveLayout(Paging.Layout.SEPARATED_ROUNDED);
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
        tiles2.tile(0).blocks().cell(0, TextCell.class).textShouldHave("test1");
        paging.selectNext();
        paging.activePageShouldBe("2");
        tiles2.tile(0).blocks().cell(0, TextCell.class).textShouldHave("test4");
        paging.selectPrev();
        paging.activePageShouldBe("1");
        paging.selectLast();
        tiles2.shouldHaveItems(2);
        tiles2.tile(0).blocks().cell(0, TextCell.class).textShouldHave("test7");
    }
}

