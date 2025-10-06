package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.masked.EmailField;
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
 * Тестирование компиляции поля {@code <email>}
 */
class EmailFieldCompileTest extends SourceCompileTestBase {

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
    void testEmail() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/masked/testEmailField.page.xml")
                .get(new PageContext("testEmailField"));
        Form form = (Form) page.getWidget();
        StandardField<?> email = (StandardField<?>) form.getComponent().getFieldsets().get(0).getRows().get(0)
                .getCols().get(0).getFields().get(0);

        assertThat(email.getControl().getId(), is("testId"));
        assertThat(email.getControl().getSrc(), is("Inputs/Email"));
        assertThat(((EmailField) email.getControl()).getInvalidText(), is("Email не соответствует формату"));
    }
}
