package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.meta.action.Perform;
import net.n2oapp.framework.api.metadata.meta.action.PerformActionPayload;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
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
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbar.page.xml")
                .get(new PageContext("testToolbar"));
        Form f = (Form) page.getWidget();

        assertThat(f.getToolbar().size(), is(2));

        AbstractButton b1 = f.getToolbar().get("topLeft").get(0).getButtons().get(0);
        assertThat(b1.getId(), is("testId1"));
        assertThat(b1.getRounded(), is(true));
        assertThat(b1.getAction(), notNullValue());
        assertThat(b1.getConditions().get(ValidationType.enabled).size(), is(1));
        assertThat(b1.getConditions().get(ValidationType.enabled).get(0).getExpression(), is("!_.isEmpty(this)"));
        assertThat(b1.getConditions().get(ValidationType.enabled).get(0).getModelLink(), is("models.resolve['testToolbar_main']"));

        AbstractButton b2 = f.getToolbar().get("bottomLeft").get(0).getButtons().get(0);
        assertThat(b2.getId(), is("testId2"));
        assertThat(b2.getRounded(), is(false));
        assertThat(b2.getAction(), notNullValue());
        assertThat(b2.getLabel(), is("Label1"));
        assertThat(b2.getConditions().get(ValidationType.enabled), nullValue());

        AbstractButton b3 = f.getToolbar().get("bottomLeft").get(0).getButtons().get(1);
        assertThat(b3.getId(), is("testId3"));
        assertThat(f.getToolbar().getButton("testId3"), notNullValue());
        assertThat(b3.getConditions().get(ValidationType.enabled).size(), is(1));
        assertThat(b3.getConfirm().getMode(), is(ConfirmType.popover));
        assertThat(b3.getConfirm().getModelLink(), is("models.resolve['testToolbar_main']"));
        assertThat(b3.getConfirm().getText(), is("`'Test ' + this.test + ' Test'`"));
        assertThat(b3.getSrc(), is("StandardButton"));
        assertThat(((PerformButton)b3).getUrl(), is("http://example.com"));
        assertThat(((PerformButton)b3).getTarget(), is(Target.self));

        AbstractButton b4 = f.getToolbar().get("topLeft").get(0).getButtons().get(1);
        assertThat(b4.getId(), is("testId4"));
        assertThat(b4.getValidate(), is("widget"));
        assertThat(b4.getValidateWidgetId(), is("testToolbar_testWidgetId"));

        AbstractButton b5 = f.getToolbar().get("topLeft").get(0).getButtons().get(2);
        assertThat(b5.getId(), is("testId5"));
        assertThat(b5.getValidateWidgetId(), is("testToolbar_main"));

        AbstractButton b6 = f.getToolbar().get("topLeft").get(0).getButtons().get(3);
        assertThat(b6.getId(), is("testId6"));
        assertThat(b6.getValidate(), is("page"));
        assertThat(b6.getValidatePageId(), is("testToolbar"));

        AbstractButton b7 = f.getToolbar().get("topLeft").get(0).getButtons().get(4);
        assertThat(b7.getId(), is("testId7"));
        assertThat(b7.getSrc(), is("MyCustomButton"));
        assertThat(b7.getAction(), notNullValue());
        Perform performAction = (Perform)b7.getAction();
        assertThat(performAction.getType(), is("n2o/custom/ACTION"));
        assertThat(performAction.getPayload(), notNullValue());
        assertThat(((PerformActionPayload)performAction.getPayload()).getParams().size(), is(1));
        assertThat(((PerformActionPayload)performAction.getPayload()).getParams().get("prop2"), is("value2"));
    }

    @Test
    public void testToolbarMenuItem() {
        Form f = (Form) ((SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbar.page.xml")
                .get(new PageContext("testToolbar"))).getWidget();

        assertThat(f.getToolbar().size(), is(2));
        Submenu button = (Submenu)f.getToolbar().get("bottomLeft").get(0).getButtons().get(2);
        PerformButton item = button.getSubMenu().get(0);
        assertThat(item.getId(), is("tesId10"));
        assertThat(item.getConfirm(), notNullValue());
        assertThat(item.getConfirm().getMode(), is(ConfirmType.modal));
        assertThat(item.getConfirm().getModelLink(), is("models.resolve['testToolbar_main']"));
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
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(1).getSrc(), is("ToggleColumn"));
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(2).getHint(), is("Обновить данные"));
        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(3).getSrc(), is("ChangeSize"));

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(4));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(0).getHint(), is("Изменить видимость фильтров"));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(1).getSrc(), is("ToggleColumn"));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(2).getHint(), is("Обновить данные"));
        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().get(3).getSrc(), is("ChangeSize"));

        assertThat(t.getToolbar().get("bottomLeft").get(0).getButtons().size(), is(3));
        assertThat(t.getToolbar().get("bottomLeft").get(1).getButtons().size(), is(1));
        assertThat(((Submenu)t.getToolbar().get("bottomLeft").get(1).getButtons().get(0)).getSubMenu().size(), is(4));
    }
}
