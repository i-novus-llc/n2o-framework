package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.LineFieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование филдсета с горизонтальной линией
 */
public class LineFieldSetCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack());
    }

    @Test
    public void testLineFieldSetWithField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testLineFieldsetCompile.widget.xml")
                .get(new WidgetContext("testLineFieldsetCompile"));
        List<FieldSet> fields = form.getComponent().getFieldsets();
        assertThat(fields.size(), is(2));

        LineFieldSet lineFieldSet = (LineFieldSet) fields.get(0);
        assertThat(lineFieldSet.getSrc(), is("LineFieldset"));
        assertThat(lineFieldSet.getLabel(), is(nullValue()));
        assertThat(lineFieldSet.getCollapsible(), is(true));
        assertThat(lineFieldSet.getHasSeparator(), is(true));
        assertThat(lineFieldSet.getExpand(), is(true));
        assertThat(lineFieldSet.getDescription(), nullValue());

        LineFieldSet lineFieldSet2 = (LineFieldSet) fields.get(1);
        assertThat(lineFieldSet2.getSrc(), is("testLine"));
        assertThat(lineFieldSet2.getLabel(), is("test"));
        assertThat(lineFieldSet2.getCollapsible(), is(false));
        assertThat(lineFieldSet2.getHasSeparator(), is(false));
        assertThat(lineFieldSet2.getExpand(), is(false));
        assertThat(lineFieldSet2.getDescription(), is("description"));
    }
}
