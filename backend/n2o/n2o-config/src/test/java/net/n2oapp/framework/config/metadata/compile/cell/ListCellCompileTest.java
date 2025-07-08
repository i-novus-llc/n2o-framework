package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.cell.*;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тест на компиляцию ячейки со списком
 */
class ListCellCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oCellsPack());
    }

    @Test
    void testListCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testListCell.page.xml")
                .get(new PageContext("testListCell"));
        Table<?> table = (Table<?>) page.getWidget();

        // Простой list
        ListCell listCell = (ListCell) table.getComponent().getBody().getCells().get(0);
        assertThat(listCell.getSrc(), is("CollapsedCell"));
        assertThat(listCell.getSize(), is(3));
        assertThat(listCell.getColor(), is("danger"));
        assertThat(listCell.getInline(), is(true));
        assertThat(listCell.getSeparator(), is("; "));
        assertThat(listCell.getContent(), nullValue());

        // list с text
        listCell = (ListCell) table.getComponent().getBody().getCells().get(1);
        checkTextCell(listCell, ", ", "password");

        // list со скрытым text
        listCell = (ListCell) table.getComponent().getBody().getCells().get(2);
        checkTextCell(listCell, ". ", null);


        // list с link
        listCell = (ListCell) table.getComponent().getBody().getCells().get(3);
        assertThat(listCell.getSize(), is(5));
        assertThat(listCell.getInline(), is(false));
        assertThat(listCell.getSeparator(), nullValue());
        assertThat(listCell.getContent().getSrc(), is("LinkCell"));
        LinkCell linkCell = (LinkCell) listCell.getContent();
        assertThat(linkCell.getUrl(), is("/test"));
        assertThat(linkCell.getTarget(), is(TargetEnum.NEW_WINDOW));
        assertThat(linkCell.getFieldKey(), is("linkField"));

        // list с badge
        listCell = (ListCell) table.getComponent().getBody().getCells().get(4);
        assertThat(listCell.getContent().getSrc(), is("BadgeCell"));
        BadgeCell badgeCell = (BadgeCell) listCell.getContent();
        assertThat(badgeCell.getText(), is("`text`"));
        assertThat(badgeCell.getColor(), is("success"));
        assertThat(badgeCell.getFieldKey(), is("badgeField"));

        // list с custom cell
        listCell = (ListCell) table.getComponent().getBody().getCells().get(5);
        assertThat(listCell.getContent().getSrc(), is("testCell"));
        AbstractCell customCell = (AbstractCell) listCell.getContent();
        assertThat(customCell.getFieldKey(), is("customField"));
    }

    private static void checkTextCell(ListCell listCell, String separator, String format) {
        assertThat(listCell.getSrc(), is("CollapsedCell"));
        assertThat(listCell.getSize(), is(3));
        assertThat(listCell.getInline(), is(true));
        assertThat(listCell.getContent().getSrc(), is("TextCell"));
        TextCell textCell = (TextCell) listCell.getContent();
        assertThat(textCell.getFieldKey(), is("textField"));
        assertThat(listCell.getSeparator(), is(separator));
        if (format != null) assertThat(textCell.getFormat(), is(format));
    }
}
