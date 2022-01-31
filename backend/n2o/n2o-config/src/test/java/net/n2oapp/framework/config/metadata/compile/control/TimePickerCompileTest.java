package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.TimePicker;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV2IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента ввода времени
 */
public class TimePickerCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new TimePickerCompiler(), new CustomFieldCompiler());
    }

    @Test
    public void testTimePicker() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testTimePicker.page.xml")
                .get(new PageContext("testTimePicker"));
        Form form = (Form) page.getWidget();
        TimePicker timePicker = (TimePicker) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl();

        assertThat(timePicker.getId(), is("test1"));
        assertThat(timePicker.getSrc(), is("testSrc"));
        assertThat(timePicker.getPrefix(), is("from: "));
        assertThat(timePicker.getFormat(), is("digit"));
        assertThat(timePicker.getMode().length, is(2));
        assertThat(timePicker.getMode()[0], is("hours"));
        assertThat(timePicker.getMode()[1], is("minutes"));
        assertThat(timePicker.getTimeFormat(), is("hh:mm"));

        timePicker = (TimePicker) ((StandardField) form.getComponent().getFieldsets().get(0).getRows().get(1).getCols().get(0).getFields().get(0)).getControl();

        assertThat(timePicker.getId(), is("test2"));
        assertThat(timePicker.getSrc(), is("TimePicker"));
        assertThat(timePicker.getPrefix(), is(nullValue()));
        assertThat(timePicker.getFormat(), is("symbols"));
        assertThat(timePicker.getMode().length, is(3));
        assertThat(timePicker.getMode()[0], is("hours"));
        assertThat(timePicker.getMode()[1], is("minutes"));
        assertThat(timePicker.getMode()[2], is("seconds"));
        assertThat(timePicker.getTimeFormat(), is("HH:mm:ss"));
    }

}
