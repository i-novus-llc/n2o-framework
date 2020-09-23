package net.n2oapp.framework.autotest.widget.tiles;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
        TilesWidget tiles = page.single().widget(TilesWidget.class);
        tiles.shouldExists();

        Tile tile1 = tiles.tile(0);
        tile1.cells().cell(0, ImageCell.class).imageShouldBe(getBaseUrl() + "/favicon.ico");
        tile1.cells().cell(1, TextCell.class).textShouldHave("text1");
        IconCell iconCell = tile1.cells().cell(2, IconCell.class);
        iconCell.iconShouldBe("fa-plus");
        iconCell.textShouldHave("icon1");
        iconCell.hover();
        page.tooltip().shouldHaveText("icon1");

        BadgeCell badgeCell = tile1.cells().cell(3, BadgeCell.class);
        badgeCell.colorShouldBe(Colors.INFO);
        badgeCell.textShouldHave("alert");
        tile1.cells().cell(4, ProgressBarCell.class).valueShouldBe("50");
        tile1.cells().cell(5, RatingCell.class).valueShouldBe("4");
        tile1.cells().cell(6, CheckboxCell.class).shouldBeChecked();
        StandardButton open = tile1.cells().cell(7, ToolbarCell.class).toolbar().button("Open");
        open.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldExists();
        modal.close();


        Tile tile2 = tiles.tile(1);
        tile2.cells().cell(1, TextCell.class).textShouldHave("text2");
        iconCell = tile2.cells().cell(2, IconCell.class);
        iconCell.iconShouldBe("fa-minus");
        iconCell.textShouldHave("icon2");
        tile2.cells().cell(4, ProgressBarCell.class).valueShouldBe("70");
        tile2.cells().cell(6, CheckboxCell.class).shouldBeUnchecked();
    }

    @Test
    public void testPagination() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/pagination/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/tiles/pagination/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TilesWidget tiles = page.single().widget(TilesWidget.class);
        tiles.shouldExists();

        TilesWidget.Paging paging = tiles.paging();
        paging.activePageShouldBe("1");
        tiles.shouldHaveItems(10);
        paging.totalElementsShouldBe(12);

        paging.selectPage("2");
        paging.activePageShouldBe("2");
        tiles.shouldHaveItems(2);
        tiles.tile(0).cells().cell(0, TextCell.class).textShouldHave("text11");
    }
}

