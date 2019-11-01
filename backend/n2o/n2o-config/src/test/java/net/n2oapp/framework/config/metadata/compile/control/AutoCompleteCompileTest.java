package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.control.AutoComplete;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AutoCompleteCompileTest extends SourceCompileTestBase {
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
        builder.compilers(new AutoCompleteCompiler());
    }

    @Test
    public void testAutoCompleteDataProvider() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/field/testAutoComplete.page.xml")
                .get(new PageContext("testAutoComplete"));
        Form form = (Form) page.getWidgets().get("testAutoComplete_main");
        StandardField<AutoComplete> field = (StandardField<AutoComplete>) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getControl().getDataProvider().getUrl(), is("n2o/data/test"));
        assertThat(field.getControl().getDataProvider().getQuickSearchParam(), is("search"));
        assertThat(field.getControl().getValueFieldId(), is("name"));

        field = (StandardField<AutoComplete>) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0);
        assertThat(field.getControl().getData().get(0).get("name"), is("test1"));
        assertThat(field.getControl().getData().get(1).get("name"), is("test2"));
        assertThat(field.getControl().getValueFieldId(), is("name"));
    }

}