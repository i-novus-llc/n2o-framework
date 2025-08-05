package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.MultiColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TableSettingsCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oAllDataPack(),
                new N2oCellsPack(),
                new N2oActionsPack());
    }

    @Test
    void testColumnsTableSettingsInMultiColumn() {
        SimplePage page = (SimplePage) compile(
                "net/n2oapp/framework/config/metadata/compile/toolbar/table_settings/testColumnsTableSettings.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank2.query.xml")
                .get(new PageContext("testColumnsTableSettings"));

        Table<?> table = (Table<?>) page.getWidget();
        TableWidgetComponent.TableHeader header = table.getComponent().getHeader();

        assertThat(header.getCells(), hasSize(5));
        checkColumn((BaseColumn) header.getCells().get(0), "id", false, true);

        MultiColumn multiColumn1 = (MultiColumn) header.getCells().get(1);
        MultiColumn multiColumn2 = (MultiColumn) header.getCells().get(2);
        MultiColumn multiColumn3 = (MultiColumn) header.getCells().get(3);
        MultiColumn multiColumn4 = (MultiColumn) header.getCells().get(4);

        checkColumn(multiColumn1, "id_multi1", false, true);
        checkColumn(multiColumn1.getChildren().get(0), "name", false, true);
        checkColumn(multiColumn1.getChildren().get(1), "type", false, true);

        checkColumn(multiColumn2, "id_multi2", false, true);
        checkColumn(multiColumn2.getChildren().get(0), "status", false, true);
        checkColumn(multiColumn2.getChildren().get(1), "region", false, true);

        MultiColumn nestedMulti = (MultiColumn) multiColumn2.getChildren().get(2);
        checkColumn(nestedMulti, null, false, true);
        checkColumn(nestedMulti.getChildren().get(0), "city", false, true);
        checkColumn(nestedMulti.getChildren().get(1), "country", false, true);

        checkColumn(multiColumn3, "id_multi3", true, true);
        checkColumn(multiColumn3.getChildren().getFirst(), "address", true, true);

        checkColumn(multiColumn4, null, true, false);
        checkColumn(multiColumn4.getChildren().getFirst(), "gender", true, false);
    }

    private static void checkColumn(BaseColumn column, String id, boolean enabled, boolean visibleState) {
        if (id == null)
            assertThat(column.getId(), nullValue());
        else
            assertThat(column.getId(), is(id));
        assertThat(column.getEnabled(), is(enabled));
        assertThat(column.getVisibleState(), is(visibleState));
    }

    @Test
    void testTableSettings() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/table_settings/testTableSettings.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank2.query.xml")
                .get(new PageContext("testTableSettings"));
        Table<?> table = (Table<?>) page.getWidget();
        TableWidgetComponent.TableHeader header = table.getComponent().getHeader();
        assertThat(header.getCells().size(), Matchers.is(3));

        Toolbar toolbar = page.getWidget().getToolbar();
        List<Group> groups = toolbar.getGroups();
        assertThat(groups.size(), is(2));

        List<AbstractButton> buttons = groups.getFirst().getButtons();
        checkButtons(buttons, AbstractButton::getHint);
        assertThat(buttons.get(9).getSrc(), is("DropdownButton"));

        buttons = ((Submenu) buttons.get(9)).getContent().stream().map(AbstractButton.class::cast).toList();
        checkButtons(buttons, AbstractButton::getLabel);
        assertThat(buttons.get(9).getSrc(), is("StandardButton"));
        assertThat(buttons.get(9).getId(), is("action1"));

        buttons = groups.get(1).getButtons();
        checkButtons(buttons, AbstractButton::getHint);
        assertThat(buttons.get(9).getSrc(), is("DropdownButton"));

        buttons = ((Submenu) buttons.get(9)).getContent().stream().map(AbstractButton.class::cast).toList();
        checkButtons(buttons, AbstractButton::getLabel);
        assertThat(buttons.get(9).getSrc(), is("StandardButton"));
        assertThat(buttons.get(9).getId(), is("action2"));
    }

    private static void checkButtons(List<AbstractButton> buttons, Function<AbstractButton, String> labelExtractor) {
        assertThat(buttons.get(0).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(0)), is("Обновить"));

        assertThat(buttons.get(1).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(1)), is("Обновить"));

        assertThat(buttons.get(2).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(2)), is("Экспортировать"));

        assertThat(buttons.get(3).getSrc(), is("ToggleColumn"));
        assertThat(labelExtractor.apply(buttons.get(3)), is("Скрытие столбцов"));

        assertThat(buttons.get(4).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(4)), is("Фильтры"));

        assertThat(buttons.get(5).getLabel(), is("button1"));
        assertThat(buttons.get(5).getSrc(), is("StandardButton"));

        assertThat(buttons.get(6).getSrc(), is("ChangeSize"));
        assertThat(labelExtractor.apply(buttons.get(6)), is("Количество записей"));

        assertThat(buttons.get(7).getSrc(), is("WordWrap"));
        assertThat(labelExtractor.apply(buttons.get(7)), is("Перенос по словам"));

        assertThat(buttons.get(8).getSrc(), is("ResetSettings"));
    }
}
