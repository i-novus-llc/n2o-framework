package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.LineFieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.fieldset.ColElementIO4;
import net.n2oapp.framework.config.io.fieldset.LineFieldsetElementIOv4;
import net.n2oapp.framework.config.io.fieldset.RowElementIO4;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест филсета с горизонтальной линией
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(), new N2oFieldSetsPack())
                .ios(new SetFieldsetElementIOv4(), new ColElementIO4(), new RowElementIO4(), new LineFieldsetElementIOv4())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/fieldset/testLineFieldsetCompileWithFields.fieldset.xml"));
    }

    @Test
    public void testLineFieldSetWithField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testLineFieldsetCompile.widget.xml")
                .get(new WidgetContext("testLineFieldsetCompile"));
        FormWidgetComponent component = form.getComponent();
        List<FieldSet> fields = component.getFieldsets();
        assertThat(fields.size(), is(4));

        assertThat(fields.get(0), instanceOf(LineFieldSet.class));
        assertThat(fields.get(1), instanceOf(LineFieldSet.class));
        assertThat(fields.get(2), instanceOf(LineFieldSet.class));
        assertThat(fields.get(3), instanceOf(LineFieldSet.class));

        assertThat(((LineFieldSet)fields.get(0)).getHasArrow(), is(true));
        assertThat(((LineFieldSet)fields.get(0)).getCollapsible(), is(false));

        assertThat(((LineFieldSet)fields.get(1)).getLabel(), is("test"));
        assertThat(((LineFieldSet)fields.get(1)).getHasArrow(), is(true));
        assertThat(((LineFieldSet)fields.get(1)).getCollapsible(), is(false));

        assertThat(((LineFieldSet)fields.get(2)).getHasArrow(), is(true));
        assertThat(((LineFieldSet)fields.get(2)).getCollapsible(), is(true));
        assertThat(((LineFieldSet)fields.get(2)).getExpand(), is(true));

        assertThat(((LineFieldSet)fields.get(3)).getHasArrow(), is(true));
        assertThat(((LineFieldSet)fields.get(3)).getCollapsible(), is(true));
        assertThat(((LineFieldSet)fields.get(3)).getExpand(), is(false));
    }

}
