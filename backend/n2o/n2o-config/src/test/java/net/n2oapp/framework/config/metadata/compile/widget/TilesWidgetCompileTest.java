package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
import net.n2oapp.framework.api.metadata.meta.cell.ImageCell;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

/**
 * Тестирование компиляции виджета Плитки
 */
class TilesWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oAllDataPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oCellsIOPack());
    }

    @Test
    void testTiles() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTilesCompile.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new PageContext("testTilesCompile"));
        Tiles tiles = (Tiles) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(tiles, allOf(
                hasProperty("src", is("TilesWidget")),
                hasProperty("colsSm", is(2)),
                hasProperty("colsMd", is(3)),
                hasProperty("colsLg", is(6)),
                hasProperty("height", is("450px")),
                hasProperty("width", is("300px"))
        ));

        assertThat(tiles.getTile().get(0), allOf(
                hasProperty("id", is("test1")),
                hasProperty("className", is("test")),
                hasProperty("style", hasEntry("color", "red")),
                hasProperty("component", hasProperty("src", is("cell1")))
        ));

        Tiles.Tile tile = tiles.getTile().get(1);
        assertThat(tile.getId(), is("id2"));
        assertThat(tile.getComponent(), instanceOf(TextCell.class));
        assertThat(tile.getComponent().getSrc(), is("TextCell"));
        assertThat(tile.getComponent().getId(), is("id2"));
        assertThat(((TextCell) tile.getComponent()).getFieldKey(), is("test2"));
        assertThat(((TextCell) tile.getComponent()).getTooltipFieldId(), is("tooltip"));

        tile = tiles.getTile().get(2);
        assertThat(tile.getId(), is("test3"));
        assertThat(tile.getComponent(), instanceOf(ImageCell.class));
        assertThat(((ImageCell) tile.getComponent()).getData(), is("/test"));

        assertThat(tiles.getPaging(), allOf(
                hasProperty("next", is(true)),
                hasProperty("prev", is(true)),
                hasProperty("showCount", is(ShowCountTypeEnum.NEVER)),
                hasProperty("showLast", is(true)),
                hasProperty("src", is("pagingSrc"))
        ));

        assertThat(page.getDatasources().get(tiles.getDatasource()).getPaging().getSize(), is(5));

        tiles = (Tiles) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(tiles, allOf(
                hasProperty("src", is("TilesWidget")),
                hasProperty("colsSm", is(1)),
                hasProperty("colsMd", is(2)),
                hasProperty("colsLg", is(4)),
                hasProperty("height", nullValue()),
                hasProperty("width", nullValue())
        ));

        assertThat(tiles.getPaging(), allOf(
                hasProperty("next", is(false)),
                hasProperty("prev", is(false)),
                hasProperty("showCount", is(ShowCountTypeEnum.ALWAYS)),
                hasProperty("size", is(10))
        ));
    }
}
