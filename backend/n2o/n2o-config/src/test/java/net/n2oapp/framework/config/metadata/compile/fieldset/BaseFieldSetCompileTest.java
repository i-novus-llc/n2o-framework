package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции базового филдсета
 */
public class BaseFieldSetCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack());
    }

    @Test
    void testBaseFieldSet() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testBaseFieldSetCompile.page.xml")
                .get(new PageContext("testBaseFieldSetCompile"));

        FieldSet fieldSet = ((Form) page.getWidget()).getComponent().getFieldsets().get(0);

        assertThat(fieldSet.getLabel(), is("`label`"));
        assertThat(fieldSet.getHelp(), is("`'This is '+help`"));
        assertThat(fieldSet.getDescription(), is("description"));
        assertThat(fieldSet.getLabelPosition(), is(FieldSet.LabelPosition.LEFT));
        assertThat(fieldSet.getLabelAlignment(), is(FieldSet.LabelAlignment.RIGHT));
        assertThat(fieldSet.getLabelWidth(), is("30px"));
        assertThat(fieldSet.getVisible(), is(true));
        assertThat(fieldSet.getEnabled(), is(false));
        assertThat(fieldSet.getJsonProperties().get("codeVerified"), CoreMatchers.is("`emailSender.status=='send'`"));


        fieldSet = ((Form) page.getWidget()).getComponent().getFieldsets().get(1);


        assertThat(fieldSet.getLabel(), nullValue());
        assertThat(fieldSet.getHelp(), nullValue());
        assertThat(fieldSet.getDescription(), nullValue());
        assertThat(fieldSet.getLabelPosition(), nullValue());
        assertThat(fieldSet.getLabelAlignment(), nullValue());
        assertThat(fieldSet.getLabelWidth(), nullValue());
        assertThat(fieldSet.getVisible(), nullValue());
        assertThat(fieldSet.getEnabled(), nullValue());
        assertThat(fieldSet.getLabelWidth(), nullValue());
        assertThat(fieldSet.getJsonProperties(), nullValue()) ;

    }
}
