package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;

class TableGeneratorsTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
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
    void generateCrud() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/crud.page.xml")
                .get(new PageContext("crud"));
        Table<?> t = (Table<?>) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().size(), is(1));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().size(), is(3));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(0).getId(), is("create"));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(1).getId(), is("update"));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(2).getId(), is("delete"));
    }

    @Test
    void generateTableSettings() {
        PageContext context = new PageContext("table_settings");
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/table_settings.page.xml")
                .get(context);
        Table<?> t = (Table<?>) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().size(), is(6));

        AbstractButton filtersBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(0);
        assertThat(filtersBtn, allOf(
                hasProperty("hint", is("Фильтры")),
                hasProperty("icon", is("fa fa-filter")),
                hasProperty("label", nullValue())
        ));
        assertThat(filtersBtn.getAction(), allOf(
                instanceOf(CustomAction.class),
                hasProperty("payload",
                        hasProperty("attributes", hasEntry("widgetId", "table_settings_tb1"))),
                hasProperty("type", is("n2o/widgets/TOGGLE_FILTER_VISIBILITY"))
        ));

        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(1), allOf(
                hasProperty("src", is("ToggleColumn")),
                hasProperty("hint", is("Скрытие столбцов")),
                hasProperty("icon", is("fa fa-table")),
                hasProperty("label", nullValue())
        ));

        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(2), allOf(
                hasProperty("action", instanceOf(RefreshAction.class)),
                hasProperty("hint", is("Обновить")),
                hasProperty("icon", is("fa fa-refresh")),
                hasProperty("label", nullValue())
        ));

        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().get(3), allOf(
                hasProperty("src", is("ChangeSize")),
                hasProperty("hint", is("Количество записей")),
                hasProperty("icon", is("fa fa-bars")),
                hasProperty("label", nullValue())
        ));

        AbstractButton wordwrapBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(4);
        assertThat(((CustomAction) wordwrapBtn.getAction()).getType(), is("n2o/table/switchTableParam"));
        assertThat(((CustomAction) wordwrapBtn.getAction()).getPayload().getAttributes().get("widgetId"), is("table_settings_tb1"));
        assertThat(wordwrapBtn, allOf(
                hasProperty("src", is("WordWrap")),
                hasProperty("hint", is("Перенос по словам")),
                hasProperty("icon", is("fa fa-exchange")),
                hasProperty("label", nullValue())
        ));

        AbstractButton exportBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(5);
        assertThat(exportBtn, allOf(
                hasProperty("hint", is("Экспортировать")),
                hasProperty("icon", is("fa fa-share-square-o")),
                hasProperty("label", nullValue())
        ));
        assertThat(exportBtn.getAction(), Matchers.instanceOf(ShowModal.class));
        assertThat(((ShowModal) exportBtn.getAction()).getPageId(), is("exportModal"));
        assertThat(((ShowModal) exportBtn.getAction()).getPayload().getPageUrl(), is("/table_settings/exportModal_table_settings_tb1"));


        PageContext modalPageContext = (PageContext) route("/table_settings/exportModal_table_settings_tb1", Page.class);
        assertThat(modalPageContext.getParentDatasourceIdsMap().size(), is(1));
        assertThat(modalPageContext.getParentDatasourceIdsMap().get("ds1"), is("table_settings_ds1"));

        StandardPage modalPage = (StandardPage) compile("net/n2oapp/framework/config/default/exportModal.page.xml")
                .get(modalPageContext);
        assertThat(modalPage.getDatasources().size(), is(1));
        assertThat(modalPage.getDatasources().containsKey("table_settings_exportModal_table_settings_tb1_exportModalDs"), is(true));
        assertThat(modalPage.getDatasources().get("table_settings_exportModal_table_settings_tb1_exportModalDs").getId(), is("table_settings_exportModal_table_settings_tb1_exportModalDs"));

        AbstractButton downloadBtn = modalPage.getToolbar().getButton("table_settings_exportModal_table_settings_tb1_mi0");
        assertThat(downloadBtn, allOf(
                hasProperty("label", is("Загрузить")),
                hasProperty("icon", is("fa fa-download")),
                hasProperty("color", is("primary"))
        ));
        CustomAction download = ((CustomAction) downloadBtn.getAction());
        assertThat(download.getType(), is("n2o/api/utils/export"));
        assertThat(download.getPayload().getAttributes(), allOf(
                hasEntry("baseURL", "/n2o/export"),
                hasEntry("configDatasource", "table_settings_exportModal_table_settings_tb1_exportModalDs"),
                hasEntry("exportDatasource", "table_settings_ds1")
        ));
        AbstractButton closeBtn = modalPage.getToolbar().getButton("table_settings_exportModal_table_settings_tb1_mi1");
        assertThat(closeBtn.getLabel(), is("Закрыть"));
        assertThat(closeBtn.getAction().getClass(), is(CloseAction.class));

        // in sub-menu
        checkToolbarWithSubMenu(t);

    }

    private static void checkToolbarWithSubMenu(Table<?> t) {
        List<AbstractButton> buttons = t.getToolbar().get("topRight").get(0).getButtons();
        assertThat(buttons.size(), is(1));
        assertThat(buttons.get(0), instanceOf(Submenu.class));

        List<PerformButton> subMenuButtons = ((Submenu) buttons.get(0)).getContent();
        assertThat(subMenuButtons.get(0).getLabel(), is("Фильтры"));
        assertThat(subMenuButtons.get(0).getIcon(), nullValue());
        assertThat(subMenuButtons.get(0).getHint(), nullValue());

        assertThat(subMenuButtons.get(1).getLabel(), is("Скрытие столбцов"));
        assertThat(subMenuButtons.get(1).getIcon(), nullValue());
        assertThat(subMenuButtons.get(1).getHint(), nullValue());

        assertThat(subMenuButtons.get(2).getLabel(), is("Обновить"));
        assertThat(subMenuButtons.get(2).getIcon(), nullValue());
        assertThat(subMenuButtons.get(2).getHint(), nullValue());

        assertThat(subMenuButtons.get(3).getLabel(), is("Количество записей"));
        assertThat(subMenuButtons.get(3).getIcon(), nullValue());
        assertThat(subMenuButtons.get(3).getHint(), nullValue());

        assertThat(subMenuButtons.get(4).getLabel(), is("Перенос по словам"));
        assertThat(subMenuButtons.get(4).getIcon(), nullValue());
        assertThat(subMenuButtons.get(4).getHint(), nullValue());

        assertThat(subMenuButtons.get(5).getLabel(), is("Экспортировать"));
        assertThat(subMenuButtons.get(5).getIcon(), nullValue());
        assertThat(subMenuButtons.get(5).getHint(), nullValue());
    }

    @Test
    void generateColumns() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/columns.page.xml")
                .get(new PageContext("columns"));
        Table<?> t = (Table<?>)  page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getSrc(), is("ToggleColumn"));
        assertThat(button.getHint(), is("Скрытие столбцов"));
        assertThat(button.getIcon(), is("fa fa-table"));
    }

    @Test
    void generateFilters() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/filters.page.xml")
                .get(new PageContext("filters"));
        Table<?> t = (Table<?>)  page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(((CustomAction) button.getAction()).getType(), Matchers.is("n2o/widgets/TOGGLE_FILTER_VISIBILITY"));
        assertThat(((CustomAction) button.getAction()).getPayload().getAttributes().get("widgetId"), Matchers.is("filters_tb1"));
        assertThat(button.getHint(), is("Фильтры"));
        assertThat(button.getIcon(), is("fa fa-filter"));
    }

    @Test
    void generateRefresh() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/refresh.page.xml")
                .get(new PageContext("refresh"));
        Table<?> t = (Table<?>)  page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getAction(), Matchers.instanceOf(RefreshAction.class));
        assertThat(button.getHint(), is("Обновить"));
        assertThat(button.getIcon(), is("fa fa-refresh"));
    }

    @Test
    void generateResize() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/resize.page.xml")
                .get(new PageContext("resize"));
        Table<?> t = (Table<?>)  page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getSrc(), is("ChangeSize"));
        assertThat(button.getHint(), is("Количество записей"));
        assertThat(button.getIcon(), is("fa fa-bars"));
    }

    @Test
    void generateWordWrap() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/wordwrap.page.xml")
                .get(new PageContext("wordwrap"));
        Table<?> t = (Table<?>)  page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(((CustomAction) button.getAction()).getType(), Matchers.is("n2o/table/switchTableParam"));
        assertThat(((CustomAction) button.getAction()).getPayload().getAttributes().get("widgetId"), Matchers.is("wordwrap_w1"));
        assertThat(((CustomAction) button.getAction()).getPayload().getAttributes().get("paramKey"), Matchers.is("textWrap"));
        assertThat(button.getSrc(), is("WordWrap"));
        assertThat(button.getHint(), is("Перенос по словам"));
        assertThat(button.getIcon(), is("fa fa-exchange"));
    }

    @Test
    void generateExport() {
        PageContext context = new PageContext("export", "/export");
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/export.page.xml")
                .get(context);
        Table<?> t = (Table<?>)  page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getAction(), Matchers.instanceOf(ShowModal.class));
        assertThat(((ShowModal) button.getAction()).getPageId(), Matchers.is("exportModal"));
        assertThat(((ShowModal) button.getAction()).getPayload().getPageUrl(), Matchers.is("/export/exportModal_export_w1"));
        assertThat(button, allOf(
                hasProperty("hint", is("Экспортировать")),
                hasProperty("icon", is("fa fa-share-square-o"))
        ));
        assertThat(button.getConditions().size(), is(1));
        assertThat(button.getConditions().get(ValidationTypeEnum.ENABLED).get(0).getExpression(), is("this.length > 0"));
        assertThat(button.getConditions().get(ValidationTypeEnum.ENABLED).get(0).getModelLink(), is("models.datasource['export_ds1']"));
        assertThat(button.getConditions().get(ValidationTypeEnum.ENABLED).get(0).getMessage(), is("Недоступно при пустых данных"));

        PageContext modalPageContext = (PageContext) route("/export/exportModal_export_w1", Page.class);
        assertThat(modalPageContext.getParentDatasourceIdsMap().size(), is(1));
        assertThat(modalPageContext.getParentDatasourceIdsMap().get("ds1"), is("export_ds1"));

        StandardPage modalPage = (StandardPage) compile("net/n2oapp/framework/config/default/exportModal.page.xml")
                .get(modalPageContext);
        assertThat(modalPage.getDatasources().size(), is(1));
        assertThat(modalPage.getDatasources().containsKey("export_exportModal_export_w1_exportModalDs"), is(true));
        assertThat(modalPage.getDatasources().get("export_exportModal_export_w1_exportModalDs").getId(), is("export_exportModal_export_w1_exportModalDs"));

        AbstractButton downloadBtn = modalPage.getToolbar().getButton("export_exportModal_export_w1_mi0");
        assertThat(downloadBtn.getLabel(), is("Загрузить"));
        assertThat(downloadBtn.getIcon(), is("fa fa-download"));
        assertThat(downloadBtn.getColor(), is("primary"));
        CustomAction download = ((CustomAction) downloadBtn.getAction());
        assertThat(download.getType(), is("n2o/api/utils/export"));
        assertThat(download.getPayload().getAttributes().get("baseURL"), is("/n2o/export"));
        assertThat(download.getPayload().getAttributes().get("configDatasource"), is("export_exportModal_export_w1_exportModalDs"));
        assertThat(download.getPayload().getAttributes().get("exportDatasource"), is("export_ds1"));
        assertThat(download.getPayload().getAttributes().get("widgetId"), is("export_w1"));
        assertThat(download.getPayload().getAttributes().get("allLimit"), is("1000"));

        AbstractButton closeBtn = modalPage.getToolbar().getButton("export_exportModal_export_w1_mi1");
        assertThat(closeBtn.getLabel(), is("Закрыть"));
        assertThat(closeBtn.getAction().getClass(), is(CloseAction.class));
    }
}
