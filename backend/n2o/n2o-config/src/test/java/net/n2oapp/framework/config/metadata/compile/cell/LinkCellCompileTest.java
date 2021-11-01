package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.LinkCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тест на компиляцию ячейки с ссылкой
 */
public class LinkCellCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.ios(new LinkCellElementIOv2());
        builder.compilers(new LinkCellCompiler());
    }

    @Test
    public void testLinkCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testLinkCell.widget.xml")
                .get(new WidgetContext("testLinkCell"));

        N2oLinkCell cell = (N2oLinkCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("LinkCell"));
        assertThat(cell.getId(), is("test1"));
        assertThat(cell.getIcon(), nullValue());
        assertThat(cell.getType(), is(IconType.text));

        cell = (N2oLinkCell) table.getComponent().getCells().get(1);
        assertThat(cell.getId(), is("test2"));
        assertThat(cell.getIcon(), nullValue());
        assertThat(cell.getType(), is(IconType.text));

        cell = (N2oLinkCell) table.getComponent().getCells().get(2);
        assertThat(cell.getId(), is("test3"));
        assertThat(cell.getIcon(), is("iconTest3"));
        assertThat(cell.getType(), is(IconType.icon));

        cell = (N2oLinkCell) table.getComponent().getCells().get(3);
        assertThat(cell.getId(), is("test4"));
        assertThat(cell.getIcon(), is("`id`"));
        assertThat(cell.getType(), is(IconType.iconAndText));

        cell = (N2oLinkCell) table.getComponent().getCells().get(4);
        assertThat(cell.getId(), is("test5"));
        assertThat(cell.getUrl(), is("`'/test/'+uid`"));
        assertThat(cell.getTarget(), is(Target.newWindow));
        assertThat(cell.getAction(), nullValue());

        cell = (N2oLinkCell) table.getComponent().getCells().get(5);
        assertThat(cell.getId(), is("test6"));
        assertThat(cell.getActionId(), is("update"));

        cell = (N2oLinkCell) table.getComponent().getCells().get(6);
        assertThat(cell.getId(), is("test7"));
        assertThat(cell.getCompiledAction(), instanceOf(LinkActionImpl.class));
    }
}
