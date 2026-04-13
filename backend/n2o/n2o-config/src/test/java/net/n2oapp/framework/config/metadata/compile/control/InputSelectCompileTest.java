package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.*;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.control.filters_buttons.ClearButtonCompiler;
import net.n2oapp.framework.config.metadata.compile.control.filters_buttons.SearchButtonCompiler;
import net.n2oapp.framework.config.metadata.compile.control.filters_buttons.SearchButtonsCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

class InputSelectCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelectFetch.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV3IOPack(), new N2oControlsV2IOPack(), new N2oCellsPack());
        builder.compilers(new InputSelectCompiler(), new SearchButtonsCompiler(), new SearchButtonCompiler(), new ClearButtonCompiler());
    }

    @Test
    void testInputSelectDataProvider() {
        PageContext pageContext = new PageContext("testInputSelect");
        Map<String, ModelLink> queryRouteMapping = new HashMap<>();
        queryRouteMapping.put("test", new ModelLink(1));
        pageContext.setQueryRouteMapping(queryRouteMapping);
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/field/testInputSelect.page.xml")
                .get(pageContext);
        Form form = (Form) page.getRegions().get("single").getFirst().getContent().getFirst();

        Models models = page.getModels();
        Map<String, Object> values = ((DefaultValues) ((List<?>) models.get("resolve['testInputSelect_main'].testId").getValue()).getFirst()).getValues();
        assertThat(values.get("id"), is(1));
        assertThat(values.get("name"), is("test"));
        assertThat(values.get("isTest"), is(true));

        List<FieldSet.Row> rowList = form.getComponent().getFieldsets().getFirst().getRows();
        StandardField<?> field = (StandardField<?>) rowList.getFirst().getCols().getFirst().getFields().getFirst();
        assertThat(field.getDependencies().size(), is(1));
        assertThat(field.getDependencies().getFirst(), allOf(
                hasProperty("type", is(ValidationTypeEnum.RESET)),
                hasProperty("on", contains("someField")),
                hasProperty("expression", is("true")),
                hasProperty("applyOnInit", is(false))));

        // первый input-select
        InputSelect inputSelect = (InputSelect) field.getControl();
        assertThat(inputSelect, allOf(
                hasProperty("sortFieldId", is("sortName")),
                hasProperty("closePopupOnSelect", is(false)),
                hasProperty("enabledFieldId", is("isEnabled")),
                hasProperty("placeholder", is("Введите")),
                hasProperty("size", is(20)),
                hasProperty("searchMinLength", is(2)),
                hasProperty("throttleDelay", is(200)),
                hasProperty("resetOnBlur", is(true)),
                hasProperty("labelFieldId", is("fullName")),
                hasProperty("inputLabelFieldId", is("shortName"))
        ));
        ClientDataProvider cdp = inputSelect.getDataProvider();
        assertThat(cdp, allOf(
                hasProperty("url", is("n2o/data/test")),
                hasProperty("quickSearchParam", is("search"))));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is(1));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));
        assertThat(cdp.getQueryMapping().get("parent").getValue(), is("is:notnull"));
        assertThat(cdp.getQueryMapping().get("parent2").getValue(), is("is:null"));

        // второй input-select
        inputSelect = (InputSelect) ((StandardField<?>) rowList.get(1).getCols().getFirst().getFields().getFirst()).getControl();
        assertThat(inputSelect, allOf(
                hasProperty("dataProvider", hasProperty("quickSearchParam", is("name"))),
                hasProperty("statusFieldId", nullValue()),
                hasProperty("maxTagCount", is(10)),
                hasProperty("maxTagTextLength", is(5)),
                hasProperty("searchMinLength", is(0)),
                hasProperty("throttleDelay", is(300)),
                hasProperty("labelFieldId", is("name")),
                hasProperty("inputLabelFieldId", is("name"))
        ));

        // третий input-select
        field = (StandardField<?>) rowList.get(2).getCols().getFirst().getFields().getFirst();
        assertThat(field.getDependencies().getFirst(), instanceOf(FetchValueDependency.class));
        assertThat(field.getDependencies().getFirst().getOn(), is(List.of("testId2")));
        FetchValueDependency fetchValueDependency = (FetchValueDependency) field.getDependencies().getFirst();
        assertThat(fetchValueDependency.getDataProvider().getUrl(), is("n2o/data/selectFetch"));
        assertThat(fetchValueDependency.getDataProvider().getQueryMapping().get("ref").getLink(), is("models.resolve['testInputSelect_main']"));
        assertThat(fetchValueDependency.getDataProvider().getQueryMapping().get("ref").getValue(), is("`testId2`"));
        assertThat(fetchValueDependency.getDataProvider().getSize(), is(7));
        assertThat(fetchValueDependency.getValueFieldId(), nullValue());
        assertThat(field.getDependencies().getFirst().getApplyOnInit(), is(true));
        assertThat(field.getDependencies().getFirst().getType(), is(ValidationTypeEnum.FETCH_VALUE));

        CompiledQuery compiledQuery = routeAndGet("/selectFetch", CompiledQuery.class);
        assertThat(compiledQuery.getId(), is("testSelectFetch"));

        checkInputSelectInTableFilters(page, models);
    }

    private static void checkInputSelectInTableFilters(StandardPage page, Models models) {
        Table<?> table = (Table<?>) page.getRegions().get("single").get(1).getContent().getFirst();
        InputSelect inputSelect = (InputSelect) ((StandardField<?>) table.getFilter().getFilterFieldsets().getFirst().getRows()
                .getFirst().getCols().getFirst().getFields().getFirst()).getControl();
        assertThat(inputSelect, allOf(
                hasProperty("labelFieldId", is("fullName")),
                hasProperty("inputLabelFieldId", is("fullName"))
        ));
        ClientDataProvider cdp = inputSelect.getDataProvider();
        assertThat(cdp.getUrl(), is("n2o/data/test"));
        assertThat(cdp.getQuickSearchParam(), is("search"));
        assertThat(cdp.getQueryMapping().get("noRef").getLink(), is("models.filter['testInputSelect_main']"));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is("`someField`"));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));
        assertThat(cdp.getQueryMapping().get("parent").getValue(), is("test"));
        assertThat(cdp.getQueryMapping().get("parent2").getValue(), is("test2"));

        List<?> value = (List<?>) models.get("filter['testInputSelect_second'].testId").getValue();
        Map<String, Object> values = ((DefaultValues) value.getFirst()).getValues();
        assertThat(values.get("id"), is(1));
        values = ((DefaultValues) value.get(1)).getValues();
        assertThat(values.get("id"), is(3));
    }
}
