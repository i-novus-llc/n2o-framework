package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.MultiFieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.fieldset.MultiFieldsetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест филдсета с динамическим числом полей
 */
public class MultiFieldSetCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack())
                .ios(new MultiFieldsetElementIOv4());
    }

    @Test
    public void testMultiFieldSetWithField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testMultiFieldsetCompile.widget.xml")
                .get(new WidgetContext("testMultiFieldsetCompile"));
        List<FieldSet> fields = form.getComponent().getFieldsets();

        assertThat(fields.get(0).getSrc(), is("MultiFieldset"));
        assertThat(((MultiFieldSet) fields.get(0)).getName(), is("members"));
        assertThat(((MultiFieldSet) fields.get(0)).getAddButtonLabel(), is("Добавить участника"));
        assertThat(((MultiFieldSet) fields.get(0)).getRemoveAllButtonLabel(), is("Удалить всех участников"));
        assertThat(((MultiFieldSet) fields.get(0)).getCanRemoveFirstItem(), is(true));
        assertThat(((MultiFieldSet) fields.get(0)).getNeedAddButton(), is(false));
        assertThat(((MultiFieldSet) fields.get(0)).getNeedRemoveButton(), is(false));
        assertThat(((MultiFieldSet) fields.get(0)).getNeedRemoveAllButton(), is(true));
        assertThat(((MultiFieldSet) fields.get(0)).getNeedCopyButton(), is(true));

        assertThat(fields.get(1).getSrc(), is("MultiFieldset"));
        assertThat(((MultiFieldSet) fields.get(1)).getCanRemoveFirstItem(), is(false));
        assertThat(((MultiFieldSet) fields.get(1)).getNeedAddButton(), is(true));
        assertThat(((MultiFieldSet) fields.get(1)).getNeedRemoveButton(), is(true));
        assertThat(((MultiFieldSet) fields.get(1)).getNeedRemoveAllButton(), is(false));
        assertThat(((MultiFieldSet) fields.get(1)).getNeedCopyButton(), is(false));
    }
}
