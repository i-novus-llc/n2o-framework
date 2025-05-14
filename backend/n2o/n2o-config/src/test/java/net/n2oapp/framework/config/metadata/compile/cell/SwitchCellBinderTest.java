package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.cell.SwitchCell;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование связывания с данными переключателя ячейки таблицы
 */
class SwitchCellBinderTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oActionsPack(), new N2oCellsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    void testUrlResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/cell/testSwitchCellBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/cell/test.object.xml");
        PageContext context = new PageContext("testSwitchCellBinder", "/p/w/:id/modal");
        SimplePage page = (SimplePage) pipeline.get(context, new DataSet().add("id", "2"));
        SwitchCell switchCell = (SwitchCell) ((Table) page.getWidget()).getComponent().getBody().getCells().get(0);
        ClientDataProvider dataProvider = ((InvokeAction)((ToolbarCell) switchCell.getSwitchList().get("2")).getToolbar().get(0).getButtons().get(0).getAction()).getPayload().getDataProvider();
        assertThat(dataProvider.getUrl(), is("n2o/data/p/w/2/modal/p_w_modal_mi0"));
    }
}
