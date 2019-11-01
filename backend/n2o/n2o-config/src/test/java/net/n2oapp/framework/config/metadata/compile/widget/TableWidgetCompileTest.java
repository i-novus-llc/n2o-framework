package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.SearchButtons;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
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
 * Тестирование компиляции таблицы
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
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oRegionsPack(), new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(), new N2oActionsPack());
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
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(1).getId(), is("subMenu1"));
        assertThat(table.getToolbar().get("topLeft").get(0).getButtons().get(1).getSubMenu().get(0).getId(), is("testAction2"));
        //columns
        assertThat(table.getComponent().getCells().size(), is(2));
        assertThat(table.getComponent().getHeaders().size(), is(2));
        assertThat(((N2oTextCell) table.getComponent().getCells().get(0)).getCssClass(),
                is("`test == 1 ? 'css1' : test == 2 ? 'css2' : 'css3'`"));
        assertThat(((N2oTextCell) table.getComponent().getCells().get(0)).getFormat(),
                is("password"));
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
        assertThat(table.getComponent().getHasSelect(), is(true));
        assertThat(table.getComponent().getFetchOnInit(), is(false));
    }

    @Test
    public void testRowColor() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4RowColorCompile.widget.xml")
                .get(new WidgetContext("testTable4RowColorCompile"));
        assertThat(table.getComponent().getRowClass(), is("`gender.id == 1 ? 'red' : gender.id == 2 ? 'blue' : gender.id == 3 ? 'white' : 'green'`"));
        assertThat(table.getComponent().getHasSelect(), is(true));
    }

    @Test
    public void testRowClick() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4RowClickCompile.page.xml")
                .get(new PageContext("testTable4RowClickCompile"));
        List<AbstractAction> rowClicks = new ArrayList<>();
        page.getWidgets().forEach((s, widget) -> rowClicks.add((AbstractAction) ((TableWidgetComponent) widget.getComponent()).getRowClick()));

        assertThat(rowClicks.size(), is(8));
        assertThat(rowClicks.get(0), nullValue());
        assertThat(rowClicks.get(1).getEnablingCondition(), nullValue(String.class));
        assertThat(rowClicks.get(2).getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(3).getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(4).getEnablingCondition(), is("1==1"));
        assertThat(rowClicks.get(5).getEnablingCondition(), is("false"));
        assertThat(rowClicks.get(6).getEnablingCondition(), is("true"));
        assertThat(rowClicks.get(7).getEnablingCondition(), is("1==1"));
    }

    @Test
    public void testSortableColumns() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new WidgetContext("testTable4SortableCompile"));
        assertThat(table.getId(), is("$testTable4SortableCompile"));
        assertThat(table.getComponent().getHeaders().size(), is(5));
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

        assertThat(table.getComponent().getHasSelect(), is(false));
    }

    @Test
    public void testFilters() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4FiltersCompile.page.xml")
                .get(new PageContext("testTable4FiltersCompile"));
        Table table = (Table) page.getWidgets().get("testTable4FiltersCompile_main");
        Filter filter = table.getFilter("name");
        assertThat(filter.getFilterId(), is("name"));
        assertThat(filter.getReloadable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`name`"));

        filter = table.getFilter("birthday.end");
        assertThat(filter.getFilterId(), is("birthday.end"));
        assertThat(filter.getReloadable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`birthday.end`"));

        filter = table.getFilter("birthday.begin");
        assertThat(filter.getFilterId(), is("birthday.begin"));
        assertThat(filter.getReloadable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`birthday.begin`"));

        filter = table.getFilter("gender*.name");
        assertThat(filter.getFilterId(), is("gender*.name"));
        assertThat(filter.getReloadable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`gender.map(function(t){return t.name})`"));

        filter = table.getFilter("gender*.id");
        assertThat(filter.getFilterId(), is("gender*.id"));
        assertThat(filter.getReloadable(), is(true));
        assertThat(filter.getLink().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(filter.getLink().getValue(), is("`gender.map(function(t){return t.id})`"));


        assertThat(table.getDataProvider().getQueryMapping().size(), is(5));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_name").getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_name").getValue(), is("`name`"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_birthday_begin").getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_birthday_begin").getValue(), is("`birthday.begin`"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_birthday_end").getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_birthday_end").getValue(), is("`birthday.end`"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getValue(), is("`gender.map(function(t){return t.id})`"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(table.getDataProvider().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getValue(), is("`gender.map(function(t){return t.name})`"));

        assertThat(page.getRoutes().getQueryMapping().size(), is(6));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_name").getOnSet().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_name").getOnSet().getValue(), is("`name`"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_begin").getOnSet().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_begin").getOnSet().getValue(), is("`birthday.begin`"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_end").getOnSet().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_end").getOnSet().getValue(), is("`birthday.end`"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getOnSet().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getOnSet().getValue(), is("`gender.map(function(t){return t.id})`"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getOnSet().getBindLink(), is("models.filter['testTable4FiltersCompile_main']"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getOnSet().getValue(), is("`gender.map(function(t){return t.name})`"));

        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_name").getOnGet().getPayload().get("value"), is(":testTable4FiltersCompile_main_name"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_name").getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_begin").getOnGet().getPayload().get("value"), is(":testTable4FiltersCompile_main_birthday_begin"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_begin").getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_end").getOnGet().getPayload().get("value"), is(":testTable4FiltersCompile_main_birthday_end"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_birthday_end").getOnGet().getType(), is("n2o/models/UPDATE"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getOnGet().getPayload().get("value"), is(":testTable4FiltersCompile_main_gender_id"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getOnGet().getType(), is("n2o/models/UPDATE_MAP"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getOnGet().getPayload().get("map"), is("id"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_id").getOnGet().getPayload().get("field"), is("gender"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getOnGet().getPayload().get("value"), is(":testTable4FiltersCompile_main_gender_name"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getOnGet().getPayload().get("map"), is("name"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getOnGet().getPayload().get("field"), is("gender"));
        assertThat(page.getRoutes().getQueryMapping().get("testTable4FiltersCompile_main_gender_name").getOnGet().getType(), is("n2o/models/UPDATE_MAP"));

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
    public void testColumnsWidth() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTable4SortableCompile.widget.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml")
                .get(new WidgetContext("testTable4SortableCompile"));
        assertThat(table.getId(), is("$testTable4SortableCompile"));
        assertThat(table.getComponent().getHeaders().size(), is(5));
        List<ColumnHeader> headers = table.getComponent().getHeaders();

        assertThat(headers.get(0).getWidth(), is("100"));
        assertThat(headers.get(1).getWidth(), nullValue());
    }

    @Test
    public void testRequiredPrefilters() {
        compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableRequiredPrefilters.page.xml")
                .get(new PageContext("testTableRequiredPrefilters"));
        QueryContext queryContext = ((QueryContext) builder.route("/testTableRequiredPrefilters", CompiledQuery.class));

        assertThat(queryContext.getValidations().get(0).getId(), is("gender*.id"));
        assertThat(queryContext.getValidations().get(0).getFieldId(), is("gender*.id"));
        assertThat(queryContext.getValidations().get(0).getMoment(), is(N2oValidation.ServerMoment.beforeQuery));
        assertThat(queryContext.getValidations().get(0).getSeverity(), is(SeverityType.danger));
    }

    @Test
    public void testColumnVisibility() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/widgets/testTableColumnVisibility.page.xml")
                .get(new PageContext("testTableColumnVisibility"));
        List<ColumnHeader> columnHeaders = ((Table) page.getWidgets().entrySet().iterator().next().getValue()).getComponent().getHeaders();
        assertThat(columnHeaders.get(0).getVisible(), is(Boolean.FALSE));
        assertThat(columnHeaders.get(1).getVisible(), is(Boolean.TRUE));
    }
}
