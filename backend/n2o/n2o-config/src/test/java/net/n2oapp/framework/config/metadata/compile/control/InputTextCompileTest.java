package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;

/**
 * Тестирование компиляции компонента ввода текста
 */
class InputTextCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testInputText() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testInputText.page.xml")
                .get(new PageContext("testInputText"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().getFirst().getRows().getFirst().getCols().getFirst().getFields().getFirst();
        List<FieldSet.Row> rows = form.getComponent().getFieldsets().getFirst().getRows();

        assertThat(field.getNoLabelBlock(), is(false));
        assertThat(field.getStyle().size(), is(2));
        assertThat(field.getStyle().get("pageBreakBefore"), is("avoid"));
        assertThat(field.getStyle().get("paddingTop"), is("0"));
        InputText inputText = (InputText) ((StandardField<?>) field).getControl();
        assertThat(inputText, allOf(
                hasProperty("style", nullValue()),
                hasProperty("src", is("InputText")),
                hasProperty("measure", is("cm")),
                hasProperty("placeholder", is("Введите текст")),
                hasProperty("autocomplete", is("on"))
        ));

        InputText inputText1 = (InputText) ((StandardField<?>) rows.get(1).getCols().getFirst().getFields().getFirst()).getControl();
        assertThat(inputText1, allOf(
                hasProperty("src", is("InputNumber")),
                hasProperty("max", is("99999999999999")),
                hasProperty("min", is("-99999999999999")),
                hasProperty("step", is("1")),
                hasProperty("measure", is("cm")),
                hasProperty("autocomplete", is("off"))
        ));
        assertThat(((StandardField<?>) rows.get(2).getCols().getFirst().getFields().getFirst()).getControl().getSrc(), is("InputNumber"));
        assertThat(((InputText) ((StandardField<?>) rows.get(2).getCols().getFirst().getFields().getFirst()).getControl()).getPrecision(), is(2));
        assertThat(((StandardField<?>) rows.get(3).getCols().getFirst().getFields().getFirst()).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField<?>) rows.get(4).getCols().getFirst().getFields().getFirst()).getControl().getSrc(), is("InputNumber"));
        assertThat(((StandardField<?>) rows.get(5).getCols().getFirst().getFields().getFirst()).getControl().getSrc(), is("InputText"));
        assertThat(((StandardField<?>) rows.get(6).getCols().getFirst().getFields().getFirst()).getControl().getSrc(), is("InputNumber"));
        assertThat(((InputText) ((StandardField<?>) rows.get(6).getCols().getFirst().getFields().getFirst()).getControl()).getPrecision(), is(8));

        assertThat(rows.getFirst().getCols().getFirst().getFields().getFirst().getHelp(), is("testHelp"));
        assertThat(rows.getFirst().getCols().getFirst().getFields().getFirst().getHelpTrigger(), is(TriggerEnum.HOVER));
        assertThat(rows.getFirst().getCols().get(1).getFields().getFirst().getHelp(), is("testHelp1_1"));
        assertThat(rows.getFirst().getCols().get(1).getFields().getFirst().getHelpTrigger(), is(TriggerEnum.CLICK));
        assertThat(rows.get(1).getCols().getFirst().getFields().getFirst().getHelp(), nullValue());

        assertThat(((InputText) ((StandardField<?>) rows.get(6).getCols().getFirst().getFields().getFirst()).getControl()).getShowButtons(), is(false));
    }

    @Test
    void testNoLabelInputText() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testInputText.page.xml")
                .get(new PageContext("testInputText"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().getFirst().getRows().getFirst().getCols().get(1).getFields().getFirst();
        assertThat(field.getLabel(), nullValue());
        assertThat(field.getNoLabelBlock(), is(true));
        assertThat(field.getLabelClass(), is("testLabelClass"));
        field = form.getComponent().getFieldsets().getFirst().getRows().getFirst().getCols().get(2).getFields().getFirst();
        assertThat(field.getLabel(), is("InputNumber"));
        assertThat(field.getNoLabel(), is("`test`"));
        assertThat(field.getNoLabelBlock(), is("`test`"));
    }
}
