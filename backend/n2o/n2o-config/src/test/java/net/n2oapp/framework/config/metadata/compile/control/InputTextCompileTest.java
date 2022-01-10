package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции компонента ввода текста
 */
public class InputTextCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputTextCompiler());
    }

    @Test
    public void testInputText() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testInputText.page.xml")
                .get(new PageContext("testInputText"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        List<FieldSet.Row> rows = form.getComponent().getFieldsets().get(0).getRows();

        assertThat(field.getStyle().size(), is(2));
        assertThat(field.getStyle().get("pageBreakBefore"), is("avoid"));
        assertThat(field.getStyle().get("paddingTop"), is("0"));
        assertThat(field.getNoLabelBlock(), is(false));
        InputText inputText = (InputText) ((StandardField) field).getControl();
        assertThat(inputText.getSrc(), is("InputText"));
        assertThat(inputText.getMeasure(), is("cm"));
        InputText inputText1 = (InputText) ((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(inputText1.getSrc(), is("InputNumber"));
        assertThat(inputText1.getMax(), is("99999999999999"));
        assertThat(inputText1.getMin(), is("-99999999999999"));
        assertThat(inputText1.getStep(), is("1"));
        assertThat(inputText1.getMeasure(), is("cm"));
        assertThat(((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((InputText) ((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl()).getPrecision(), is(2));
        assertThat(((StandardField) rows.get(3).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField) rows.get(4).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField) rows.get(5).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputText"));
        assertThat(((StandardField) rows.get(6).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((InputText) ((StandardField) rows.get(6).getCols().get(0).getFields().get(0)).getControl()).getPrecision(), is(8));

        assertThat(rows.get(0).getCols().get(0).getFields().get(0).getHelp(), is("testHelp"));
        assertThat(rows.get(1).getCols().get(0).getFields().get(0).getHelp(), nullValue());

        assertThat(((InputText) ((StandardField) rows.get(6).getCols().get(0).getFields().get(0)).getControl()).getShowButtons(), is(false));
    }

    @Test
    public void testNoLabelInputText() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testInputText.page.xml")
                .get(new PageContext("testInputText"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(1).getFields().get(0);
        assertThat(field.getLabel(), nullValue());
        assertThat(field.getNoLabelBlock(), is(true));
        assertThat(field.getLabelClass(), is("testLabelClass"));
    }
}
