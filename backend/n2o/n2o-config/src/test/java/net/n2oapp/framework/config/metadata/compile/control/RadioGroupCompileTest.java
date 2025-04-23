package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.RadioGroup;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RadioGroupCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new RadioGroupCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/field/testSelect.query.xml"));
    }

    @Test
    void testRadioGroupTypes() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testRadioGroup.page.xml")
                .get(new PageContext("testRadioGroup", "/test"));
        Form form = (Form) page.getWidget();
        RadioGroup radioGroup = (RadioGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(radioGroup.getType().getId(), is("default"));
        assertThat(radioGroup.getInline(), is(false));


        radioGroup = (RadioGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(radioGroup.getType().getId(), is("tabs"));
        assertThat(radioGroup.getInline(), is(true));

        radioGroup = (RadioGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(2).getCols().get(0).getFields().get(0)).getControl();
        assertThat(radioGroup.getType().getId(), is("tabs"));
        assertThat(radioGroup.getInline(), is(false));
    }
}
