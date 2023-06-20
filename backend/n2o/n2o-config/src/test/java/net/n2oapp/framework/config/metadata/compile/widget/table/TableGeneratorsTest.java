package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.refresh.RefreshAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableGeneratorsTest extends SourceCompileTestBase {

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
    void generateCrud() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/crud.page.xml")
                .get(new PageContext("crud"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().size(), is(1));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().size(), is(3));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(0).getId(), is("create"));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(1).getId(), is("update"));
        assertThat(t.getToolbar().get("topRight").get(0).getButtons().get(2).getId(), is("delete"));
    }

    @Test
    public void generateTableSettings() {
        PageContext context = new PageContext("table_settings");
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/table_settings.page.xml")
                .get(context);
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("topLeft").get(0).getButtons().size(), is(6));

        AbstractButton filtersBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(0);
        assertThat(((CustomAction) filtersBtn.getAction()).getType(), Matchers.is("n2o/widgets/TOGGLE_FILTER_VISIBILITY"));
        assertThat(filtersBtn.getHint(), is("Фильтры"));
        assertThat(filtersBtn.getIcon(), is("fa fa-filter"));
        assertThat(filtersBtn.getLabel(), nullValue());
        assertThat(((CustomAction) filtersBtn.getAction()).getPayload().getAttributes().get("widgetId"), Matchers.is("table_settings_tb1"));

        AbstractButton columnsBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(1);
        assertThat(columnsBtn.getSrc(), is("ToggleColumn"));
        assertThat(columnsBtn.getHint(), is("Скрытие столбцов"));
        assertThat(columnsBtn.getIcon(), is("fa fa-table"));
        assertThat(columnsBtn.getLabel(), nullValue());

        AbstractButton refreshBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(2);
        assertThat(refreshBtn.getAction(), Matchers.instanceOf(RefreshAction.class));
        assertThat(refreshBtn.getHint(), is("Обновить"));
        assertThat(refreshBtn.getIcon(), is("fa fa-refresh"));
        assertThat(refreshBtn.getLabel(), nullValue());

        AbstractButton resizeBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(3);
        assertThat(resizeBtn.getSrc(), is("ChangeSize"));
        assertThat(resizeBtn.getHint(), is("Количество записей"));
        assertThat(resizeBtn.getIcon(), is("fa fa-bars"));
        assertThat(resizeBtn.getLabel(), nullValue());

        AbstractButton wordwrapBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(4);
        assertThat(((CustomAction) wordwrapBtn.getAction()).getType(), Matchers.is("n2o/widgets/TOGGLE_WORD_WRAP"));
        assertThat(((CustomAction) wordwrapBtn.getAction()).getPayload().getAttributes().get("widgetId"), Matchers.is("table_settings_tb1"));
        assertThat(wordwrapBtn.getSrc(), is("WordWrap"));
        assertThat(wordwrapBtn.getHint(), is("Перенос по словам"));
        assertThat(wordwrapBtn.getIcon(), is("fa fa-exchange"));
        assertThat(wordwrapBtn.getLabel(), nullValue());

        AbstractButton exportBtn = t.getToolbar().get("topLeft").get(0).getButtons().get(5);
        assertThat(exportBtn.getAction(), Matchers.instanceOf(ShowModal.class));
        assertThat(((ShowModal) exportBtn.getAction()).getPageId(), Matchers.is("exportModal"));
        assertThat(((ShowModal) exportBtn.getAction()).getPayload().getPageUrl(), Matchers.is("/table_settings/exportModal"));
        assertThat(exportBtn.getHint(), is("Экспортировать"));
        assertThat(exportBtn.getIcon(), is("fa fa-share-square-o"));
        assertThat(exportBtn.getLabel(), nullValue());

        PageContext modalPageContext = (PageContext) route("/table_settings/exportModal", Page.class);
        assertThat(modalPageContext.getParentDatasourceIdsMap().size(), is(1));
        assertThat(modalPageContext.getParentDatasourceIdsMap().get("ds1"), is("table_settings_ds1"));

        StandardPage modalPage = (StandardPage) compile("net/n2oapp/framework/config/default/exportModal.page.xml")
                .get(modalPageContext);
        assertThat(modalPage.getDatasources().size(), is(1));
        assertThat(modalPage.getDatasources().containsKey("table_settings_exportModal_exportModalDs"), is(true));
        assertThat(modalPage.getDatasources().get("table_settings_exportModal_exportModalDs").getId(), is("table_settings_exportModal_exportModalDs"));

        AbstractButton downloadBtn = modalPage.getToolbar().getButton("table_settings_exportModal_mi0");
        assertThat(downloadBtn.getLabel(), is("Загрузить"));
        assertThat(downloadBtn.getIcon(), is("fa fa-download"));
        assertThat(downloadBtn.getColor(), is("primary"));
        CustomAction download = ((CustomAction) downloadBtn.getAction());
        assertThat(download.getType(), is("n2o/api/utils/export"));
        assertThat(download.getPayload().getAttributes().get("baseURL"), is("/n2o/export"));
        assertThat(download.getPayload().getAttributes().get("configDatasource"), is("exportModal_exportModalDs"));
        assertThat(download.getPayload().getAttributes().get("exportDatasource"), is("table_settings_ds1"));

        AbstractButton closeBtn = modalPage.getToolbar().getButton("table_settings_exportModal_mi1");
        assertThat(closeBtn.getLabel(), is("Закрыть"));
        assertThat(closeBtn.getAction().getClass(), is(CloseAction.class));

        // in sub-menu
        List<AbstractButton> buttons = t.getToolbar().get("topRight").get(0).getButtons();
        assertThat(buttons.size(), is(1));
        assertThat(buttons.get(0), instanceOf(Submenu.class));

        List<PerformButton> subMenuButtons = ((Submenu) buttons.get(0)).getSubMenu();
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
    public void generateColumns() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/columns.page.xml")
                .get(new PageContext("columns"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getSrc(), is("ToggleColumn"));
        assertThat(button.getHint(), is("Скрытие столбцов"));
        assertThat(button.getIcon(), is("fa fa-table"));
    }

    @Test
    public void generateFilters() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/filters.page.xml")
                .get(new PageContext("filters"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(((CustomAction) button.getAction()).getType(), Matchers.is("n2o/widgets/TOGGLE_FILTER_VISIBILITY"));
        assertThat(((CustomAction) button.getAction()).getPayload().getAttributes().get("widgetId"), Matchers.is("filters_tb1"));
        assertThat(button.getHint(), is("Фильтры"));
        assertThat(button.getIcon(), is("fa fa-filter"));
    }

    @Test
    public void generateRefresh() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/refresh.page.xml")
                .get(new PageContext("refresh"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getAction(), Matchers.instanceOf(RefreshAction.class));
        assertThat(button.getHint(), is("Обновить"));
        assertThat(button.getIcon(), is("fa fa-refresh"));
    }

    @Test
    public void generateResize() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/resize.page.xml")
                .get(new PageContext("resize"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getSrc(), is("ChangeSize"));
        assertThat(button.getHint(), is("Количество записей"));
        assertThat(button.getIcon(), is("fa fa-bars"));
    }

    @Test
    public void generateWordWrap() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/wordwrap.page.xml")
                .get(new PageContext("wordwrap"));
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(((CustomAction) button.getAction()).getType(), Matchers.is("n2o/widgets/TOGGLE_WORD_WRAP"));
        assertThat(((CustomAction) button.getAction()).getPayload().getAttributes().get("widgetId"), Matchers.is("wordwrap_w1"));
        assertThat(button.getSrc(), is("WordWrap"));
        assertThat(button.getHint(), is("Перенос по словам"));
        assertThat(button.getIcon(), is("fa fa-exchange"));
    }

    @Test
    public void generateExport() {
        PageContext context = new PageContext("export", "/export");
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/toolbar/generate/export.page.xml")
                .get(context);
        Table t = (Table) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(t.getToolbar().get("bottomRight").get(0).getButtons().size(), is(1));

        AbstractButton button = t.getToolbar().get("bottomRight").get(0).getButtons().get(0);

        assertThat(button.getAction(), Matchers.instanceOf(ShowModal.class));
        assertThat(((ShowModal) button.getAction()).getPageId(), Matchers.is("exportModal"));
        assertThat(((ShowModal) button.getAction()).getPayload().getPageUrl(), Matchers.is("/export/exportModal"));
        assertThat(button.getHint(), is("Экспортировать"));
        assertThat(button.getIcon(), is("fa fa-share-square-o"));

        PageContext modalPageContext = (PageContext) route("/export/exportModal", Page.class);
        assertThat(modalPageContext.getParentDatasourceIdsMap().size(), is(1));
        assertThat(modalPageContext.getParentDatasourceIdsMap().get("ds1"), is("export_ds1"));

        StandardPage modalPage = (StandardPage) compile("net/n2oapp/framework/config/default/exportModal.page.xml")
                .get(modalPageContext);
        assertThat(modalPage.getDatasources().size(), is(1));
        assertThat(modalPage.getDatasources().containsKey("export_exportModal_exportModalDs"), is(true));
        assertThat(modalPage.getDatasources().get("export_exportModal_exportModalDs").getId(), is("export_exportModal_exportModalDs"));

        AbstractButton downloadBtn = modalPage.getToolbar().getButton("export_exportModal_mi0");
        assertThat(downloadBtn.getLabel(), is("Загрузить"));
        assertThat(downloadBtn.getIcon(), is("fa fa-download"));
        assertThat(downloadBtn.getColor(), is("primary"));
        CustomAction download = ((CustomAction) downloadBtn.getAction());
        assertThat(download.getType(), is("n2o/api/utils/export"));
        assertThat(download.getPayload().getAttributes().get("baseURL"), is("/n2o/export"));
        assertThat(download.getPayload().getAttributes().get("configDatasource"), is("exportModal_exportModalDs"));
        assertThat(download.getPayload().getAttributes().get("exportDatasource"), is("export_ds1"));

        AbstractButton closeBtn = modalPage.getToolbar().getButton("export_exportModal_mi1");
        assertThat(closeBtn.getLabel(), is("Закрыть"));
        assertThat(closeBtn.getAction().getClass(), is(CloseAction.class));
    }
}
