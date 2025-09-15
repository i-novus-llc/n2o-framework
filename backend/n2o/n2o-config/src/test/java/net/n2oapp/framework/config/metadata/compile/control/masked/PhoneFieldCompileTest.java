package net.n2oapp.framework.config.metadata.compile.control.masked;

import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.masked.PhoneField;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции поля {@code <phone>}
 */
class PhoneFieldCompileTest extends SourceCompileTestBase {

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
    void testPhone() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/masked/testPhoneField.page.xml")
                .get(new PageContext("testPhoneField"));
        Form form = (Form) page.getWidget();
        Field field = form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        PhoneField phone = (PhoneField) ((StandardField<?>) field).getControl();

        assertThat(phone.getId(), is("testId"));
        assertThat(phone.getCountries().get(0), is("RU"));
        assertThat(phone.getCountries().get(1), is("KZ"));
        assertThat(phone.getInvalidText(), is("Телефон не соответствует формату"));
    }
}
