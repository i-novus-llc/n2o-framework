package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.NumberPicker;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
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
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента выбора числа из диапазона
 */
public class NumberPickerCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new NumberPickerCompiler());
    }

    @Test
    public void testNumberPicker() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/mapping/testNumberPicker.page.xml")
                .get(new PageContext("testNumberPicker"));
        Form form = (Form) page.getWidget();
        NumberPicker numberPicker = (NumberPicker) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(0).getCols().get(0).getFields().get(0)).getControl();

        assertThat(numberPicker.getSrc(), is("testSrc"));
        assertThat(numberPicker.getMin(), is(1));
        assertThat(numberPicker.getMax(), is(10));
        assertThat(numberPicker.getStep(), is(2));

        numberPicker = (NumberPicker) ((StandardField) form.getComponent().getFieldsets().get(0).getRows()
                .get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(numberPicker.getSrc(), is("NumberPicker"));
        assertThat(numberPicker.getMin(), is(0));
        assertThat(numberPicker.getMax(), is(100));
        assertThat(numberPicker.getStep(), is(1));
    }
}