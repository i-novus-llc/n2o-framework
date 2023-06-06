package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.fieldset.LineFieldSet;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование филдсета с горизонтальной линией
 */
public class LineFieldSetCompileTest extends SourceCompileTestBase {

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
    void testLineFieldSetWithField() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/fieldset/testLineFieldsetCompile.page.xml")
                .get(new PageContext("testLineFieldsetCompile"));
        Form form = (Form) page.getWidget();
        List<FieldSet> fields = form.getComponent().getFieldsets();
        assertThat(fields.size(), is(4));

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

        LineFieldSet lineFieldSetBadge = (LineFieldSet) fields.get(2);
        assertThat(lineFieldSetBadge.getBadge().getText(), is("`test`"));
        assertThat(lineFieldSetBadge.getBadge().getPosition().getId(), is("right"));
        assertThat(lineFieldSetBadge.getBadge().getShape().getId(), is("square"));
        assertThat(lineFieldSetBadge.getBadge().getImagePosition().getId(), is("left"));
        assertThat(lineFieldSetBadge.getBadge().getImageShape().getId(), is("circle"));

        lineFieldSetBadge = (LineFieldSet) fields.get(3);
        assertThat(lineFieldSetBadge.getBadge().getText(), is("text"));
        assertThat(lineFieldSetBadge.getBadge().getPosition().getId(), is("left"));
        assertThat(lineFieldSetBadge.getBadge().getShape().getId(), is("circle"));
        assertThat(lineFieldSetBadge.getBadge().getImage(), is("test"));
        assertThat(lineFieldSetBadge.getBadge().getImagePosition().getId(), is("right"));
        assertThat(lineFieldSetBadge.getBadge().getImageShape().getId(), is("square"));

    }
}
