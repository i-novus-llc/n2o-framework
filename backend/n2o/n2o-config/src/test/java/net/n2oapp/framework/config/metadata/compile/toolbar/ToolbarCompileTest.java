package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

class ToolbarCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
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
    void testToolbarGrouping() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testToolbarGrouping.page.xml")
                .get(new PageContext("testToolbarGrouping"));
        List<Group> groupList = page.getWidget().getToolbar().get("topLeft");
        assertThat(groupList.size(), is(3));
        assertThat(groupList.get(0).getButtons().get(0).getId(), is("beforeGroup"));
        assertThat(groupList.get(1).getButtons().get(0).getId(), is("firstInGroup"));
        assertThat(groupList.get(1).getButtons().get(1).getId(), is("secondInGroup"));
        assertThat(groupList.get(2).getButtons().get(0).getId(), is("firstAfterGroup"));
        assertThat(groupList.get(2).getButtons().get(1).getId(), is("secondAfterGroup"));


        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.api.toolbar.grouping", "false");

        page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testToolbarGrouping.page.xml")
                .get(new PageContext("testToolbarGrouping"));
        groupList = page.getWidget().getToolbar().get("topLeft");
        assertThat(groupList.size(), is(4));
        assertThat(groupList.get(0).getButtons().get(0).getId(), is("beforeGroup"));
        assertThat(groupList.get(1).getButtons().get(0).getId(), is("firstInGroup"));
        assertThat(groupList.get(1).getButtons().get(1).getId(), is("secondInGroup"));
        assertThat(groupList.get(2).getButtons().get(0).getId(), is("firstAfterGroup"));
        assertThat(groupList.get(3).getButtons().get(0).getId(), is("secondAfterGroup"));
    }

    @Test
    void testToolbar() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbar.page.xml")
                .get(new PageContext("testToolbar"));
        Form f = (Form) page.getWidget();

        assertThat(f.getToolbar().size(), is(2));
        assertThat(f.getToolbar().getGroup(0).getClassName(), is("my-toolbar"));
        assertThat(f.getToolbar().getGroup(0).getStyle().get("color"), is("red"));

        AbstractButton b1 = f.getToolbar().get("topLeft").get(0).getButtons().get(0);
        assertThat(b1.getId(), is("testId1"));
        assertThat(b1.getRounded(), is(true));
        assertThat(b1.getAction(), notNullValue());
        assertThat(b1.getConditions().get(ValidationTypeEnum.enabled).size(), is(1));
        assertThat(b1.getConditions().get(ValidationTypeEnum.enabled).get(0).getExpression(), is("!$.isEmptyModel(this)"));
        assertThat(b1.getConditions().get(ValidationTypeEnum.enabled).get(0).getModelLink(), is("models.resolve['testToolbar_w1']"));

        AbstractButton b2 = f.getToolbar().get("bottomLeft").get(0).getButtons().get(0);
        assertThat(b2.getId(), is("testId2"));
        assertThat(b2.getRounded(), is(false));
        assertThat(b2.getAction(), notNullValue());
        assertThat(b2.getLabel(), is("Label1"));
        assertThat(b2.getConditions().get(ValidationTypeEnum.enabled), nullValue());

        AbstractButton b3 = f.getToolbar().get("bottomLeft").get(0).getButtons().get(1);
        assertThat(b3.getId(), is("testId3"));
        assertThat(f.getToolbar().getButton("testId3"), notNullValue());
        assertThat(b3.getConditions().get(ValidationTypeEnum.enabled).size(), is(1));
        assertThat(b3.getSrc(), is("StandardButton"));
        assertThat(((LinkAction) b3.getAction()).getUrl(), is("http://example.com"));
        assertThat(((LinkAction) b3.getAction()).getTarget(), is(TargetEnum.self));

        AbstractButton b7 = f.getToolbar().get("topLeft").get(0).getButtons().get(1);
        assertThat(b7.getId(), is("testId4"));
        assertThat(b7.getSrc(), is("MyCustomButton"));
        assertThat(b7.getAction(), notNullValue());
        CustomAction performAction = (CustomAction) b7.getAction();
        assertThat(performAction.getType(), is("n2o/custom/ACTION"));
        assertThat(performAction.getPayload(), notNullValue());
        assertThat(performAction.getPayload().getAttributes().size(), is(1));
        assertThat(performAction.getPayload().getAttributes().get("prop2"), is("value2"));
    }

    @Test
    void testToolbarMenuItem() {
        Form f = (Form) ((SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testToolbar.page.xml")
                .get(new PageContext("testToolbar"))).getWidget();

        assertThat(f.getToolbar().size(), is(2));
        Submenu button = (Submenu) f.getToolbar().get("bottomLeft").get(0).getButtons().get(2);
        PerformButton item = button.getSubMenu().get(0);
        assertThat(item.getId(), is("tesId10"));
    }

    @Test
    void generatedButtonsComeAfterButtonsInToolbar() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generatedButtonsComeAfterButtonsInToolbar.page.xml")
                .get(new PageContext("generatedButtonsComeAfterButtonsInToolbar"));
        Toolbar toolbar = page.getWidget().getToolbar();
        assertThat(toolbar.getGroups().get(0).getButtons().get(0).getLabel(), is("Сохранить"));
        assertThat(toolbar.getGroups().get(1).getButtons().get(0).getLabel(), is("Закрыть"));
    }
}