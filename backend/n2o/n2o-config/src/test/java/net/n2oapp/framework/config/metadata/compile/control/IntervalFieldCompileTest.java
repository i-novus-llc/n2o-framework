package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.IntervalField;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntervalFieldCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsPack());

    }

    @Test
    public void testIntervalField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/control/testIntervalField.widget" +
                ".xml", "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new WidgetContext("testIntervalField"));
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        IntervalField intervalField = (IntervalField) field;
        InputText beginControl = (InputText) intervalField.getBeginControl();
        InputText endControl = (InputText) intervalField.getEndControl();
        assertThat(beginControl.getId(), is("begin"));
        assertThat(beginControl.getMin(), is(0));
        assertThat(endControl.getId(), is("end"));
        assertThat(endControl.getMax(), is(10));
    }
}
