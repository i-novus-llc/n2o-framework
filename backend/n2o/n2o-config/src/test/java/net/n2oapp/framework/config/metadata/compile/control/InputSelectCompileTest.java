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
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class InputSelectCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelectFetch.query.xml"));
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oCellsPack());
        builder.compilers(new InputSelectCompiler());
    }

    @Test
    public void testInputSelectDataProvider() {
        PageContext pageContext = new PageContext("testInputSelect");
        Map<String, ModelLink> queryRouteMapping = new HashMap<>();
        queryRouteMapping.put("test", new ModelLink(1));
        pageContext.setQueryRouteMapping(queryRouteMapping);
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/field/testInputSelect.page.xml")
                .get(pageContext);
        Form form = (Form) page.getRegions().get("single").get(0).getContent().get(0);

        Models models = page.getModels();
        assertThat(((DefaultValues) ((List) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues().get("id"), is(1));
        assertThat(((DefaultValues) ((List) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues().get("name"), is("test"));
        assertThat(((DefaultValues) ((List) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues().get("isTest"), is(true));

        StandardField field = ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0));
        assertThat(field.getDependencies().size(), is(1));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationType.reset));
        assertThat(field.getDependencies().get(0).getOn().get(0), is("someField"));
        assertThat(field.getDependencies().get(0).getExpression(), is("true"));

        InputSelect inputSelect = (InputSelect) field.getControl();
        assertThat(inputSelect.getSortFieldId(), is("sortName"));
        assertThat(inputSelect.getBadgeFieldId(), is("badgeFieldId"));
        assertThat(inputSelect.getBadgeColorFieldId(), is("badgeColorFieldId"));
        assertThat(inputSelect.getClosePopupOnSelect(), is(false));
        assertThat(inputSelect.getEnabledFieldId(), is("isEnabled"));
        ClientDataProvider cdp = inputSelect.getDataProvider();

        inputSelect = (InputSelect) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(inputSelect.getDataProvider().getQuickSearchParam(), is("name"));
        assertThat(inputSelect.getStatusFieldId(), nullValue());
        assertThat(inputSelect.getMaxTagTextLength(), is(5));

        assertThat(cdp.getUrl(), is("n2o/data/test"));
        assertThat(cdp.getQuickSearchParam(), is("search"));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is(1));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));

        field = ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(2).getCols().get(0).getFields().get(0));
        assertThat(field.getDependencies().get(0), instanceOf(FetchValueDependency.class));
        assertThat(field.getDependencies().get(0).getOn(), is(Arrays.asList("testId2")));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getUrl(), is("n2o/data/selectFetch"));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getQueryMapping().get("ref").getBindLink(), is("models.resolve['testInputSelect_main']"));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getQueryMapping().get("ref").getValue(), is("`testId2`"));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getDataProvider().getSize(), is(7));
        assertThat(((FetchValueDependency) field.getDependencies().get(0)).getValueFieldId(), is("name"));
        assertThat(field.getDependencies().get(0).getApplyOnInit(), is(true));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationType.fetchValue));

        CompiledQuery compiledQuery = routeAndGet("/selectFetch", CompiledQuery.class);
        assertThat(compiledQuery.getId(), is("testSelectFetch"));

        Table table = (Table) page.getRegions().get("single").get(1).getContent().get(0);
        cdp = ((InputSelect) ((StandardField) table.getFilter().getFilterFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl()).getDataProvider();
        assertThat(cdp.getUrl(), is("n2o/data/test"));
        assertThat(cdp.getQuickSearchParam(), is("search"));
        assertThat(cdp.getQueryMapping().get("noRef").getBindLink(), is("models.filter['testInputSelect_main']"));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is("`someField`"));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));
    }
}
