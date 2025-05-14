package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.control.DatePicker;
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
 * Тестирование компиляции компонента ввода date-time
 */
class DatePickerCompileTest extends SourceCompileTestBase {
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
    void testDatePicker() {
        SimplePage page= (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/control/testDatePicker.page.xml")
                .get(new PageContext("testDatePicker"));
        Form form = (Form) page.getWidget();
        StandardField standardField = (StandardField) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(standardField.getLabel(), is("Дата рождения"));
        DatePicker field = (DatePicker) standardField.getControl();
        assertThat(field.getId(), is("test"));
        assertThat(field.getSrc(), is("DatePicker"));
        assertThat(field.getOutputFormat(), is("YYYY-MM-DDTHH:mm:ss"));
        assertThat(field.getPlaceholder(), is("Введите дату рождения"));
        assertThat(field.getDateFormat(), is("DD.MM.YYYY"));
        assertThat(field.getTimeFormat(), is("HH:mm"));
        assertThat(field.getMin(), is("01.01.2000"));
        assertThat(field.getMax(), is("01.01.3000"));
        assertThat(field.getUtc(), is(true));
    }
}
