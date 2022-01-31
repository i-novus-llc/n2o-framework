package net.n2oapp.framework.config.metadata.compile.dependency;

import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции зависимости между полем и кнопками
 */
public class ButtonDependencyCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testJsonForm.object.xml"));
    }

    @Test
    public void testButtonDependency() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependency.page.xml")
                .get(new PageContext("testButtonDependency"));
        List<AbstractButton> buttons = ((Widget) ((TabsRegion) page.getRegions().get("single").get(0)).getItems().get(0).getContent().get(0))
                .getToolbar().get("topLeft").get(0).getButtons();
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(0).getConditions().get(ValidationType.visible).get(0).getModelLink(),
                is("models.resolve['testButtonDependency_table']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(1).getConditions().get(ValidationType.enabled).get(0).getModelLink(),
                is("models.resolve['testButtonDependency_table']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(2).getConditions().get(ValidationType.visible).get(0).getModelLink(),
                is("models.resolve['testButtonDependency_table']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(2).getConditions().get(ValidationType.visible).get(0).getModelLink(),
                is("models.resolve['testButtonDependency_table']"));

        assertThat(buttons.get(1).getVisible(), is(false));
        assertThat(buttons.get(1).getEnabled(), is(false));
        assertThat(buttons.get(2).getVisible(), nullValue());
        assertThat(buttons.get(2).getEnabled(), nullValue());


        Condition condition = buttons.get(2).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("property1"));
        // if disable-on-empty-model = false, should not have contains enabled !_.isEmpty(this) condition
        condition = buttons.get(2).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("property2"));
        // if disable-on-empty-model = auto, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(3).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.multi['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        // if disable-on-empty-model = true, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(4).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.multi['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));

        condition = buttons.get(5).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.filter['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("property1"));
        condition = buttons.get(6).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.filter['testButtonDependency_test']"));
        assertThat(condition.getExpression(), is("a==b"));
        condition = buttons.get(6).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        condition = buttons.get(6).getConditions().get(ValidationType.enabled).get(1);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("c==d"));
        assertThat(condition.getMessage(), is("Не указана дата"));

        List<PerformButton> submenu = ((Submenu) buttons.get(7)).getSubMenu();
        assertThat(submenu.get(0).getVisible(), is(false));
        assertThat(submenu.get(0).getEnabled(), is(false));
        assertThat(submenu.get(1).getVisible(), nullValue());
        assertThat(submenu.get(1).getEnabled(), nullValue());
        condition = submenu.get(1).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("property1"));
        condition = submenu.get(1).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        condition = submenu.get(1).getConditions().get(ValidationType.enabled).get(1);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("property2"));
        condition = submenu.get(2).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.filter['testButtonDependency_test']"));
        assertThat(condition.getExpression(), is("a==b"));
        assertThat(submenu.get(2).getConditions().get(ValidationType.visible).size(), is (1));
        condition = submenu.get(2).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        condition = submenu.get(2).getConditions().get(ValidationType.enabled).get(1);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependency_table']"));
        assertThat(condition.getExpression(), is("c==d"));
        assertThat(condition.getMessage(), is("Не указана дата"));
    }

    @Test
    public void testButtonDependencyWithDatasource() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependencyWithDatasource.page.xml")
                .get(new PageContext("testButtonDependencyWithDatasource"));
        List<AbstractButton> buttons = ((Widget) ((TabsRegion) page.getRegions().get("single").get(0)).getItems().get(0).getContent().get(0))
                .getToolbar().get("topLeft").get(0).getButtons();
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(0).getConditions().get(ValidationType.visible).get(0).getModelLink(),
                is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(1).getConditions().get(ValidationType.enabled).get(0).getModelLink(),
                is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(2).getConditions().get(ValidationType.visible).get(0).getModelLink(),
                is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(((Submenu) buttons.get(0)).getSubMenu().get(2).getConditions().get(ValidationType.visible).get(0).getModelLink(),
                is("models.resolve['testButtonDependencyWithDatasource_table']"));

        assertThat(buttons.get(1).getVisible(), is(false));
        assertThat(buttons.get(1).getEnabled(), is(false));
        assertThat(buttons.get(2).getVisible(), nullValue());
        assertThat(buttons.get(2).getEnabled(), nullValue());


        Condition condition = buttons.get(2).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("property1"));
        // if disable-on-empty-model = false, should not have contains enabled !_.isEmpty(this) condition
        condition = buttons.get(2).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("property2"));
        // if disable-on-empty-model = auto, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(3).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.multi['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        // if disable-on-empty-model = true, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(4).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.multi['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));

        condition = buttons.get(5).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.filter['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("property1"));
        condition = buttons.get(6).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.filter['testButtonDependencyWithDatasource_test']"));
        assertThat(condition.getExpression(), is("a==b"));
        condition = buttons.get(6).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        condition = buttons.get(6).getConditions().get(ValidationType.enabled).get(1);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("c==d"));
        assertThat(condition.getMessage(), is("Не указана дата"));

        List<PerformButton> submenu = ((Submenu) buttons.get(7)).getSubMenu();
        assertThat(submenu.get(0).getVisible(), is(false));
        assertThat(submenu.get(0).getEnabled(), is(false));
        assertThat(submenu.get(1).getVisible(), nullValue());
        assertThat(submenu.get(1).getEnabled(), nullValue());
        condition = submenu.get(1).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("property1"));
        condition = submenu.get(1).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        condition = submenu.get(1).getConditions().get(ValidationType.enabled).get(1);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("property2"));
        condition = submenu.get(2).getConditions().get(ValidationType.visible).get(0);
        assertThat(condition.getModelLink(), is("models.filter['testButtonDependencyWithDatasource_test']"));
        assertThat(condition.getExpression(), is("a==b"));
        assertThat(submenu.get(2).getConditions().get(ValidationType.visible).size(), is (1));
        condition = submenu.get(2).getConditions().get(ValidationType.enabled).get(0);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("!_.isEmpty(this)"));
        condition = submenu.get(2).getConditions().get(ValidationType.enabled).get(1);
        assertThat(condition.getModelLink(), is("models.resolve['testButtonDependencyWithDatasource_table']"));
        assertThat(condition.getExpression(), is("c==d"));
        assertThat(condition.getMessage(), is("Не указана дата"));
    }
}
