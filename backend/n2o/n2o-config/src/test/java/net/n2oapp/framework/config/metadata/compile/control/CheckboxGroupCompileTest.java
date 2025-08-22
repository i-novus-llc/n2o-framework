package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.control.CheckboxGroup;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class CheckboxGroupCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
    }

    @Test
    void testCheckboxGroup() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testCheckboxGroup.page.xml")
               .get(new PageContext("testCheckboxGroup", "/test"));
        Form form = (Form) page.getWidget();

        ClientDataProvider cdp = ((ListControl) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl()).getDataProvider();

        assertThat(cdp.getUrl(), is("n2o/data/test"));
        assertThat(cdp.getQueryMapping().get("noRef").getValue(), is("`someField`"));
        assertThat(cdp.getQueryMapping().get("countries").getValue(), is(Arrays.asList(1, 2, 3)));

        String format = ((ListControl) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl()).getFormat();
        assertThat(format, nullValue());

        Map<String, PageRoutes.Query> queryMapping = page.getRoutes().getQueryMapping();
        assertThat(queryMapping.size(), is(1));
        assertThat(queryMapping.get("genders").getOnSet().getLink(), is("models.resolve['test_w1']"));
        assertThat(queryMapping.get("genders").getOnSet().getValue(), is("`gender.map(function(t){return t.id})`"));


        format = ((ListControl) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl()).getFormat();
        assertThat(format, is("`name+'  '+inn`"));

        format = ((ListControl) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(2).getCols().get(0).getFields().get(0)).getControl()).getFormat();
        assertThat(format, is("password"));
    }

    @Test
    void testCheckboxGroupTypes() {
        SimplePage page =(SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testCheckboxGroupTypes.page.xml")
                .get(new PageContext("testCheckboxGroupTypes", "/test"));
        Form form = (Form) page.getWidget();
        CheckboxGroup checkboxGroup = (CheckboxGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(checkboxGroup.getType().getId(), is("default"));
        assertThat(checkboxGroup.getInline(), is(false));

        checkboxGroup = (CheckboxGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(checkboxGroup.getType().getId(), is("btn"));
        assertThat(checkboxGroup.getInline(), is(true));
    }
}
