package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
                new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"));
    }

    @Test
    public void testCopyAction() {
        ModalPageContext modalPageContext = new ModalPageContext("testCopyAction", "/modal");
        HashMap<String, String> parentWidgetIdDatasourceMap = new HashMap<>();
        parentWidgetIdDatasourceMap.put("page_form", "page_form");
        modalPageContext.setParentWidgetIdDatasourceMap(parentWidgetIdDatasourceMap);
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/copy/testCopyAction.page.xml")
                .get(modalPageContext);

        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);

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
        assertThat(action.getPayload().getSource().getPrefix(), is(ReduxModel.edit.getId()));
        assertThat(action.getPayload().getSource().getKey(), is("modal_table1"));
        assertThat(action.getPayload().getSource().getField(), is("id"));
        assertThat(action.getPayload().getTarget().getPrefix(), is(ReduxModel.edit.getId()));
        assertThat(action.getPayload().getTarget().getKey(), is("modal_table2"));
        assertThat(action.getPayload().getTarget().getField(), is("dictionary.id"));
        assertThat(action.getPayload().getMode(), is(CopyMode.replace));
        assertThat(action.getMeta().getModalsToClose(), is(1));

        action = (CopyAction) page.getToolbar().getButton("pageBtn").getAction();
        assertThat(action.getPayload().getSource().getKey(), is("modal_table1"));
        assertThat(action.getPayload().getTarget().getKey(), is("page_form"));
    }

    @Test
    public void copyV2() {
        PageContext pageContext = new PageContext("testCopyActionV2", "/p");
        pageContext.setParentClientPageId("page1");
        pageContext.setParentDatasourceIdsMap(Map.of("ds2", "page1_ds2"));
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/copy/testCopyActionV2.page.xml")
                .get(pageContext);

        CopyAction copyInPage = (CopyAction) page.findButton("copyInPage").getAction();
        assertThat(copyInPage.getType(), is("n2o/models/COPY"));
        assertThat(copyInPage.getPayload().getSource().getKey(), is("p_ds1"));
        assertThat(copyInPage.getPayload().getSource().getField(), nullValue());
        assertThat(copyInPage.getPayload().getSource().getPrefix(), is("resolve"));
        assertThat(copyInPage.getPayload().getTarget().getKey(), is("p_ds1"));
        assertThat(copyInPage.getPayload().getTarget().getField(), nullValue());
        assertThat(copyInPage.getPayload().getTarget().getPrefix(), is("resolve"));
        assertThat(copyInPage.getPayload().getMode(), is(CopyMode.merge));
        assertThat(copyInPage.getMeta().getModalsToClose(), is(1));

        CopyAction copyInPage2 = (CopyAction) page.findButton("copyInPage2").getAction();
        assertThat(copyInPage2.getType(), is("n2o/models/COPY"));
        assertThat(copyInPage2.getPayload().getSource().getKey(), is("p_ds1"));
        assertThat(copyInPage2.getPayload().getSource().getField(), is("sourceId"));
        assertThat(copyInPage2.getPayload().getSource().getPrefix(), is("filter"));
        assertThat(copyInPage2.getPayload().getTarget().getKey(), is("page1_ds2"));
        assertThat(copyInPage2.getPayload().getTarget().getField(), is("targetId"));
        assertThat(copyInPage2.getPayload().getTarget().getPrefix(), is("resolve"));
        assertThat(copyInPage2.getPayload().getMode(), is(CopyMode.replace));
        assertThat(copyInPage2.getMeta().getModalsToClose(), is(0));
    }

    @Test
    public void testCopyRefPageAttributes() {
        PageContext pageContext = new PageContext("testCopyRefPageAttributes", "/p");
        compile("net/n2oapp/framework/config/metadata/compile/action/copy/testCopyRefPageAttributes.page.xml",
                "net/n2oapp/framework/config/metadata/compile/action/copy/testCopyRefPageAttributesModal.page.xml")
                .get(pageContext);

        // parent -> this
        StandardPage page = (StandardPage) routeAndGet("/p/modal1", Page.class);
        CopyAction submit = (CopyAction) page.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getSource().getKey(), is("p_ds"));
        assertThat(submit.getPayload().getTarget().getKey(), is("p_modal1_ds2"));

        // this -> parent
        page = (StandardPage) routeAndGet("/p/modal2", Page.class);
        submit = (CopyAction) page.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getSource().getKey(), is("p_modal2_ds"));
        assertThat(submit.getPayload().getTarget().getKey(), is("p_ds2"));

        // parent -> this (with app-datasource)
        page = (StandardPage) routeAndGet("/p/modal3", Page.class);
        submit = (CopyAction) page.getToolbar().getButton("submit").getAction();
        assertThat(submit.getPayload().getSource().getKey(), is("appDs"));
        assertThat(submit.getPayload().getTarget().getKey(), is("appDs2"));
    }
}