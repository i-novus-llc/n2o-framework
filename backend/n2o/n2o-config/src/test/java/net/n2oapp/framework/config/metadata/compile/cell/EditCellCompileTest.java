package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.table.EditType;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.cell.EditCell;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.widget.table.cell.EditCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.control.InputTextCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тест компиляции редактируемой ячейки
 */
public class EditCellCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack(), new N2oActionsPack());
        builder.ios(new EditCellElementIOv2(), new InputTextIOv2());
        builder.compilers(new EditCellCompiler(), new InputTextCompiler());
    }

    @Test
    public void testCompileEditCell() {
        WidgetContext context = new WidgetContext("testEditCell", "main/:id/open");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "main"));
        Map<String, ModelLink> mapping = new HashMap<>();
        mapping.put("id", new ModelLink(ReduxModel.RESOLVE, "main", "id"));
        context.setPathRouteMapping(mapping);
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testEditCell.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/cell/testEditCell.object.xml")
                .get(context);

        EditCell cell = (EditCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("EditableCell"));
        assertThat(cell.getFormat(), is("formatTest"));
        assertThat(cell.getEditType(), is(EditType.inline));

        assertThat(cell.getControl(), notNullValue());
        assertThat(cell.getControl(), instanceOf(InputText.class));
        assertThat(cell.getControl().getSrc(), is("InputText"));
        assertThat(cell.getEditFieldId(), is("itIdTest"));

        assertThat(cell.getCompiledAction(), notNullValue());
        assertThat(((InvokeAction)cell.getCompiledAction()).getPayload().getDataProvider().getUrl(), is("n2o/data/main/:id/open/test1"));
        assertThat(((InvokeAction)cell.getCompiledAction()).getPayload().getDataProvider().getPathMapping().get("id"), is(new ModelLink(ReduxModel.RESOLVE, "main", "id")));

        cell = (EditCell) table.getComponent().getCells().get(1);
        assertThat(cell.getSrc(), is("EditableCell"));
        assertThat(cell.getEditType(), is(EditType.popup));
        assertThat(cell.getEnabled(), is(false));

        assertThat(cell.getControl(), notNullValue());
        assertThat(cell.getControl(), instanceOf(InputText.class));
        assertThat(cell.getControl().getSrc(), is("InputText"));

    }
}
