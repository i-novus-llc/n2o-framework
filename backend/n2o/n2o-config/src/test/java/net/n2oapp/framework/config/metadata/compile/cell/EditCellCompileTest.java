package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.EditType;
import net.n2oapp.framework.api.metadata.meta.control.EditCell;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.widget.table.cell.EditCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.action.CloseActionCompiler;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.instanceOf;

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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack()
        ,new N2oControlsPack(), new N2oControlsV2IOPack());
        builder.ios(new EditCellElementIOv2(), new CloseActionElementIOV1());
        builder.compilers(new EditCellCompiler(), new CloseActionCompiler());
    }

    @Test
    public void testCompileEditCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testEditCell.widget.xml")
                .get(new WidgetContext("testEditCell"));

        EditCell cell = (EditCell)table.getComponent().getCells().get(0);

        assertThat(cell.getSrc(), is("EditableCell"));
        assertThat(cell.getActionId(), is("actionTest"));
        assertThat(cell.getFormat(), is("formatTest"));
        assertThat(cell.getEditType(), is(EditType.inline));

        assertThat(cell.getControl(), notNullValue());
        assertThat(cell.getControl(), instanceOf(InputText.class));
        assertThat(cell.getControl().getSrc(), is("InputText"));
        assertThat(cell.getEditFieldId(), is("itIdTest"));

        cell = (EditCell)table.getComponent().getCells().get(1);
        assertThat(cell.getSrc(), is("EditableCell"));
        assertThat(cell.getEditType(), is(EditType.popup));
        assertThat(cell.getEnabled(), is(false));

        assertThat(cell.getControl(), notNullValue());
        assertThat(cell.getControl(), instanceOf(InputText.class));
        assertThat(cell.getControl().getSrc(), is("InputText"));
    }
}
