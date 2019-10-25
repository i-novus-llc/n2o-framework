package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.toolbar.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ToolbarCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/mapping/testToolbarCell.object.xml"));
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack());
    }

    @Test
    public void testToolbarCell() {
        Page page = compile("net/n2oapp/framework/config/mapping/testToolbarCell.page.xml")
                .get(new PageContext("testToolbarCell"));

        ToolbarCell toolbar = (ToolbarCell) ((TableWidgetComponent) page.getWidgets()
                .get("testToolbarCell_main").getComponent()).getCells().get(0);
        assertThat(toolbar.getId(), is("test"));
        assertThat(toolbar.getFieldKey(), is("test"));
        assertThat(toolbar.getSrc(), is("ButtonsCell"));
        assertThat(toolbar.getToolbar().get(0).getButtons().size(), is(2));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getId(), is("menuItem0"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getLabel(), is("label"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getIcon(), is("icon"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getColor(), is("danger"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getVisible(), is("`test==1`"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getConfirm().getMode(), is(ConfirmType.popover));
        assertThat(toolbar.getActions().get("menuItem0").getId(), is("menuItem0"));
        assertThat(((LinkAction) toolbar.getActions().get("menuItem0")).getOptions().getPath(), is("https://www.google.com/"));
        assertThat(((LinkAction) toolbar.getActions().get("menuItem0")).getOptions().getTarget(), is(Target.self));
        BindLink link = ((LinkAction) toolbar.getActions().get("menuItem0")).getOptions().getQueryMapping().get("q");
        assertThat(link.getBindLink(), is("models.resolve['testToolbarCell_main']"));
        assertThat(link.getFieldValue(), is("test"));

        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getId(), is("subMenu1"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getLabel(), is("label"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getIcon(), is("icon"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getSubMenu().get(0).getId(), is("linkAction"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getSubMenu().get(0).getConfirm().getMode(), is(ConfirmType.modal));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getSubMenu().get(0).getVisible(), is("`test==1`"));
        assertThat(toolbar.getActions().get("linkAction").getId(), is("linkAction"));
        assertThat(toolbar.getActions().get("linkAction").getSrc(), is("link"));
        assertThat(toolbar.getActions().get("linkAction").getSrc(), is("link"));
        assertThat(((LinkAction) toolbar.getActions().get("linkAction")).getOptions().getPath(), is("https://www.google.com/"));
        assertThat(((LinkAction) toolbar.getActions().get("linkAction")).getOptions().getTarget(), is(Target.self));


        toolbar = (ToolbarCell) ((TableWidgetComponent) page.getWidgets()
                .get("testToolbarCell_main").getComponent()).getCells().get(1);
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getId(), is("update"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getLabel(), is("Изменить"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getIcon(), is("fa fa-pencil"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getColor(), is("link"));
        assertThat(toolbar.getActions().get("update").getId(), is("update"));
        assertThat(((ShowModal) toolbar.getActions().get("update")).getObjectId(), is("testToolbarCell"));
        assertThat(((ShowModal) toolbar.getActions().get("update")).getOperationId(), is("update"));

        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getId(), is("delete"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getLabel(), is("Удалить"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getColor(), is("link"));
        assertThat(toolbar.getActions().get("delete").getId(), is("delete"));
        assertThat(((InvokeAction) toolbar.getActions().get("delete")).getObjectId(), is("testToolbarCell"));
        assertThat(((InvokeAction) toolbar.getActions().get("delete")).getOperationId(), is("delete"));
    }

}
