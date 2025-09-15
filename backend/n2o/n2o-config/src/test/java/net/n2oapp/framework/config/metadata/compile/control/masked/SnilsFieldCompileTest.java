package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.meta.control.masked.SnilsField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
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
 * Тестирование компиляции поля {@code <snils>}
 */
class SnilsFieldCompileTest extends SourceCompileTestBase {

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
    void testSnils() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/masked/testSnilsField.page.xml")
                .get(new PageContext("testSnilsField"));
        Form form = (Form) page.getWidget();
        StandardField<?> snils = (StandardField<?>) form.getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0);

        assertThat(snils.getControl().getId(), is("testId"));
        assertThat(snils.getLabel(), is("test"));
        assertThat(snils.getControl().getSrc(), is("Inputs/SNILS"));
        assertThat(((SnilsField) snils.getControl()).getInvalidText(), is("СНИЛС не соответствует формату"));
    }
}
