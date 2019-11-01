package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ToolbarCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oPagesPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oRegionsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.widget.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"));
    }

    @Test
    public void testToolbarGrouping() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testToolbarGrouping.widget.xml")
                .get(new WidgetContext("testToolbarGrouping"));
        List<Group> groupList = form.getToolbar().get("topLeft");
        assertThat(groupList.size(), is(3));
        assertThat(groupList.get(0).getButtons().get(0).getId(), is("beforeGroup"));
        assertThat(groupList.get(1).getButtons().get(0).getId(), is("firstInGroup"));
        assertThat(groupList.get(1).getButtons().get(1).getId(), is("secondInGroup"));
        assertThat(groupList.get(2).getButtons().get(0).getId(), is("firstAfterGroup"));
        assertThat(groupList.get(2).getButtons().get(1).getId(), is("secondAfterGroup"));


        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.api.toolbar.grouping", "false");

        form = (Form) compile("net/n2oapp/framework/config/metadata/compile/widgets/testToolbarGrouping.widget.xml")
                .get(new WidgetContext("testToolbarGrouping"));
        groupList = form.getToolbar().get("topLeft");
        assertThat(groupList.size(), is(4));
        assertThat(groupList.get(0).getButtons().get(0).getId(), is("beforeGroup"));
        assertThat(groupList.get(1).getButtons().get(0).getId(), is("firstInGroup"));
        assertThat(groupList.get(1).getButtons().get(1).getId(), is("secondInGroup"));
        assertThat(groupList.get(2).getButtons().get(0).getId(), is("firstAfterGroup"));
        assertThat(groupList.get(3).getButtons().get(0).getId(), is("secondAfterGroup"));
    }

    @Test
    public void testToolbar() {
        Form f = (Form) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbar.widget.xml")
                .get(new WidgetContext("testToolbar"));

        assertThat(f.getToolbar().size(), is(2));

        Button b1 = f.getToolbar().get("topLeft").get(0).getButtons().get(0);
        assertThat(b1.getId(), is("testId1"));
        assertThat(b1.getActionId(), is("testActionId1"));
        assertThat(b1.getConditions().get(ValidationType.enabled).size(), is(1));
        assertThat(b1.getConditions().get(ValidationType.enabled).get(0).getExpression(), is("!_.isEmpty(this)"));
        assertThat(b1.getConditions().get(ValidationType.enabled).get(0).getModelLink(), is("models.resolve['$testToolbar']"));

        Button b2 = f.getToolbar().get("bottomLeft").get(0).getButtons().get(0);
        assertThat(b2.getId(), is("testId2"));
        assertThat(b2.getActionId(), is("testActionId1"));
        assertThat(b2.getLabel(), is("Label1"));
        assertThat(b2.getConditions().get(ValidationType.enabled), nullValue());

        Button b3 = f.getToolbar().get("bottomLeft").get(0).getButtons().get(1);
        assertThat(b3.getId(), is("testId3"));
        assertThat(b3.getActionId(), is("testId3"));
        assertThat(f.getActions().containsKey("testId3"), is(true));
        assertThat(b3.getConditions().get(ValidationType.enabled).size(), is(1));
        assertThat(b3.getConfirm().getMode(), is(ConfirmType.popover));
        assertThat(b3.getConfirm().getModelLink(), is("models.resolve['$testToolbar']"));
        assertThat(b3.getConfirm().getText(), is("`'Test ' + this.test + ' Test'`"));

        Button b4 = f.getToolbar().get("topLeft").get(0).getButtons().get(1);
        assertThat(b4.getId(), is("testId4"));
        assertThat(b4.getValidatedWidgetId(), is("testWidgetId"));

        Button b5 = f.getToolbar().get("topLeft").get(0).getButtons().get(2);
        assertThat(b5.getId(), is("testId5"));
        assertThat(b5.getValidatedWidgetId(), is("$testToolbar"));
    }

    @Test
    public void testToolbarMenuItem() {
        Form f = (Form) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbar.widget.xml")
                .get(new WidgetContext("testToolbar"));

        assertThat(f.getToolbar().size(), is(2));
        Button button = f.getToolbar().get("bottomLeft").get(0).getButtons().get(2);
        MenuItem item = button.getSubMenu().get(0);
        assertThat(item.getId(), is("tesId10"));
        assertThat(item.getConfirm(), notNullValue());
        assertThat(item.getConfirm().getMode(), is(ConfirmType.modal));
        assertThat(item.getConfirm().getModelLink(), is("models.resolve['$testToolbar']"));
        assertThat(item.getConfirm().getText(), is("`'Test ' + this.test + ' Test'`"));
    }


    @Test
    public void testGenerate() {
        Table t = (Table) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbarGenerate.widget.xml")
                .get(new WidgetContext("testToolbarGenerate"));

        assertThat(t.getToolbar().size(), is(4));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().size(), is(3));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(0).getId(), is("create"));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(1).getId(), is("update"));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(2).getId(), is("delete"));

        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().size(), is(4));
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(0).getHint(), is("Изменить видимость фильтров"));
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(1).getDropdownSrc(), is("ToggleColumn"));
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(2).getHint(), is("Обновить данные"));
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(3).getDropdownSrc(), is("ChangeSize"));

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(4));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(0).getHint(), is("Изменить видимость фильтров"));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(1).getDropdownSrc(), is("ToggleColumn"));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(2).getHint(), is("Обновить данные"));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(3).getDropdownSrc(), is("ChangeSize"));

        assertThat(t.getToolbar().get("bottomLeft").get(0).getButtons().size(), is(3));
        assertThat(t.getToolbar().get("bottomLeft").get(1).getButtons().size(), is(1));
        assertThat(t.getToolbar().get("bottomLeft").get(1).getButtons().get(0).getSubMenu().size(), is(4));
    }
}
