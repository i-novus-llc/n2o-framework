package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.masked.MaskedInput;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции поля {@code <masked-input>}
 */
class MaskedInputCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    void testMaskedInput() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/masked/testMaskedInput.page.xml")
                .get(new PageContext("testMaskedInput"));
        Form form = (Form) page.getWidget();

        StandardField<?> field = (StandardField<?>) form.getComponent().getFieldsets().get(0)
                .getRows().get(0).getCols().get(0).getFields().get(0);
        MaskedInput input = (MaskedInput) field.getControl();
        assertThat(input.getId(), is("test"));
        assertThat(input.getSrc(), is("InputMask"));
        assertThat(input.getMask(), is("mask"));
        assertThat(input.getPlaceholder(), is("0"));
        assertThat(input.getMeasure(), is("cm"));
        assertThat(input.getClearOnBlur(), is(false));
        assertThat(input.getAutocomplete(), is("on"));
        assertThat(input.getInvalidText(), is("test invalid text"));

        field = (StandardField<?>) form.getComponent().getFieldsets().get(0).getRows().get(1)
                .getCols().get(0).getFields().get(0);
        input = (MaskedInput) field.getControl();
        assertThat(input.getId(), is("testDefault"));
        assertThat(input.getClearOnBlur(), is(true));
        assertThat(input.getAutocomplete(), is("off"));
        assertThat(input.getInvalidText(), is("Данные не соответствуют формату"));
    }
}