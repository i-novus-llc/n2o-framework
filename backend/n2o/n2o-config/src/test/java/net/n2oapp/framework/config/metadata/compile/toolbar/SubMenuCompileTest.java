package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции кнопки с выпадающим меню
 */
class SubMenuCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack(), new N2oPagesPack(), new N2oRegionsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    void testSubMenu() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/testSubMenu.page.xml")
                .get(new PageContext("testSubMenu"));

        Toolbar toolbar = ((Table<?>) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar();
        Submenu subMenu = (Submenu) toolbar.getButton("testSubMenu_mi0");
        assertThat(subMenu, allOf(
                hasProperty("src", is("DropdownButton")),
                hasProperty("showToggleIcon", is(false)),
                hasProperty("visible", is(false)),
                hasProperty("enabled", is(false))
        ));

        List<PerformButton> items = subMenu.getContent();
        assertThat(items.size(), is(2));
        PerformButton createBtn = items.get(0);
        assertThat(createBtn, allOf(
                hasProperty("id", is("create")),
                hasProperty("label", is("Создать")),
                hasProperty("action", instanceOf(ShowModal.class))
        ));

        PerformButton updateBtn = items.get(1);
        assertThat(updateBtn, allOf(
                hasProperty("id", is("update")),
                hasProperty("label", is("Изменить")),
                hasProperty("action", instanceOf(ShowModal.class))
        ));
        assertThat(updateBtn.getConditions().get(ValidationTypeEnum.ENABLED).size(), is(1));

        subMenu = (Submenu) toolbar.getButton("testSubMenu_mi3");
        assertThat(subMenu.getShowToggleIcon(), is(true));
        assertThat(subMenu.getContent(), nullValue());
        checkCondition(subMenu.getConditions(), "name != null", "models.filter['testSubMenu_form']");

        subMenu = (Submenu) toolbar.getButton("testSubMenu_mi4");
        checkCondition(subMenu.getConditions(), "name != null", "models.resolve['testSubMenu_table']");

        subMenu = (Submenu) toolbar.getButton("testSubMenu_mi5");
        assertThat(subMenu.getIcon(), is("fa fa-plus"));
        assertThat(subMenu.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(subMenu.getContent().get(0).getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(subMenu.getContent().get(0).getDatasource(), is("testSubMenu_table"));
        assertThat(subMenu.getContent().get(0).getIcon(), is("fa fa-pencil"));
        assertThat(subMenu.getContent().get(0).getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(subMenu.getContent().get(1).getModel(), is(ReduxModelEnum.RESOLVE));
        assertThat(subMenu.getContent().get(1).getDatasource(), is("testSubMenu_ds"));

        assertThat(subMenu.getContent().size(), is(3));
        assertThat(subMenu.getContent().get(2), allOf(
                hasProperty("id", is("close")),
                hasProperty("label", is("Закрыть")),
                hasProperty("action", instanceOf(LinkAction.class))
        ));
    }

    private static void checkCondition(Map<ValidationTypeEnum, List<Condition>> conditions, String expression, String modelLink) {
        assertThat(conditions.get(ValidationTypeEnum.ENABLED).size(), is(1));
        assertThat(conditions.get(ValidationTypeEnum.VISIBLE).size(), is(1));
        Condition condition = conditions.get(ValidationTypeEnum.ENABLED).get(0);
        assertThat(condition.getExpression(), is(expression));
        assertThat(condition.getModelLink(), is(modelLink));
        condition = conditions.get(ValidationTypeEnum.VISIBLE).get(0);
        assertThat(condition.getExpression(), is(expression));
        assertThat(condition.getModelLink(), is(modelLink));
    }
}
