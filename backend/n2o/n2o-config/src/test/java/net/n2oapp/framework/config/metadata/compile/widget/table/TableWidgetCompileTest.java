package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Place;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.Alignment;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.cell.AbstractCell;
import net.n2oapp.framework.api.metadata.meta.cell.BadgeCell;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции виджета Таблица
 */
public class TableWidgetCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oAllDataPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oCellsPack(),
                new N2oActionsPack()
        );
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable5Compile.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
        );
    }

    @Test
    void testTable() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable5Compile.page.xml")
                .get(new PageContext("testTable5Compile"));
        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(table.getId(), is("testTable5Compile_w1"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().size(), is(3));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(0).getId(), is("testAction"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(0).getStyle().get("pageBreakBefore"), is("avoid"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(0).getStyle().get("paddingTop"), is("0"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(1).getId(), is("testTable5Compile_mi1"));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getShowToggleIcon(), is(true));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getSubMenu().get(0).getId(), is("testAction2"));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getSubMenu().get(0).getStyle().get("pageBreakBefore"), is("avoid"));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getSubMenu().get(0).getStyle().get("paddingTop"), is("0"));
        //columns
        assertThat(table.getComponent().getHeader().getCells().size(), is(7));

        assertThat(table.getComponent().getHeader().getCells().get(0).getLabel(), is("id"));
        assertThat(table.getComponent().getHeader().getCells().get(0).getSrc(), is("MyTableHeader"));
        assertThat(table.getComponent().getHeader().getCells().get(0).getElementAttributes().get("className"), is("my-table-header"));
        assertThat(table.getComponent().getHeader().getCells().get(0).getElementAttributes().get("style"), notNullValue());
        assertThat(((Map<String, String>) table.getComponent().getHeader().getCells().get(0).getElementAttributes().get("style")).get("color"), is("red"));

        assertThat(table.getComponent().getHeader().getCells().get(1).getSrc(), is("TextTableHeader"));
        assertThat(table.getComponent().getHeader().getCells().get(1).getElementAttributes().get("className"), is(nullValue()));
        assertThat(table.getComponent().getHeader().getCells().get(1).getElementAttributes().get("style"), nullValue());

        assertThat(table.getComponent().getHeader().getCells().get(4).getLabel(), is("label"));

        assertThat(table.getComponent().getHeader().getCells().get(5).getLabel(), is("id"));

        assertThat(table.getComponent().getHeader().getCells().get(6).getLabel(), is("label"));

        //sells
        assertThat(table.getComponent().getBody().getCells().size(), is(7));

        assertThat(((AbstractCell) table.getComponent().getBody().getCells().get(0)).getElementAttributes().get("style"), is("marginLeft:10px"));
        assertThat(((TextCell) table.getComponent().getBody().getCells().get(0)).getElementAttributes().get("className"), is("`test == 1 ? 'css1' : test == 2 ? 'css2' : 'css3'`"));
        assertThat(((TextCell) table.getComponent().getBody().getCells().get(0)).getFormat(), is("password"));
        assertThat(table.getComponent().getBody().getCells().get(2).getId(), is("cell2"));
        assertThat(table.getComponent().getBody().getCells().get(3).getId(), is("cell3"));

        assertThat(table.getToolbar().getButton("but"), notNullValue());
        assertThat(table.getComponent().getBody().getRow().getSrc(), is("TableRow"));
        assertThat(table.getComponent().getBody().getRow().getElementAttributes().get("className"), is("red"));
        assertThat(table.getComponent().getBody().getRow().getElementAttributes().get("style"), is("color:blue"));
        QueryContext queryContext = (QueryContext) route("/testTable5Compile/w1", CompiledQuery.class);

        assertThat(queryContext.getValidations(), notNullValue());
        assertThat(queryContext.getValidations().size(), is(1));
        assertThat(queryContext.getValidations().get(0), instanceOf(MandatoryValidation.class));
        assertThat(queryContext.getValidations().get(0).getMoment(), is(N2oValidation.ServerMoment.beforeQuery));
        assertThat(queryContext.getMessagesForm(), is("testTable5Compile_w1"));

        assertThat(table.getComponent().getRowSelection(), is(RowSelectionEnum.CHECKBOX));
        assertThat(table.getComponent().getAutoSelect(), is(true));
        assertThat(table.getComponent().getHeight(), is("200px"));
        assertThat(table.getComponent().getWidth(), is("400px"));
        assertThat(table.getComponent().getTextWrap(), is(false));
        assertThat(table.getFiltersDatasourceId(), is("testTable5Compile_filtersDs"));

        Dependency enabled = table.getDependency().getEnabled().get(0);
        assertThat(enabled.getOn(), is("models.filter['testTable5Compile_w1']"));
        assertThat(enabled.getCondition(), is("name == 'test2'"));
        Dependency visible = table.getDependency().getVisible().get(0);
        assertThat(visible.getOn(), is("models.filter['testTable5Compile_filtersDs']"));
        assertThat(visible.getCondition(), is("name == 'test1'"));
    }

    @Test
    void testRowColor() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4RowColorCompile.widget.xml").get(new WidgetContext("testTable4RowColorCompile"));
        assertThat(table.getComponent().getBody().getRow().getElementAttributes().get("className"),
                is("`gender.id == 1 ? 'red' : gender.id == 2 ? 'blue' : gender.id == 3 ? 'white' : 'green'`"));
    }

    @Test
    void testRowClick() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4RowClickCompile.page.xml").get(new PageContext("testTable4RowClickCompile"));
        List<TableWidgetComponent> rowClicks = new ArrayList<>();
        page.getRegions().get("single").get(0).getContent().forEach(c -> rowClicks.add(((Table) c).getComponent()));

        assertThat(rowClicks.size(), is(10));
        assertThat(rowClicks.get(0).getBody().getRow().getClick(), nullValue());
        assertThat(rowClicks.get(1).getBody().getRow().getClick().getEnablingCondition(), nullValue(String.class));
        assertThat(rowClicks.get(2).getBody().getRow().getClick().getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(3).getBody().getRow().getClick().getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(4).getBody().getRow().getClick().getEnablingCondition(), is("1==1"));
        assertThat(rowClicks.get(5).getBody().getRow().getClick().getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(6).getBody().getRow().getClick().getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(7).getBody().getRow().getClick().getEnablingCondition(), is("1==1"));
        assertThat(rowClicks.get(8).getBody().getRow().getClick().getAction(), notNullValue());
    }

    @Test
    void testSortableColumns() {
        Table table = (Table) ((SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.page.xml", "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml").get(new PageContext("testTable4SortableCompile"))).getWidget();
        assertThat(table.getId(), is("testTable4SortableCompile_w1"));
        assertThat(table.getComponent().getHeader().getCells().size(), is(7));
        List<ColumnHeader> headers = table.getComponent().getHeader().getCells();

        assertThat(headers.get(0).getId(), is("id"));

        assertThat(headers.get(0).getLabel(), is("id_name"));
        assertThat(headers.get(0).getSortingParam(), is("id"));

        assertThat(headers.get(1).getId(), is("col"));
        assertThat(headers.get(1).getLabel(), is("col_label"));

        assertThat(headers.get(2).getId(), is("name"));
        assertThat(headers.get(2).getLabel(), is("name"));
        assertThat(headers.get(2).getSortingParam(), is("id"));

        assertThat(headers.get(3).getId(), is("comments"));
        assertThat(headers.get(3).getLabel(), is("comments"));

        assertThat(headers.get(4).getId(), is("notInQuery"));
        assertThat(headers.get(4).getLabel(), is("notInQueryLabel"));

        QueryContext context = (QueryContext) route("/testTable4SortableCompile/w1", CompiledQuery.class);
        assertThat(context.getSortingMap().get("sorting.id"), is("id"));
        assertThat(context.getSortingMap().get("sorting.name"), is("name"));

        assertThat(table.getComponent().getRowSelection(), is(RowSelectionEnum.RADIO));
        assertThat(table.getComponent().getTextWrap(), is(true));
    }

    @Test
    void testFilters() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4FiltersCompile.page.xml").get(new PageContext("testTable4FiltersCompile", "/page"));
        QueryContext queryCtx = (QueryContext) route("/page/main", CompiledQuery.class);

        //pre-filter name
        ModelLink nameLink = ((StandardDatasource) page.getDatasources().get("page_main")).getProvider().getQueryMapping().get("nameParam");
        assertThat(nameLink.normalizeLink(), is("models.filter['page_main'].name"));
        assertThat(page.getRoutes().getQueryMapping().get("nameParam").getOnSet(), is(nameLink));
        assertThat(queryCtx.getFilters().stream().anyMatch(f -> f.getFilterId().equals("name")), is(true));

        //table filter birthday
        ModelLink birthdayBeginLink = ((StandardDatasource) page.getDatasources().get("page_main")).getProvider().getQueryMapping().get("main_birthday_begin");
        ModelLink birthdayEndLink = ((StandardDatasource) page.getDatasources().get("page_main")).getProvider().getQueryMapping().get("main_birthday_end");
        assertThat(birthdayBeginLink.normalizeLink(), is("models.filter['page_main'].birthday.begin"));
        assertThat(birthdayEndLink.normalizeLink(), is("models.filter['page_main'].birthday.end"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_begin").getOnSet(), is(birthdayBeginLink));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_end").getOnSet(), is(birthdayEndLink));
        assertThat(queryCtx.getFilters().stream().anyMatch(f -> f.getFilterId().equals("birthday.begin")), is(true));
        assertThat(queryCtx.getFilters().stream().anyMatch(f -> f.getFilterId().equals("birthday.end")), is(true));

        //table filter gendersLink
        ModelLink gendersLink = ((StandardDatasource) page.getDatasources().get("page_main")).getProvider().getQueryMapping().get("main_genders_id");
        assertThat(gendersLink.getBindLink(), is("models.filter['page_main']"));
        assertThat(gendersLink.getValue(), is("`genders.map(function(t){return t.id})`"));
        assertThat(page.getRoutes().getQueryMapping().get("main_genders_id").getOnSet(), is(gendersLink));
        assertThat(queryCtx.getFilters().stream().anyMatch(f -> f.getFilterId().equals("genders*.id")), is(true));
    }

    @Test
    void testDefaultValues() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableCompileFilters.page.xml").get(new PageContext("testTableCompileFilters"));
        assertThat(page.getModels().size(), is(8));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].birthday").getValue()).getValues().get("begin"), is("21.10.2018"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].birthday").getValue()).getValues().get("end"), is("22.11.2018"));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].name").getValue(), is("test"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].gender").getValue()).getValues().get("name"), is("test"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].gender").getValue()).getValues().get("id"), is(1));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].age").getValue(), is(18));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].amount").getValue(), is("100.99"));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].hidden").getValue(), is("test"));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].name2").getValue(), is("`today()`"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].birthday2").getValue()).getValues().get("begin"), is("`today()`"));
    }

    @Test
    void testColumnsWidth() {
        Table table = (Table) ((SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.page.xml", "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml").get(new PageContext("testTable4SortableCompile"))).getWidget();
        assertThat(table.getId(), is("testTable4SortableCompile_w1"));
        assertThat(table.getComponent().getHeader().getCells().size(), is(7));
        List<ColumnHeader> headers = table.getComponent().getHeader().getCells();

        assertThat(headers.get(0).getElementAttributes().get("width"), is("100px"));
        assertThat(headers.get(1).getElementAttributes().get("width"), nullValue());
        assertThat(headers.get(6).getElementAttributes().get("width"), is("200px"));
    }

    @Test
    void testRequiredPrefilters() {
        compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableRequiredPrefilters.page.xml").get(new PageContext("testTableRequiredPrefilters"));
        QueryContext queryContext = ((QueryContext) route("/testTableRequiredPrefilters/w1", CompiledQuery.class));

        assertThat(queryContext.getValidations().get(0).getId(), is("genders*.id"));
        assertThat(queryContext.getValidations().get(0).getFieldId(), is("genders*.id"));
        assertThat(queryContext.getValidations().get(0).getMoment(), is(N2oValidation.ServerMoment.beforeQuery));
        assertThat(queryContext.getValidations().get(0).getSeverity(), is(SeverityType.danger));
    }

    @Test
    void testColumnVisibility() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableColumnVisibility.page.xml").get(new PageContext("testTableColumnVisibility"));
        List<ColumnHeader> columnHeaders = ((Table) page.getRegions().get("single").get(0).getContent().get(0)).getComponent().getHeader().getCells();
        assertThat(columnHeaders.get(0).getVisible(), nullValue());
        assertThat(columnHeaders.get(0).getConditions().get(ValidationType.visible).get(0).getExpression(), is("abc == 1"));
        assertThat(columnHeaders.get(0).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.filter['testTableColumnVisibility_table']"));
        assertThat(columnHeaders.get(1).getVisible(), is(Boolean.TRUE));
        assertThat(columnHeaders.get(2).getVisible(), nullValue());
        assertThat(columnHeaders.get(3).getConditions().get(ValidationType.visible).get(0).getExpression(), is("type == 1"));
        assertThat(columnHeaders.get(3).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.resolve['testTableColumnVisibility_form']"));
    }

    @Test
    void testFilterColumns() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFilterColumns.page.xml").get(new PageContext("testFilterColumns"));


        List<ColumnHeader> columnHeaders = ((Table) page.getWidget()).getComponent().getHeader().getCells();
        assertThat(columnHeaders.get(0), instanceOf(ColumnHeader.class));
        assertThat(columnHeaders.get(0).getId(), is("name"));
        assertThat(columnHeaders.get(0).getLabel(), is("label"));
        assertThat(columnHeaders.get(0).getFilterable(), is(true));
        assertThat(columnHeaders.get(0).getFilterControl(), instanceOf(InputText.class));
        assertThat(columnHeaders.get(0).getFilterControl().getId(), is("name"));

        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("w1_name");
        assertThat(query.getOnSet().getBindLink(), is("models.filter['testFilterColumns_w1']"));
        assertThat(query.getOnSet().getValue(), is("`name`"));

        BindLink link = ((StandardDatasource) page.getDatasources().get("testFilterColumns_w1")).getProvider().getQueryMapping().get("w1_name");
        assertThat(link.getValue(), is("`name`"));
        assertThat(link.getBindLink(), is("models.filter['testFilterColumns_w1']"));

        List<Cell> cells = ((Table) page.getWidget()).getComponent().getBody().getCells();
        assertThat(cells.get(0), instanceOf(BadgeCell.class));
        assertThat(cells.get(0).getId(), is("name"));
        assertThat(cells.get(1), instanceOf(TextCell.class));
        assertThat(cells.get(1).getId(), is("age"));
    }

    @Test
    void testMultiColumn() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testMultiColumn.page.xml").get(new PageContext("testMultiColumn"));

        List<ColumnHeader> headers = ((Table) page.getWidget()).getComponent().getHeader().getCells();
        assertThat(headers.size(), is(2));
        assertThat(headers.get(0).getId(), is("test1"));
        assertThat(headers.get(0).getMultiHeader(), nullValue());
        assertThat(headers.get(0).getChildren(), nullValue());
        assertThat(headers.get(1).getLabel(), is("label"));
        assertThat(headers.get(1).getMultiHeader(), is(true));

        headers = headers.get(1).getChildren();
        assertThat(headers.size(), is(3));
        assertThat(headers.get(0).getMultiHeader(), is(true));
        assertThat(headers.get(0).getSrc(), is("MyTableHeader"));
        assertThat(headers.get(0).getElementAttributes().get("className"), is("my-table-header"));
        assertThat(headers.get(0).getElementAttributes().get("style"), notNullValue());
        assertThat(((Map<String, String>) headers.get(0).getElementAttributes().get("style")).get("color"), is("red"));
        assertThat(headers.get(1).getId(), is("test4"));
        assertThat(headers.get(1).getMultiHeader(), is(nullValue()));
        assertThat(headers.get(1).getChildren(), nullValue());
        assertThat(headers.get(2).getId(), is("name"));
        assertThat(headers.get(2).getFilterable(), is(true));
        assertThat(headers.get(2).getFilterControl(), instanceOf(InputText.class));
        assertThat(headers.get(2).getFilterControl().getId(), is("name"));

        // проверка компиляции фильтруемого столбца внутри мульти-столбца
        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("table_name");
        assertThat(query.getOnSet().getBindLink(), is("models.filter['testMultiColumn_table']"));
        assertThat(query.getOnSet().getValue(), is("`name`"));

        BindLink link = ((StandardDatasource) page.getDatasources().get("testMultiColumn_table")).getProvider().getQueryMapping().get("table_name");
        assertThat(link.getValue(), is("`name`"));
        assertThat(link.getBindLink(), is("models.filter['testMultiColumn_table']"));

        headers = headers.get(0).getChildren();
        assertThat(headers.size(), is(2));
        assertThat(headers.get(0).getId(), is("test2"));
        assertThat(headers.get(0).getMultiHeader(), is(nullValue()));
        assertThat(headers.get(0).getChildren(), nullValue());
        assertThat(headers.get(1).getId(), is("test3"));
        assertThat(headers.get(1).getMultiHeader(), is(nullValue()));
        assertThat(headers.get(1).getChildren(), nullValue());
    }

    @Test
    void testPaginationDefaultParams() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4PaginationDefault.widget.xml").get(new WidgetContext("testTable4PaginationDefault"));
        Pagination pagination = table.getPaging();
        checkDefaultPagingParams(pagination);
    }

    @Test
    void testPaginationMissing() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4PaginationMissing.widget.xml").get(new WidgetContext("testTable4PaginationMissing"));
        Pagination pagination = table.getPaging();
        checkDefaultPagingParams(pagination);
    }

    @Test
    void testPaginationNonDefaultParams() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4PaginationNonDefault.widget.xml").get(new WidgetContext("testTable4PaginationNonDefault"));
        Pagination pagination = table.getPaging();

        assertThat(pagination.getPrev(), is(true));
        assertThat(pagination.getNext(), is(true));
        assertThat(pagination.getShowCount(), is(ShowCountType.BY_REQUEST));
        assertThat(pagination.getShowLast(), is(false));
        assertThat(pagination.getPrevLabel(), is("prev"));
        assertThat(pagination.getPrevIcon(), is("fa fa-angle-left"));
        assertThat(pagination.getNextLabel(), is("next"));
        assertThat(pagination.getNextIcon(), is("fa fa-angle-right"));
        assertThat(pagination.getClassName(), is("class"));
        assertThat(pagination.getStyle(), is(Map.of("width", "15", "height", "10")));
        assertThat(pagination.getPlace(), is(Place.topLeft));
    }

    @Test
    void testHeaderLabelInitialization() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4HeaderLabels.page.xml").get(new PageContext("testTable4HeaderLabels"));

        List<ColumnHeader> columnHeaders = ((Table) page.getRegions().get("single").get(0).getContent().get(0)).getComponent().getHeader().getCells();
        assertThat(columnHeaders.get(0).getLabel(), is("id"));
        assertThat(columnHeaders.get(1).getLabel(), is("name"));
    }

    private void checkDefaultPagingParams(Pagination pagination) {
        assertThat(pagination.getPrev(), is(false));
        assertThat(pagination.getNext(), is(false));
        assertThat(pagination.getShowCount(), is(ShowCountType.ALWAYS));
        assertThat(pagination.getShowLast(), is(true));
        assertThat(pagination.getPrevLabel(), is(nullValue()));
        assertThat(pagination.getPrevIcon(), is("fa fa-angle-left"));
        assertThat(pagination.getNextLabel(), is(nullValue()));
        assertThat(pagination.getNextIcon(), is("fa fa-angle-right"));
        assertThat(pagination.getClassName(), is(nullValue()));
        assertThat(pagination.getStyle(), is(nullValue()));
        assertThat(pagination.getPlace(), is(Place.bottomLeft));
    }

    @Test
    void testColumnAttributes() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable5ColumnsAttributesCompile.widget.xml").get(new WidgetContext("testTable5ColumnsAttributesCompile"));

        assertThat(table.getComponent().getHeader().getCells().size(), is(3));

        ColumnHeader header = table.getComponent().getHeader().getCells().get(0);
        assertThat(header.getSrc(), is("MyTableHeader"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.RIGHT.getId()));
        assertThat(header.getElementAttributes().get("className"), is("my-table-header"));
        assertThat(((Map<String, String>) header.getElementAttributes().get("style")).get("color"), is("red"));

        header = table.getComponent().getHeader().getCells().get(1);
        assertThat(header.getSrc(), is("MyFilterHeader"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.LEFT.getId()));
        assertThat(header.getElementAttributes().get("className"), is("my-filter-header"));
        assertThat(((Map<String, String>) header.getElementAttributes().get("style")).get("color"), is("green"));

        header = table.getComponent().getHeader().getCells().get(2);
        assertThat(header.getSrc(), is("MyMultiHeader"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));
        assertThat(header.getElementAttributes().get("className"), is("my-multi-header"));
        assertThat(((Map<String, String>) header.getElementAttributes().get("style")).get("color"), is("blue"));
        assertThat(header.getLabel(), is("Multi"));
    }

    @Test
    void testPassSortingToDatasource() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable5PassSortingToDatasource.page.xml")
                .get(new PageContext("testTable5PassSortingToDatasource"));

        AbstractDatasource ds1 = page.getDatasources().get("testTable5PassSortingToDatasource_ds1");
        assertThat(ds1.getSorting().size(), is(1));
        assertThat(ds1.getSorting().get("name"), is("DESC"));

        AbstractDatasource inlineDatasource = page.getDatasources().get("testTable5PassSortingToDatasource_w2");
        assertThat(inlineDatasource.getSorting().size(), is(1));
        assertThat(inlineDatasource.getSorting().get("age"), is("ASC"));
    }

    @Test
    void testAlignment() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable5Alignment.widget.xml").get(new WidgetContext("testTable5Alignment"));

        ColumnHeader header = table.getComponent().getHeader().getCells().get(0);
        AbstractCell cell = (AbstractCell) table.getComponent().getBody().getCells().get(0);
        assertThat(header.getLabel(), is("simple1"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.LEFT.getId()));
        assertThat(cell.getId(), is("simple1"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.LEFT.getId()));

        header = table.getComponent().getHeader().getCells().get(1);
        cell = (AbstractCell) table.getComponent().getBody().getCells().get(1);
        assertThat(header.getLabel(), is("simple2"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));
        assertThat(cell.getId(), is("simple2"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));

        header = table.getComponent().getHeader().getCells().get(2);
        cell = (AbstractCell) table.getComponent().getBody().getCells().get(2);
        assertThat(header.getLabel(), is("filter1"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.RIGHT.getId()));
        assertThat(cell.getId(), is("filter1"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));


        ColumnHeader multiHeader = table.getComponent().getHeader().getCells().get(3);
        assertThat(multiHeader.getLabel(), is("multi1"));
        assertThat(multiHeader.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));


        multiHeader = table.getComponent().getHeader().getCells().get(4);
        assertThat(multiHeader.getLabel(), is("multi2"));
        assertThat(multiHeader.getElementAttributes().get("alignment"), is(Alignment.RIGHT.getId()));

        header = multiHeader.getChildren().get(0);
        cell = (AbstractCell) table.getComponent().getBody().getCells().get(3);
        assertThat(header.getLabel(), is("sub21"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));
        assertThat(cell.getId(), is("sub21"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));


        multiHeader = table.getComponent().getHeader().getCells().get(5);
        assertThat(multiHeader.getLabel(), is("multi3"));
        assertThat(multiHeader.getElementAttributes().get("alignment"), is(Alignment.RIGHT.getId()));

        header = multiHeader.getChildren().get(0);
        cell = (AbstractCell) table.getComponent().getBody().getCells().get(4);
        assertThat(header.getLabel(), is("sub31"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.LEFT.getId()));
        assertThat(cell.getId(), is("sub31"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));

        header = multiHeader.getChildren().get(1);
        cell = (AbstractCell) table.getComponent().getBody().getCells().get(5);
        assertThat(header.getLabel(), is("sub32"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.RIGHT.getId()));
        assertThat(cell.getId(), is("sub32"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.CENTER.getId()));

        header = multiHeader.getChildren().get(2);
        cell = (AbstractCell) table.getComponent().getBody().getCells().get(6);
        assertThat(header.getLabel(), is("sub33"));
        assertThat(header.getElementAttributes().get("alignment"), is(Alignment.LEFT.getId()));
        assertThat(cell.getId(), is("sub33"));
        assertThat(cell.getElementAttributes().get("alignment"), is(Alignment.RIGHT.getId()));
    }

    @Test
    void testBlackResetList() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testBlackResetList.widget.xml")
                .get(new WidgetContext("testBlackResetList"));

        assertThat(table.getFilter().getBlackResetList().get(0), is("urgently"));
    }

}
