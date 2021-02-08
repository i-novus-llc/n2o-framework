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
 * Тестирование филдсета с динамическим числом полей
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
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack());
    }

    @Test
    public void testMultiFieldSetWithField() {
        Form form = (Form) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testMultiFieldsetCompile.widget.xml")
                .get(new WidgetContext("testMultiFieldsetCompile"));
        List<FieldSet> fieldsets = form.getComponent().getFieldsets();

        MultiFieldSet multiFieldSet = (MultiFieldSet) fieldsets.get(0);
        assertThat(multiFieldSet.getSrc(), is("MultiFieldset"));
        assertThat(multiFieldSet.getLabel(), is("Заголовок"));
        assertThat(multiFieldSet.getChildrenLabel(), is("`'Участник '+index`"));
        assertThat(multiFieldSet.getName(), is("members"));
        assertThat(multiFieldSet.getAddButtonLabel(), is("Добавить участника"));
        assertThat(multiFieldSet.getRemoveAllButtonLabel(), is("Удалить всех участников"));
        assertThat(multiFieldSet.getCanRemoveFirstItem(), is(true));
        assertThat(multiFieldSet.getNeedAddButton(), is(false));
        assertThat(multiFieldSet.getNeedRemoveButton(), is(false));
        assertThat(multiFieldSet.getNeedRemoveAllButton(), is(true));
        assertThat(multiFieldSet.getNeedCopyButton(), is(true));

        MultiFieldSet multiFieldSet2 = (MultiFieldSet) fieldsets.get(1);
        assertThat(multiFieldSet2.getSrc(), is("test"));
        assertThat(multiFieldSet2.getChildrenLabel(), is("`members[index].name`"));
        assertThat(multiFieldSet2.getCanRemoveFirstItem(), is(false));
        assertThat(multiFieldSet2.getNeedAddButton(), is(true));
        assertThat(multiFieldSet2.getNeedRemoveButton(), is(true));
        assertThat(multiFieldSet2.getNeedRemoveAllButton(), is(false));
        assertThat(multiFieldSet2.getNeedCopyButton(), is(false));
    }
}
