package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.CopyActionElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Проверка копиляции действия copy
 */
public class CopyActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oAllDataPack(), new N2oFieldSetsPack());
        builder.ios(new CopyActionElementIOV1());
        builder.compilers(new CopyActionCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"));
    }

    @Test
    public void testCopyAction() {
        ModalPageContext modalPageContext = new ModalPageContext("testCopyAction", "/modal");
        HashMap<String, String> parentWidgetIdDatasourceMap = new HashMap<>();
        parentWidgetIdDatasourceMap.put("page_form", "page_form_ds");
        modalPageContext.setParentWidgetIdDatasourceMap(parentWidgetIdDatasourceMap);
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testCopyAction.page.xml")
                .get(modalPageContext);

        Form table = (Form) page.getRegions().get("single").get(0).getContent().get(0);

        CopyAction action = (CopyAction) ((Submenu)table.getToolbar().getGroup(0).getButtons().get(0)).getSubMenu().get(0).getAction();
        assertThat(action.getType(), is("n2o/models/COPY"));
        assertThat(action.getPayload().getSource().getKey(), is("modal_table1"));
        assertThat(action.getPayload().getSource().getField(), nullValue());
        assertThat(action.getPayload().getSource().getPrefix(), is("edit"));
        assertThat(action.getPayload().getTarget().getKey(), is("modal_table1"));
        assertThat(action.getPayload().getTarget().getField(), nullValue());
        assertThat(action.getPayload().getTarget().getPrefix(), is("filter"));
        assertThat(action.getPayload().getMode(), is(CopyMode.merge));
        assertThat(action.getMeta().getModalsToClose(), is(1));

        action = (CopyAction) table.getToolbar().getButton("btn").getAction();
        assertThat(action.getType(), is("n2o/models/COPY"));
        assertThat(action.getPayload().getSource().getPrefix(), is(ReduxModel.EDIT.getId()));
        assertThat(action.getPayload().getSource().getKey(), is("modal_table1"));
        assertThat(action.getPayload().getSource().getField(), is("id"));
        assertThat(action.getPayload().getTarget().getPrefix(), is(ReduxModel.EDIT.getId()));
        assertThat(action.getPayload().getTarget().getKey(), is("modal_table2"));
        assertThat(action.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(action.getPayload().getMode(), is(CopyMode.replace));
        assertThat(action.getMeta().getModalsToClose(), is(1));

        action = (CopyAction) page.getToolbar().getButton("menuItem0").getAction();
        assertThat(action.getPayload().getSource().getKey(), is("modal_table1"));
        assertThat(action.getPayload().getTarget().getKey(), is("page_form_ds"));
    }

    @Test
    public void testInitWidgetIdWithoutPage() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/action/testCopyAction.widget.xml")
                .get(new WidgetContext("testCopyAction"));

        CopyAction action = (CopyAction) ((Submenu)table.getToolbar().getButton("subMenu0")).getSubMenu().get(0).getAction();
        assertThat(action.getPayload().getSource().getKey(), is("$testCopyAction"));
        assertThat(action.getPayload().getTarget().getKey(), is("$testCopyAction"));

        action = (CopyAction) table.getToolbar().getButton("btn").getAction();
        assertThat(action.getPayload().getSource().getKey(), is("table"));
        assertThat(action.getPayload().getTarget().getKey(), is("form"));
    }
}