package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.io.widget.v4.TableElementIOV4;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.io.widget.v5.TilesWidgetIOV5;
import net.n2oapp.framework.config.metadata.merge.widget.N2oFormMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oTableMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oTilesMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oWidgetMerger;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class N2oTilesMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs( new N2oCellsPack())
                .ios(new TilesWidgetIOV5())
                .mergers(new N2oTilesMerger());
    }

    @Test
    void mergeTiles() {
        N2oTiles tiles = merge("net/n2oapp/framework/config/metadata/merge/widget/parentTilesMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childTilesMerger.widget.xml")
                .get("parentTilesMerger", N2oTiles.class);

        assertThat(tiles.getHeight(), is("101"));
        assertThat(tiles.getWidth(), is("101"));
        assertThat(tiles.getPagination().getClassName(), is("childPagination"));
        assertThat(tiles.getColsLg(), is(6));
        assertThat(tiles.getColsSm(), is(1));
        assertThat(tiles.getColsMd(), is(3));
        assertThat(tiles.getContent()[0].getId(), is("childBlock"));
        assertThat(tiles.getContent()[1].getId(), is("parentBlock"));
    }
}
