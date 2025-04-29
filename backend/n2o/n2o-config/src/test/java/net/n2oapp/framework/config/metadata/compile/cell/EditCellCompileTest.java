package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.cell.EditCell;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v3.EditCellElementIOv3;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.control.InputTextCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тест компиляции редактируемой ячейки
 */
class EditCellCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack(), new N2oActionsPack());
        builder.ios(new EditCellElementIOv3(), new InputTextIOv3());
        builder.compilers(new EditCellCompiler(), new InputTextCompiler());
    }

    @Test
    void testCompileEditCell() {
        PageContext context = new PageContext("testEditCell", "main/:id/open");
        context.setParentModelLinks(Collections.singletonList(new ModelLink(ReduxModelEnum.resolve, "main")));
        Map<String, ModelLink> mapping = new HashMap<>();
        mapping.put("id", new ModelLink(ReduxModelEnum.resolve, "main", "id"));
        context.setPathRouteMapping(mapping);
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testEditCell.page.xml",
                "net/n2oapp/framework/config/metadata/compile/cell/testEditCell.object.xml")
                .get(context);
        Table table = (Table) page.getWidget();
        EditCell cell = (EditCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("EditableCell"));
        assertThat(cell.getFormat(), is("formatTest"));
        assertThat(cell.getEnabled(), is(true));

        assertThat(cell.getControl(), notNullValue());
        assertThat(cell.getControl(), instanceOf(InputText.class));
        assertThat(cell.getControl().getSrc(), is("InputText"));
        assertThat(((InputText) cell.getControl()).getId(), is("test1"));

        assertThat(cell.getAction(), notNullValue());
        assertThat(((InvokeAction) cell.getAction()).getPayload().getDataProvider().getUrl(),
                is("n2o/data/main/:id/open/actionTest"));
        assertThat(((InvokeAction) cell.getAction()).getPayload().getDataProvider().getPathMapping().get("id"),
                is(new ModelLink(ReduxModelEnum.resolve, "main", "id")));

        cell = (EditCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getSrc(), is("EditableCell"));
        assertThat(cell.getEnabled(), is(false));

        assertThat(cell.getControl(), notNullValue());
        assertThat(cell.getControl(), instanceOf(InputText.class));
        assertThat(cell.getControl().getSrc(), is("InputText"));
        assertThat(((InputText) cell.getControl()).getId(), is("test2"));
    }
}
