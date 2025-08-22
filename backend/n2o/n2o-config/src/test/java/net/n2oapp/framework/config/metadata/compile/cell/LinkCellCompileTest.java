package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.cell.LinkCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тест на компиляцию ячейки со ссылкой
 */
class LinkCellCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testLinkCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testLinkCell.page.xml")
                .get(new PageContext("testLinkCell"));
        Table table = (Table) page.getWidget();
        LinkCell cell = (LinkCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("LinkCell"));
        assertThat(cell.getId(), is("test0"));
        assertThat(cell.getIcon(), nullValue());

        cell = (LinkCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getId(), is("test1"));
        assertThat(cell.getIcon(), is("iconTest"));

        cell = (LinkCell) table.getComponent().getBody().getCells().get(2);
        assertThat(cell.getId(), is("test2"));
        assertThat(cell.getIcon(), is("`id`"));
        assertThat(cell.getJsonProperties().get("codeVerified"), CoreMatchers.is("`emailSender.status=='send'`"));

        cell = (LinkCell) table.getComponent().getBody().getCells().get(3);
        assertThat(cell.getId(), is("test3"));
        assertThat(cell.getAction(), nullValue());
        assertThat(cell.getUrl(), is("`'/test/'+uid`"));
        assertThat(cell.getTarget(), is(TargetEnum.NEW_WINDOW));

        cell = (LinkCell) table.getComponent().getBody().getCells().get(4);
        assertThat(cell.getId(), is("test4"));

        cell = (LinkCell) table.getComponent().getBody().getCells().get(5);
        assertThat(cell.getId(), is("test5"));
        assertThat(cell.getAction(), instanceOf(LinkActionImpl.class));
    }
}
