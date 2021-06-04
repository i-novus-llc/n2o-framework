package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.RadioGroup;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RadioGroupCompileTest extends SourceCompileTestBase {
    @Override
    @Before
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
    public void testRadioGroupTypes() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/field/testRadioGroup.widget.xml")
                .get(new WidgetContext("testRadioGroup", "/test"));

        RadioGroup radioGroup = (RadioGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
        .get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(radioGroup.getType().getId(), is("default"));

        radioGroup = (RadioGroup) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(radioGroup.getType().getId(), is("tabs"));
    }
}
