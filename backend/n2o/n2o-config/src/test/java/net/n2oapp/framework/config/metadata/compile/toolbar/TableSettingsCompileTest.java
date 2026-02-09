package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.MultiColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ResizeButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    void testExportWithFilenameAndNoModal() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/table_settings/testExportWithFilenameNoModal.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank2.query.xml")
                .get(new PageContext("testExportWithFilenameNoModal"));

        AbstractButton exportButton = page.getWidget().getToolbar().getGroups().getFirst().getButtons().getFirst();

        assertThat(exportButton.getAction(), instanceOf(CustomAction.class));
        CustomAction exportAction = (CustomAction) exportButton.getAction();
        assertThat(exportAction.getType(), is("n2o/api/utils/export"));

        Map<String, Object> payloadAttributes = exportAction.getPayload().getAttributes();
        assertThat(payloadAttributes.get("filename"), is("Выгрузка из перечня заявок"));

        HashMap<String, Object> values = (HashMap<String, Object>) payloadAttributes.get("values");
        assertThat(((HashMap<String, Object>) values.get("format")).get("value"), is("xlsx"));
        assertThat(((HashMap<String, Object>) values.get("charset")).get("value"), is("utf-8"));
        assertThat(((HashMap<String, Object>) values.get("type")).get("value"), is("all"));
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
        checkColumn(nestedMulti, "cell7", false, true);
        checkColumn(nestedMulti.getChildren().get(0), "city", false, true);
        checkColumn(nestedMulti.getChildren().get(1), "country", false, true);

        checkColumn(multiColumn3, "id_multi3", true, true);
        checkColumn(multiColumn3.getChildren().getFirst(), "address", true, true);

        checkColumn(multiColumn4, "cell12", true, false);
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
        assertThat(groups.size(), is(3));

        List<AbstractButton> buttons = groups.getFirst().getButtons();
        checkButtons(buttons, AbstractButton::getHint, "success");
        assertThat(buttons.get(9).getSrc(), is("DropdownButton"));

        buttons = ((Submenu) buttons.get(9)).getContent().stream().map(AbstractButton.class::cast).toList();
        checkButtons(buttons, AbstractButton::getLabel, null);
        assertThat(buttons.get(9).getSrc(), is("StandardButton"));
        assertThat(buttons.get(9).getId(), is("action1"));

        buttons = groups.get(1).getButtons();
        checkButtons(buttons, AbstractButton::getHint, "success");
        assertThat(buttons.get(9).getSrc(), is("DropdownButton"));

        buttons = ((Submenu) buttons.get(9)).getContent().stream().map(AbstractButton.class::cast).toList();
        checkButtons(buttons, AbstractButton::getLabel, null);
        assertThat(buttons.get(9).getSrc(), is("StandardButton"));
        assertThat(buttons.get(9).getId(), is("action2"));

        buttons = groups.get(2).getButtons();
        assertThat(buttons.get(0).getAction(), instanceOf(CustomAction.class));

        assertThat(((CustomAction) buttons.getFirst().getAction()).getType(), is("n2o/api/utils/export"));
        HashMap<String, Object> values = (HashMap<String, Object>) ((CustomAction) buttons.getFirst().getAction()).getPayload().getAttributes().get("values");
        assertThat(((HashMap<String, Object>) values.get("format")).get("value"), is("csv"));
        assertThat(((HashMap<String, Object>) values.get("charset")).get("value"), is("utf-8"));
        assertThat(((HashMap<String, Object>) values.get("type")).get("value"), is("all"));
    }

    private static void checkButtons(List<AbstractButton> buttons, Function<AbstractButton, String> labelExtractor, String color) {
        assertThat(buttons.getFirst().getSrc(), is("StandardButton"));
        assertThat(buttons.getFirst().getLabel(), is("Обновление"));
        assertThat(buttons.getFirst().getIcon(), nullValue());
        assertThat(buttons.getFirst().getHint(), nullValue());
        assertThat(buttons.getFirst().getColor(), is(color));

        assertThat(buttons.get(1).getSrc(), is("StandardButton"));
        assertThat(buttons.get(1).getLabel(), nullValue());
        assertThat(buttons.get(1).getIcon(), is("fas fa-sync"));
        assertThat(buttons.get(1).getHint(), is("Подсказка"));

        assertThat(buttons.get(2).getSrc(), is("StandardButton"));
        assertThat(buttons.get(2).getAction(), instanceOf(ShowModal.class));
        assertThat(((ShowModal) buttons.get(2).getAction()).getPageId(), is("exportModal?formatId=xlsx&formatName=XLSX&charsetId=utf-8&charsetName=UTF-8&sizeId=all"));
        assertThat(labelExtractor.apply(buttons.get(2)), is("Экспортировать"));
        assertThat(buttons.get(2).getColor(), is(color));

        assertThat(buttons.get(3).getSrc(), is("ToggleColumn"));
        assertThat(labelExtractor.apply(buttons.get(3)), is("Скрытие столбцов"));
        assertThat(buttons.get(3).getColor(), is(color));

        assertThat(buttons.get(4).getSrc(), is("StandardButton"));
        assertThat(labelExtractor.apply(buttons.get(4)), is("Фильтры"));
        assertThat(buttons.get(4).getColor(), is(color));

        assertThat(buttons.get(5).getLabel(), is("button1"));
        assertThat(buttons.get(5).getSrc(), is("StandardButton"));

        assertThat(buttons.get(6).getSrc(), is("ChangeSize"));
        assertThat(buttons.get(6).getClassName(), is("test"));
        assertThat(buttons.get(6).getStyle(), is(Map.of("color", "red")));
        assertThat(labelExtractor.apply(buttons.get(6)), is("Количество записей"));
        assertThat(((ResizeButton) buttons.get(6)).getSize(), is(new Integer[]{5, 10, 15}));
        assertThat(buttons.get(6).getColor(), is(color));

        assertThat(buttons.get(7).getSrc(), is("WordWrap"));
        assertThat(labelExtractor.apply(buttons.get(7)), is("Перенос по словам"));
        assertThat(buttons.get(7).getColor(), is(color));

        assertThat(buttons.get(8).getSrc(), is("ResetSettings"));
        assertThat(buttons.get(8).getColor(), is(color));
    }
}
