package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack(), new N2oCellsPack());
        builder.compilers(new InputSelectCompiler());
    }

    @Test
    public void testInputSelectDataProvider() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/field/testInputSelect.page.xml")
                .get(new PageContext("testInputSelect"));
        Form form = (Form) page.getWidgets().get("testInputSelect_main");

        Models models = page.getModels();
        assertThat(((DefaultValues) ((List) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues().get("id"), is(1));
        assertThat(((DefaultValues) ((List) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues().get("name"), is("test"));
        assertThat(((DefaultValues) ((List) models.get("resolve['testInputSelect_main'].testId").getValue()).get(0)).getValues().get("isTest"), is(true));

        InputSelect inputSelect = (InputSelect) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(inputSelect.getBadgeFieldId(), is("badgeFieldId"));
        assertThat(inputSelect.getBadgeColorFieldId(), is("badgeColorFieldId"));
        WidgetDataProvider wdp = inputSelect.getDataProvider();

        assertThat(wdp.getUrl(), is("n2o/data/test"));
        assertThat(wdp.getQuickSearchParam(), is("search"));
        assertThat(wdp.getQueryMapping().get("noRef").getBindLink(), is("models.resolve['testInputSelect_main']"));
        assertThat(wdp.getQueryMapping().get("noRef").getValue(), is("`someField`"));
        assertThat(wdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));

        Table table = (Table) page.getWidgets().get("testInputSelect_second");
        wdp = ((InputSelect) ((StandardField) table.getFilter().getFilterFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl()).getDataProvider();
        assertThat(wdp.getUrl(), is("n2o/data/test"));
        assertThat(wdp.getQuickSearchParam(), is("search"));
        assertThat(wdp.getQueryMapping().get("noRef").getBindLink(), is("models.filter['testInputSelect_main']"));
        assertThat(wdp.getQueryMapping().get("noRef").getValue(), is("`someField`"));
        assertThat(wdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));
    }

}