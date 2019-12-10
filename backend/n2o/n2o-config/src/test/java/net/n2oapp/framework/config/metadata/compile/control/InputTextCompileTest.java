package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InputTextCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputTextCompiler());
    }

    @Test
    public void testInputText() {
        Form form = (Form) compile("net/n2oapp/framework/config/mapping/testInputText.widget.xml")
                .get(new WidgetContext("testInputText"));
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        InputText inputText = (InputText) ((StandardField) field).getControl();
        List<FieldSet.Row> rows = form.getComponent().getFieldsets().get(0).getRows();
        InputText inputText1 = (InputText) ((StandardField) rows.get(1).getCols().get(0).getFields().get(0)).getControl();
        assertThat(field.getDependencies().size(), is(8));
        assertThat(field.getDependencies().get(0).getExpression(), is("test2 == null"));
        assertThat(field.getDependencies().get(0).getOn().get(0), is("test2"));
        assertThat(field.getDependencies().get(0).getType(), is(ValidationType.enabled));
        assertThat(field.getDependencies().get(1).getExpression(), is("test3 == null"));
        assertThat(field.getDependencies().get(1).getOn().get(0), is("test3"));
        assertThat(field.getDependencies().get(1).getType(), is(ValidationType.required));
        assertThat(field.getDependencies().get(2).getExpression(), is("test4 == null"));
        assertThat(field.getDependencies().get(2).getOn().get(0), is("test4"));
        assertThat(field.getDependencies().get(2).getType(), is(ValidationType.reset));
        assertThat(field.getDependencies().get(3).getExpression(), is("test4 == null"));
        assertThat(field.getDependencies().get(3).getOn().get(0), is("test4"));
        assertThat(field.getDependencies().get(3).getType(), is(ValidationType.visible));
        assertThat(field.getDependencies().get(4).getExpression(), is("test4 == null"));
        assertThat(field.getDependencies().get(4).getOn().get(0), is("test4"));
        assertThat(field.getDependencies().get(4).getType(), is(ValidationType.reset));
        assertThat(field.getDependencies().get(5).getExpression(), is("test4 == null"));
        assertThat(field.getDependencies().get(5).getOn().get(0), is("test4"));
        assertThat(field.getDependencies().get(5).getType(), is(ValidationType.visible));
        assertThat(field.getDependencies().get(6).getExpression(), is("test4 == null"));
        assertThat(field.getDependencies().get(6).getOn().get(0), is("test4"));
        assertThat(field.getDependencies().get(6).getType(), is(ValidationType.visible));
        assertThat(field.getDependencies().get(7).getOn().get(0), is("name"));
        assertThat(field.getDependencies().get(7).getOn().get(1), is("type"));
        assertThat(field.getDependencies().get(7).getType(), is(ValidationType.reRender));
        assertThat(inputText.getSrc(), is("InputText"));
        assertThat(inputText1.getSrc(), is("InputNumber"));
        assertThat(inputText1.getMax(), is(Integer.MAX_VALUE));
        assertThat(inputText1.getMin(), is(Integer.MIN_VALUE));
        assertThat(inputText1.getStep(), is("1"));
        assertThat(((StandardField) rows.get(2).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField) rows.get(3).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField) rows.get(4).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField) rows.get(5).getCols().get(0).getFields().get(0)).getControl().getSrc(), is("InputText"));

        assertThat(rows.get(0).getCols().get(0).getFields().get(0).getHelp(), is("testHelp"));
        assertThat(rows.get(1).getCols().get(0).getFields().get(0).getHelp(), nullValue());

        assertThat(((InputText) ((StandardField) rows.get(6).getCols().get(0).getFields().get(0)).getControl()).getShowButtons(), is(false));
    }

    @Test
    public void testNoLabelInputText() {
        Form form = (Form) compile("net/n2oapp/framework/config/mapping/testInputText.widget.xml")
                .get(new WidgetContext("testInputText"));
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(1).getFields().get(0);
        assertThat(field.getLabel(), nullValue());
        assertThat(field.getLabelClass(), is("testLabelClass"));
    }

}
