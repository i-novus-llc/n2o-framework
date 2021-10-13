package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
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
import static org.hamcrest.Matchers.nullValue;

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
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testToolbarCell.page.xml")
                .get(new PageContext("testToolbarCell"));

        ToolbarCell toolbar = (ToolbarCell) ((TableWidgetComponent) page.getWidget().getComponent()).getCells().get(0);
        assertThat(toolbar.getId(), is("test"));
        assertThat(toolbar.getFieldKey(), is("test"));
        assertThat(toolbar.getSrc(), is("ButtonsCell"));
        assertThat(toolbar.getToolbar().get(0).getButtons().size(), is(3));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getId(), is("menuItem0"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getLabel(), is("label"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getIcon(), is("icon"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getColor(), is("danger"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getVisible(), is("`test==1`"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getConfirm().getMode(), is(ConfirmType.popover));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getConditions().get(ValidationType.enabled), nullValue());

        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getId(), is("subMenu1"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getLabel(), is("label"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getIcon(), is("icon"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getClassName(), is("class"));
        assertThat(((Submenu)toolbar.getToolbar().get(0).getButtons().get(1)).getSubMenu().get(0).getId(), is("linkAction"));
        assertThat(((Submenu)toolbar.getToolbar().get(0).getButtons().get(1)).getSubMenu().get(0).getConfirm().getMode(), is(ConfirmType.modal));
        assertThat(((Submenu)toolbar.getToolbar().get(0).getButtons().get(1)).getSubMenu().get(0).getVisible(), is("`test==1`"));

        assertThat(toolbar.getToolbar().get(0).getButtons().get(2).getVisible(), is(false));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(2).getEnabled(), is(false));

        toolbar = (ToolbarCell) ((TableWidgetComponent) page.getWidget().getComponent()).getCells().get(1);
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getId(), is("update"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getLabel(), is("Изменить"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getIcon(), is("fa fa-pencil"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(0).getColor(), is("link"));

        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getId(), is("delete"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getLabel(), is("Удалить"));
        assertThat(toolbar.getToolbar().get(0).getButtons().get(1).getColor(), is("link"));
    }

}
