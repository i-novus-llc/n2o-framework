package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v5.ListWidgetElementIOv5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование слияния виджетов {@code <list>}
 */
class N2oListMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oCellsV3IOPack())
                .ios(new ListWidgetElementIOv5())
                .mergers(new N2oListMerger());
    }

    @Test
    void mergeList() {
        N2oListWidget list = merge("net/n2oapp/framework/config/metadata/merge/widget/parentListMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childListMerger.widget.xml")
                .get("parentListMerger", N2oListWidget.class);

        assertThat(list.getPagination().getClassName(), is("childPagination"));
        assertThat(list.getContent().length, is(2));
        N2oIconCell iconCell = (N2oIconCell) list.getContent()[0].getCell();
        assertThat(iconCell.getText(), is("childIcon"));
        N2oImageCell imageCell = (N2oImageCell) list.getContent()[1].getCell();
        assertThat(imageCell.getData(), is("testUrl"));
    }
}
