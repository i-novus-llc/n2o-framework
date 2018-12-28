package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLink;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.LinkCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тест на компиляцию ячейки с ссылкой
 */
public class LinkCellCompilerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.ios(new LinkCellElementIOv2());
        builder.compilers(new LinkCellCompiler());
    }

    @Test
    public void testCompileActions() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testLinkCell.widget.xml")
                .get(new WidgetContext("testLinkCell"));

        assertThat(table.getComponent().getCells().get(0).getId(), is("test1"));
        assertThat(((N2oLink)table.getComponent().getCells().get(0)).getIcon(), nullValue());
        assertThat(((N2oLink)table.getComponent().getCells().get(0)).getType(), nullValue());

        assertThat(table.getComponent().getCells().get(1).getId(), is("test2"));
        assertThat(((N2oLink)table.getComponent().getCells().get(1)).getIcon(), is("iconTest2"));
        assertThat(((N2oLink)table.getComponent().getCells().get(1)).getType(), is(IconType.text));

        assertThat(table.getComponent().getCells().get(2).getId(), is("test3"));
        assertThat(((N2oLink)table.getComponent().getCells().get(2)).getIcon(), is("iconTest3"));
        assertThat(((N2oLink)table.getComponent().getCells().get(2)).getType(), is(IconType.icon));

        assertThat(table.getComponent().getCells().get(3).getId(), is("test4"));
        assertThat(((N2oLink)table.getComponent().getCells().get(3)).getIcon(), is("`id`"));
        assertThat(((N2oLink)table.getComponent().getCells().get(3)).getType(), is(IconType.iconAndText));
    }
}
