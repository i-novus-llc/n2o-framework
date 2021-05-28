package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.action.UpdateMapModelPayload;
import net.n2oapp.framework.api.metadata.meta.action.UpdateModelPayload;
import net.n2oapp.framework.api.metadata.meta.control.*;
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
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции виджета Таблица
 */
public class TableWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oRegionsPack(), new N2oAllDataPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(), new N2oActionsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable4Compile.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
    }

    @Test
    public void testTable() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4Compile.widget.xml")
                .get(new WidgetContext("testTable4Compile"));
        assertThat(table.getId(), is("$testTable4Compile"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().size(), is(3));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(0).getId(), is("testAction"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(0).getStyle().get("pageBreakBefore"), is("avoid"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(0).getStyle().get("paddingTop"), is("0"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(1).getId(), is("subMenu1"));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getShowToggleIcon(), is(true));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getSubMenu().get(0).getId(), is("testAction2"));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getSubMenu().get(0).getStyle().get("pageBreakBefore"), is("avoid"));
        assertThat(((Submenu) table.getToolbar().get("topLeft").get(0).getButtons().get(1)).getSubMenu().get(0).getStyle().get("paddingTop"), is("0"));
        //columns
        assertThat(table.getComponent().getHeaders().get(0).getSrc(), is("MyTableHeader"));
        assertThat(table.getComponent().getHeaders().get(0).getCssClass(), is("my-table-header"));
        assertThat(table.getComponent().getHeaders().get(0).getStyle().size(), is(1));
        assertThat(table.getComponent().getHeaders().get(0).getStyle().get("color"), is("red"));
        assertThat(table.getComponent().getHeaders().get(1).getSrc(), is("TextTableHeader"));
        assertThat(table.getComponent().getHeaders().get(1).getCssClass(), is(nullValue()));
        assertThat(table.getComponent().getHeaders().get(1).getStyle(), is(nullValue()));
        assertThat(table.getComponent().getCells().size(), is(2));
        assertThat(((N2oAbstractCell) table.getComponent().getCells().get(0)).getReactStyle().get("marginLeft"), is("10px"));
        assertThat(table.getComponent().getHeaders().size(), is(2));
        assertThat(((N2oTextCell) table.getComponent().getCells().get(0)).getCssClass(),
                is("`test == 1 ? 'css1' : test == 2 ? 'css2' : 'css3'`"));
        assertThat(((N2oTextCell) table.getComponent().getCells().get(0)).getFormat(), is("password"));
        assertThat(((N2oTextCell) table.getComponent().getCells().get(0)).getHideOnBlur(), is(true));
        assertThat(table.getActions().containsKey("but"), is(true));
        assertThat(table.getComponent().getRowClass(), is("red"));
        QueryContext queryContext = (QueryContext) route("/testTable4Compile", CompiledQuery.class);
        assertThat(queryContext.getValidations(), notNullValue());
        assertThat(queryContext.getValidations().size(), is(1));
        assertThat(queryContext.getValidations().get(0), instanceOf(MandatoryValidation.class));
        assertThat(queryContext.getValidations().get(0).getMoment(), is(N2oValidation.ServerMoment.beforeQuery));
        assertThat(queryContext.getFailAlertWidgetId(), is("$testTable4Compile"));
        assertThat(queryContext.getSuccessAlertWidgetId(), is("$testTable4Compile"));
        assertThat(queryContext.getMessagesForm(), is("$testTable4Compile_filter"));
        assertThat(table.getComponent().getRowSelection(), is(RowSelectionEnum.checkbox));
        assertThat(table.getComponent().getFetchOnInit(), is(false));
        assertThat(table.getComponent().getAutoCheckboxOnSelect(), is(true));
        assertThat(table.getComponent().getHeight(), is("200px"));
        assertThat(table.getComponent().getWidth(), is("400px"));
        assertThat(table.getComponent().getTextWrap(), is(false));
        assertThat(table.getFiltersDefaultValuesQueryId(), is("test"));
    }

    @Test
    public void testRowColor() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4RowColorCompile.widget.xml")
                .get(new WidgetContext("testTable4RowColorCompile"));
        assertThat(table.getComponent().getRowClass(),
                is("`gender.id == 1 ? 'red' : gender.id == 2 ? 'blue' : gender.id == 3 ? 'white' : 'green'`"));
        assertThat(table.getComponent().getHasSelect(), is(true));
    }

    @Test
    public void testRowClick() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4RowClickCompile.page.xml")
                .get(new PageContext("testTable4RowClickCompile"));
        List<TableWidgetComponent> rowClicks = new ArrayList<>();
        page.getRegions().get("right").get(0).getContent().forEach(c -> rowClicks.add(((Table) c).getComponent()));

        assertThat(rowClicks.size(), is(10));
        assertThat(rowClicks.get(0).getRowClick(), nullValue());
        assertThat(rowClicks.get(1).getRowClick().getEnablingCondition(), nullValue(String.class));
        assertThat(rowClicks.get(2).getRowClick().getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(3).getRowClick().getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(4).getRowClick().getEnablingCondition(), is("1==1"));
        assertThat(rowClicks.get(5).getRowClick().getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(6).getRowClick().getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(7).getRowClick().getEnablingCondition(), is("1==1"));
        assertThat(rowClicks.get(8).getRowClick().getAction(), notNullValue());
    }

    @Test
    public void testSortableColumns() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new WidgetContext("testTable4SortableCompile"));
        assertThat(table.getId(), is("$testTable4SortableCompile"));
        assertThat(table.getComponent().getHeaders().size(), is(6));
        List<ColumnHeader> headers = table.getComponent().getHeaders();

        assertThat(headers.get(0).getId(), is("id"));
        assertThat(headers.get(0).getLabel(), is("id_name"));
        assertThat(headers.get(0).getSortable(), is(true));

        assertThat(headers.get(1).getId(), is("col"));
        assertThat(headers.get(1).getLabel(), is("col_label"));
        assertThat(headers.get(1).getSortable(), nullValue());

        assertThat(headers.get(2).getId(), is("name"));
        assertThat(headers.get(2).getLabel(), is("name"));
        assertThat(headers.get(2).getSortable(), is(true));

        assertThat(headers.get(3).getId(), is("comments"));
        assertThat(headers.get(3).getLabel(), is("comments"));
        assertThat(headers.get(3).getSortable(), is(false));

        assertThat(headers.get(4).getId(), is("notInQuery"));
        assertThat(headers.get(4).getLabel(), is("notInQueryLabel"));

        QueryContext context = (QueryContext) route("/testTable4SortableCompile", CompiledQuery.class);
        assertThat(context.getSortingMap().get("id"), is("id"));
        assertThat(context.getSortingMap().get("col"), is("col_id"));
        assertThat(context.getSortingMap().get("name"), is("id"));
        assertThat(context.getSortingMap().get("comments"), is("comments"));

        assertThat(table.getComponent().getRowSelection(), is(RowSelectionEnum.radio));
        assertThat(table.getComponent().getTextWrap(), is(true));
    }

    @Test
    public void testFilters() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4FiltersCompile.page.xml")
                .get(new PageContext("testTable4FiltersCompile"));
        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);
        Filter filter = table.getFilter("name");
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getRoutable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`name`"));

        filter = table.getFilter("birthday.end");
        assertThat(filter.getFilterId(), is("birthday.end"));
        assertThat(filter.getRoutable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`birthday.end`"));

        filter = table.getFilter("birthday.begin");
        assertThat(filter.getFilterId(), is("birthday.begin"));
        assertThat(filter.getRoutable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`birthday.begin`"));

        filter = table.getFilter("gender*.name");
        assertThat(filter.getFilterId(), is("gender*.name"));
        assertThat(filter.getRoutable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`gender.map(function(t){return t.name})`"));

        filter = table.getFilter("gender*.id");
        assertThat(filter.getFilterId(), is("gender*.id"));
        assertThat(filter.getRoutable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`gender.map(function(t){return t.id})`"));


        assertThat(table.getDataProvider().getQueryMapping().size(), is(5));
        assertThat(table.getDataProvider().getQueryMapping().get("nameParam").getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("nameParam").getValue(),
                is("`name`"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_birthday_begin").getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_birthday_begin").getValue(),
                is("`birthday.begin`"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_birthday_end").getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_birthday_end").getValue(),
                is("`birthday.end`"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_gender_id").getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_gender_id").getValue(),
                is("`gender.map(function(t){return t.id})`"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_gender_name").getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("main_gender_name").getValue(),
                is("`gender.map(function(t){return t.name})`"));

        assertThat(page.getRoutes().getQueryMapping().size(), is(6));
        assertThat(page.getRoutes().getQueryMapping().get("nameParam").getOnSet().getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("nameParam").getOnSet().getValue(),
                is("`name`"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_begin").getOnSet().getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_begin").getOnSet().getValue(),
                is("`birthday.begin`"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_end").getOnSet().getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_end").getOnSet().getValue(),
                is("`birthday.end`"));
        assertThat(page.getRoutes().getQueryMapping().get("main_gender_id").getOnSet().getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("main_gender_id").getOnSet().getValue(),
                is("`gender.map(function(t){return t.id})`"));
        assertThat(page.getRoutes().getQueryMapping().get("main_gender_name").getOnSet().getBindLink(),
                is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("main_gender_name").getOnSet().getValue(),
                is("`gender.map(function(t){return t.name})`"));

        assertThat(((UpdateModelPayload) page.getRoutes().getQueryMapping().get("nameParam").
                getOnGet().getPayload()).getValue(), is(":nameParam"));
        assertThat(page.getRoutes().getQueryMapping().get("nameParam").
                getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) page.getRoutes().getQueryMapping().get("main_birthday_begin").
                getOnGet().getPayload()).getValue(), is(":main_birthday_begin"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_begin").
                getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) page.getRoutes().getQueryMapping().get("main_birthday_end").
                getOnGet().getPayload()).getValue(), is(":main_birthday_end"));
        assertThat(page.getRoutes().getQueryMapping().get("main_birthday_end").
                getOnGet().getType(), is("n2o/models/UPDATE"));

        assertThat(page.getRoutes().getQueryMapping().get("main_gender_id").
                getOnGet().getType(), is("n2o/models/UPDATE_MAP"));
        UpdateMapModelPayload genderPayload = (UpdateMapModelPayload) page.getRoutes().getQueryMapping().
                get("main_gender_id").getOnGet().getPayload();
        assertThat(genderPayload.getValue(), is(":main_gender_id"));
        assertThat(genderPayload.getMap(), is("id"));
        assertThat(genderPayload.getField(), is("gender"));

        assertThat(page.getRoutes().getQueryMapping().get("main_gender_name").getOnGet().getType(), is("n2o/models/UPDATE_MAP"));
        genderPayload = (UpdateMapModelPayload) page.getRoutes().getQueryMapping().get("main_gender_name").getOnGet().getPayload();
        assertThat(genderPayload.getValue(), is(":main_gender_name"));
        assertThat(genderPayload.getMap(), is("name"));
        assertThat(genderPayload.getField(), is("gender"));


        assertThat(table.getFilter().getHideButtons(), is(true));
        Field field = table.getFilter().getFilterFieldsets().get(0).getRows().get(3).getCols().get(0).getFields().get(0);
        assertThat(field.getId(), is("sb"));
        assertThat(field.getSrc(), is("StandardField"));
        assertThat(((StandardField) field).getControl(), instanceOf(SearchButtons.class));
        assertThat(((StandardField) field).getControl().getSrc(), is("FilterButtonsField"));

        assertThat(((StandardField<SearchButtons>) field).getControl().getResetLabel(), is("resetLabel"));
        assertThat(((StandardField<SearchButtons>) field).getControl().getSearchLabel(), is("searchLabel"));
    }

    @Test
    public void testDefaultValues() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableCompileFilters.page.xml")
                .get(new PageContext("testTableCompileFilters"));
        assertThat(page.getModels().size(), is(8));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].birthday").getValue()).
                getValues().get("begin"), is("21.10.2018"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].birthday").getValue())
                .getValues().get("end"), is("22.11.2018"));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].name").getValue(), is("test"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].gender").getValue())
                .getValues().get("name"), is("test"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].gender").getValue())
                .getValues().get("id"), is(1));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].age").getValue(), is(18));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].amount").getValue(), is("100.99"));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].hidden").getValue(), is("test"));
        assertThat(page.getModels().get("filter['testTableCompileFilters_testTable'].name2").getValue(), is("`today()`"));
        assertThat(((DefaultValues) page.getModels().get("filter['testTableCompileFilters_testTable'].birthday2").getValue())
                .getValues().get("begin"), is("`today()`"));
    }

    @Test
    public void testColumnsWidth() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new WidgetContext("testTable4SortableCompile"));
        assertThat(table.getId(), is("$testTable4SortableCompile"));
        assertThat(table.getComponent().getHeaders().size(), is(6));
        List<ColumnHeader> headers = table.getComponent().getHeaders();

        assertThat(headers.get(0).getWidth(), is("100"));
        assertThat(headers.get(1).getWidth(), nullValue());
    }

    @Test
    public void testRequiredPrefilters() {
        compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableRequiredPrefilters.page.xml")
                .get(new PageContext("testTableRequiredPrefilters"));
        QueryContext queryContext = ((QueryContext) builder.route("/testTableRequiredPrefilters", CompiledQuery.class, null));

        assertThat(queryContext.getValidations().get(0).getId(), is("gender*.id"));
        assertThat(queryContext.getValidations().get(0).getFieldId(), is("gender*.id"));
        assertThat(queryContext.getValidations().get(0).getMoment(), is(N2oValidation.ServerMoment.beforeQuery));
        assertThat(queryContext.getValidations().get(0).getSeverity(), is(SeverityType.danger));
    }

    @Test
    public void testColumnVisibility() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableColumnVisibility.page.xml")
                .get(new PageContext("testTableColumnVisibility"));
        List<ColumnHeader> columnHeaders = ((Table) page.getRegions().get("top").get(0).getContent().get(0))
                .getComponent().getHeaders();
        assertThat(columnHeaders.get(0).getVisible(), nullValue());
        assertThat(columnHeaders.get(0).getConditions().get(ValidationType.visible).get(0).getExpression(), is("abc == 1"));
        assertThat(columnHeaders.get(0).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.filter['testTableColumnVisibility_table']"));
        assertThat(columnHeaders.get(1).getVisible(), is(Boolean.TRUE));
        assertThat(columnHeaders.get(2).getVisible(), nullValue());
        assertThat(columnHeaders.get(3).getConditions().get(ValidationType.visible).get(0).getExpression(), is("type == 1"));
        assertThat(columnHeaders.get(3).getConditions().get(ValidationType.visible).get(0).getModelLink(), is("models.resolve['testTableColumnVisibility_form']"));
    }

    @Test
    public void testFilterColumns() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testFilterColumns.page.xml")
                .get(new PageContext("testFilterColumns"));
        List<ColumnHeader> columnHeaders = ((Table) page.getWidget()).getComponent().getHeaders();
        assertThat(columnHeaders.get(0), instanceOf(ColumnHeader.class));
        assertThat(columnHeaders.get(0).getId(), is("name"));
        assertThat(columnHeaders.get(0).getLabel(), is("label"));
        assertThat(columnHeaders.get(0).getFilterable(), is(true));
        assertThat(columnHeaders.get(0).getFilterControl(), instanceOf(InputText.class));
        assertThat(columnHeaders.get(0).getFilterControl().getId(), is("name"));

        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("main_name");
        assertThat(query.getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getPrefix(), is("filter"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getKey(), is("testFilterColumns_main"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getField(), is("name"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getValue(), is(":main_name"));
        assertThat(query.getOnSet().getBindLink(), is("models.filter['testFilterColumns_main']"));
        assertThat(query.getOnSet().getValue(), is("`name`"));

        BindLink link = page.getWidget().getDataProvider().getQueryMapping().get("main_name");
        assertThat(link.getValue(), is("`name`"));
        assertThat(link.getBindLink(), is("models.filter['testFilterColumns_main']"));

        List<N2oCell> cells = ((Table) page.getWidget()).getComponent().getCells();
        assertThat(cells.get(0), instanceOf(N2oBadgeCell.class));
        assertThat(cells.get(0).getId(), is("name"));
        assertThat(cells.get(1), instanceOf(N2oTextCell.class));
        assertThat(cells.get(1).getId(), is("age"));
    }

    @Test
    public void testMultiColumn() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testMultiColumn.page.xml")
                .get(new PageContext("testMultiColumn"));

        List<ColumnHeader> headers = ((Table) page.getWidget()).getComponent().getHeaders();
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
        assertThat(headers.get(0).getCssClass(), is("my-table-header"));
        assertThat(headers.get(0).getStyle().size(), is(1));
        assertThat(headers.get(0).getStyle().get("color"), is("red"));
        assertThat(headers.get(1).getId(), is("test4"));
        assertThat(headers.get(1).getMultiHeader(), is(nullValue()));
        assertThat(headers.get(1).getChildren(), nullValue());
        assertThat(headers.get(2).getId(), is("name"));
        assertThat(headers.get(2).getFilterable(), is(true));
        assertThat(headers.get(2).getFilterControl(), instanceOf(InputText.class));
        assertThat(headers.get(2).getFilterControl().getId(), is("name"));

        // проверка компиляции фильтруемого столбца внутри мульти-столбца
        PageRoutes.Query query = page.getRoutes().getQueryMapping().get("table_name");
        assertThat(query.getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getPrefix(), is("filter"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getKey(), is("testMultiColumn_table"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getField(), is("name"));
        assertThat(((UpdateModelPayload) query.getOnGet().getPayload()).getValue(), is(":table_name"));
        assertThat(query.getOnSet().getBindLink(), is("models.filter['testMultiColumn_table']"));
        assertThat(query.getOnSet().getValue(), is("`name`"));

        BindLink link = page.getWidget().getDataProvider().getQueryMapping().get("table_name");
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
    public void testPaginationDefaultParams() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4PaginationDefault.widget.xml")
                .get(new WidgetContext("testTable4PaginationDefault"));
        Pagination pagination = table.getPaging();

        assertThat(pagination.getFirst(), is(true));
        assertThat(pagination.getLast(), is(false));
        assertThat(pagination.getPrev(), is(false));
        assertThat(pagination.getNext(), is(false));
        assertThat(pagination.getShowSinglePage(), is(false));
        assertThat(pagination.getShowCount(), is(true));
        assertThat(pagination.getLayout(), is(Layout.separated));
        assertThat(pagination.getPrevLabel(), is(nullValue()));
        assertThat(pagination.getPrevIcon(), is("fa fa-angle-left"));
        assertThat(pagination.getNextLabel(), is(nullValue()));
        assertThat(pagination.getNextIcon(), is("fa fa-angle-right"));
        assertThat(pagination.getFirstLabel(), is(nullValue()));
        assertThat(pagination.getFirstIcon(), is("fa fa-angle-double-left"));
        assertThat(pagination.getLastLabel(), is(nullValue()));
        assertThat(pagination.getLastIcon(), is("fa fa-angle-double-right"));
        assertThat(pagination.getMaxPages(), is(5));
        assertThat(pagination.getClassName(), is(nullValue()));
        assertThat(pagination.getStyle(), is(nullValue()));
        assertThat(pagination.getPlace(), is(Place.bottomLeft));
    }

    @Test
    public void testPaginationNonDefaultParams() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4PaginationNonDefault.widget.xml")
                .get(new WidgetContext("testTable4PaginationNonDefault"));
        Pagination pagination = table.getPaging();

        assertThat(pagination.getFirst(), is(true));
        assertThat(pagination.getLast(), is(true));
        assertThat(pagination.getPrev(), is(true));
        assertThat(pagination.getNext(), is(true));
        assertThat(pagination.getShowSinglePage(), is(false));
        assertThat(pagination.getShowCount(), is(true));
        assertThat(pagination.getLayout(), is(Layout.flat));
        assertThat(pagination.getPrevLabel(), is("prev"));
        assertThat(pagination.getPrevIcon(), is("fa fa-angle-left"));
        assertThat(pagination.getNextLabel(), is("next"));
        assertThat(pagination.getNextIcon(), is("fa fa-angle-right"));
        assertThat(pagination.getFirstLabel(), is("first"));
        assertThat(pagination.getFirstIcon(), is("fa fa-angle-double-left"));
        assertThat(pagination.getLastLabel(), is("last"));
        assertThat(pagination.getLastIcon(), is("fa fa-angle-double-right"));
        assertThat(pagination.getMaxPages(), is(10));
        assertThat(pagination.getClassName(), is("class"));
        assertThat(pagination.getStyle(), is("style"));
        assertThat(pagination.getPlace(), is(Place.topLeft));
    }
}
