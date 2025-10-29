package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.control.*;
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
        Form form = (Form) page.getRegions().get("single").get(0).getContent().get(0);

        Models models = page.getModels();
        Map<String, Object> values = ((DefaultValues) ((List<?>) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues();
        assertThat(values.get("id"), is(1));
        assertThat(values.get("name"), is("test"));
        assertThat(values.get("isTest"), is(true));

        StandardField<?> field = ((StandardField<?>) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0));
        assertThat(field.getDependencies().size(), is(1));
        assertThat(field.getDependencies().get(0), allOf(
                hasProperty("type", is(ValidationTypeEnum.RESET)),
                hasProperty("on", contains("someField")),
                hasProperty("expression", is("true")),
                hasProperty("applyOnInit", is(false))));

        InputSelect inputSelect = (InputSelect) field.getControl();
        assertThat(inputSelect, allOf(
                hasProperty("sortFieldId", is("sortName")),
                hasProperty("closePopupOnSelect", is(false)),
                hasProperty("enabledFieldId", is("isEnabled")),
                hasProperty("placeholder", is("Введите")),
                hasProperty("size", is(20)),
                hasProperty("searchMinLength", is(2)),
                hasProperty("throttleDelay", is(200)),
                hasProperty("resetOnBlur", is(true))
        ));
        ClientDataProvider cdp = inputSelect.getDataProvider();

        inputSelect = (InputSelect) ((StandardField<?>) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(inputSelect, allOf(
                hasProperty("dataProvider", hasProperty("quickSearchParam", is("name"))),
                hasProperty("statusFieldId", nullValue()),
                hasProperty("maxTagCount", is(10)),
                hasProperty("maxTagTextLength", is(5)),
                hasProperty("searchMinLength", is(0)),
                hasProperty("throttleDelay", is(300))));

        assertThat(cdp, allOf(
                hasProperty("url", is("n2o/data/test")),
                hasProperty("quickSearchParam", is("search"))));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is(1));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));

        field = ((StandardField<?>) form.getComponent().getFieldsets().get(0).getRows()
                .get(2).getCols().get(0).getFields().get(0));
        assertThat(field.getDependencies().get(0), instanceOf(FetchValueDependency.class));
        assertThat(field.getDependencies().get(0).getOn(), is(List.of("testId2")));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getUrl(), is("n2o/data/selectFetch"));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getQueryMapping().get("ref").getLink(), is("models.resolve['testInputSelect_main']"));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getQueryMapping().get("ref").getValue(), is("`testId2`"));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getSize(), is(7));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getValueFieldId(), nullValue());
        assertThat(field.getDependencies().get(0).getApplyOnInit(), is(true));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationTypeEnum.FETCH_VALUE));

        CompiledQuery compiledQuery = routeAndGet("/selectFetch", CompiledQuery.class);
        assertThat(compiledQuery.getId(), is("testSelectFetch"));

        checkInputSelectInTableFilters(page, models);
    }

    private static void checkInputSelectInTableFilters(StandardPage page, Models models) {
        Table<?> table = (Table<?>) page.getRegions().get("single").get(1).getContent().get(0);
        ClientDataProvider cdp = ((InputSelect) ((StandardField<?>) table.getFilter().getFilterFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl()).getDataProvider();
        assertThat(cdp.getUrl(), is("n2o/data/test"));
        assertThat(cdp.getQuickSearchParam(), is("search"));
        assertThat(cdp.getQueryMapping().get("noRef").getLink(), is("models.filter['testInputSelect_main']"));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is("`someField`"));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));

        List<?> value = (List<?>) models.get("filter['testInputSelect_second'].testId").getValue();
        Map<String, Object> values = ((DefaultValues) value.get(0)).getValues();
        assertThat(values.get("id"), is(1));
        values = ((DefaultValues) value.get(1)).getValues();
        assertThat(values.get("id"), is(3));
    }
}
