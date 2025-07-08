package net.n2oapp.framework.config.metadata.compile.dependency;

import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции зависимости между полем и кнопками
 */
class ButtonDependencyCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testJsonForm.object.xml"));
    }

    @Test
    void testButtonDependency() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependency.page.xml")
                .get(new PageContext("testButtonDependency"));
        List<AbstractButton> buttons = ((Widget<?>) ((TabsRegion) page.getRegions().get("single").get(0)).getItems().get(0).getContent().get(0))
                .getToolbar().get("topLeft").get(0).getButtons();

        assertThat(buttons.get(0), allOf(
                hasProperty("visible", is(false)),
                hasProperty("enabled", is(false))
        ));
        assertThat(buttons.get(1), allOf(
                hasProperty("visible", nullValue()),
                hasProperty("enabled", nullValue())
        ));


        Condition condition = buttons.get(1).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "property1");

        // if disable-on-empty-model = false, should not have contains enabled !_.isEmpty(this) condition
        condition = buttons.get(1).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "property2");

        // if disable-on-empty-model = auto, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(2).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.multi['testButtonDependency_table']", "!$.isEmptyModel(this)");

        // if disable-on-empty-model = true, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(3).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.multi['testButtonDependency_table']", "!$.isEmptyModel(this)");

        condition = buttons.get(4).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.filter['testButtonDependency_table']", "property1");

        condition = buttons.get(5).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.filter['testButtonDependency_test']", "a==b");

        condition = buttons.get(5).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "!$.isEmptyModel(this)");

        condition = buttons.get(5).getConditions().get(ValidationTypeEnum.ENABLED).get(1);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "c==d");
        assertThat(condition.getMessage(), is("Не указана дата"));

        List<PerformButton> submenu = ((Submenu) buttons.get(6)).getContent();

        assertThat(submenu.get(0), allOf(
                hasProperty("visible", is(false)),
                hasProperty("enabled", is(false))
        ));
        assertThat(submenu.get(1), allOf(
                hasProperty("visible", nullValue()),
                hasProperty("enabled", nullValue())
        ));

        condition = submenu.get(1).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "property1");

        condition = submenu.get(1).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "!$.isEmptyModel(this)");

        condition = submenu.get(1).getConditions().get(ValidationTypeEnum.ENABLED).get(1);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "property2");

        condition = submenu.get(2).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.filter['testButtonDependency_test']", "a==b");
        assertThat(submenu.get(2).getConditions().get(ValidationTypeEnum.VISIBLE).size(), is(1));

        condition = submenu.get(2).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "!$.isEmptyModel(this)");

        condition = submenu.get(2).getConditions().get(ValidationTypeEnum.ENABLED).get(1);
        checkCondition(condition, "models.resolve['testButtonDependency_table']", "c==d");
        assertThat(condition.getMessage(), is("Не указана дата"));
    }

    private static void checkCondition(Condition condition, String value, String property1) {
        assertThat(condition.getModelLink(), is(value));
        assertThat(condition.getExpression(), is(property1));
    }

    @Test
    void testButtonDependencyWithDatasource() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/dependency/testButtonDependencyWithDatasource.page.xml")
                .get(new PageContext("testButtonDependencyWithDatasource"));
        List<AbstractButton> buttons = ((Widget<?>) ((TabsRegion) page.getRegions().get("single").get(0)).getItems().get(0).getContent().get(0))
                .getToolbar().get("topLeft").get(0).getButtons();

        assertThat(buttons.get(0), allOf(
                hasProperty("visible", is(false)),
                hasProperty("enabled", is(false))
        ));
        assertThat(buttons.get(1), allOf(
                hasProperty("visible", nullValue()),
                hasProperty("enabled", nullValue())
        ));


        Condition condition = buttons.get(1).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "property1");
        // if disable-on-empty-model = false, should not have contains enabled !_.isEmpty(this) condition
        condition = buttons.get(1).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "property2");
        // if disable-on-empty-model = auto, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(2).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.multi['testButtonDependencyWithDatasource_table']", "!$.isEmptyModel(this)");
        // if disable-on-empty-model = true, should have contains enabled !_.isEmpty(this) condition for MULTI model
        condition = buttons.get(3).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.multi['testButtonDependencyWithDatasource_table']", "!$.isEmptyModel(this)");

        condition = buttons.get(4).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.filter['testButtonDependencyWithDatasource_table']", "property1");
        condition = buttons.get(5).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.filter['testButtonDependencyWithDatasource_test']", "a==b");
        condition = buttons.get(5).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "!$.isEmptyModel(this)");
        condition = buttons.get(5).getConditions().get(ValidationTypeEnum.ENABLED).get(1);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "c==d");
        assertThat(condition.getMessage(), is("Не указана дата"));

        List<PerformButton> submenu = ((Submenu) buttons.get(6)).getContent();
        assertThat(submenu.get(0), allOf(
                hasProperty("visible", is(false)),
                hasProperty("enabled", is(false))
        ));
        assertThat(submenu.get(1), allOf(
                hasProperty("visible", nullValue()),
                hasProperty("enabled", nullValue())
        ));
        condition = submenu.get(1).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "property1");
        condition = submenu.get(1).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "!$.isEmptyModel(this)");
        condition = submenu.get(1).getConditions().get(ValidationTypeEnum.ENABLED).get(1);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "property2");
        condition = submenu.get(2).getConditions().get(ValidationTypeEnum.VISIBLE).get(0);
        checkCondition(condition, "models.filter['testButtonDependencyWithDatasource_test']", "a==b");
        assertThat(submenu.get(2).getConditions().get(ValidationTypeEnum.VISIBLE).size(), is(1));
        condition = submenu.get(2).getConditions().get(ValidationTypeEnum.ENABLED).get(0);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "!$.isEmptyModel(this)");
        condition = submenu.get(2).getConditions().get(ValidationTypeEnum.ENABLED).get(1);
        checkCondition(condition, "models.resolve['testButtonDependencyWithDatasource_table']", "c==d");
        assertThat(condition.getMessage(), is("Не указана дата"));
    }
}
