package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.MultiFieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
        builder.packs(new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack());
    }

    @Test
    public void testMultiFieldSetWithField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testMultiFieldsetCompile.page.xml")
                .get(new PageContext("testMultiFieldsetCompile"));
        Form form = (Form) page.getWidget();
        List<FieldSet> fieldsets = form.getComponent().getFieldsets();

        MultiFieldSet multiFieldSet = (MultiFieldSet) fieldsets.get(0);
        assertThat(multiFieldSet.getSrc(), is("MultiFieldset"));
        assertThat(multiFieldSet.getLabel(), is("Заголовок"));
        assertThat(multiFieldSet.getChildrenLabel(), is("`'Участник '+index`"));
        assertThat(multiFieldSet.getFirstChildrenLabel(), is("Участник"));
        assertThat(multiFieldSet.getName(), is("members"));
        assertThat(multiFieldSet.getAddButtonLabel(), is("Добавить участника"));
        assertThat(multiFieldSet.getRemoveAllButtonLabel(), is("Удалить всех участников"));
        assertThat(multiFieldSet.getCanRemoveFirstItem(), is(true));
        assertThat(multiFieldSet.getNeedAddButton(), is(false));
        assertThat(multiFieldSet.getNeedRemoveButton(), is(false));
        assertThat(multiFieldSet.getNeedRemoveAllButton(), is(true));
        assertThat(multiFieldSet.getNeedCopyButton(), is(true));
        assertThat(multiFieldSet.getDescription(), is("description"));

        MultiFieldSet multiFieldSet2 = (MultiFieldSet) fieldsets.get(1);
        assertThat(multiFieldSet2.getSrc(), is("test"));
        assertThat(multiFieldSet2.getChildrenLabel(), is("`members[index].name`"));
        assertThat(multiFieldSet2.getFirstChildrenLabel(), is(nullValue()));
        assertThat(multiFieldSet2.getCanRemoveFirstItem(), is(false));
        assertThat(multiFieldSet2.getNeedAddButton(), is(true));
        assertThat(multiFieldSet2.getNeedRemoveButton(), is(true));
        assertThat(multiFieldSet2.getNeedRemoveAllButton(), is(false));
        assertThat(multiFieldSet2.getNeedCopyButton(), is(false));
        assertThat(multiFieldSet2.getDescription(), nullValue());
    }
}
