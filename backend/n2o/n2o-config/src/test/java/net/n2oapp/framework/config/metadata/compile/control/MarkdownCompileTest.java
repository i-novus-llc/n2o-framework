package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.control.Markdown;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции Markdown
 */
public class MarkdownCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    public void testMarkdown() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/field/testMarkdown.page.xml")
                .get(new PageContext("testMarkdown"));
        Form form = (Form) page.getWidget();

        Markdown field = (Markdown) form.getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0);
        assertThat(field.getSrc(), is("MarkdownField"));
        assertThat(field.getContent(), containsString("`'markdown text '+name_param+' type '+param_type`"));
        assertThat(field.getVisible(), is(false));
        assertThat(field.getDependencies().get(0).getExpression(), is("name != null"));
        assertThat(field.getLabel(), is("markdownLabel"));
        assertThat(field.getRequired(), is(true));
        assertThat(field.getActions().size(), is(2));
        assertThat(((LinkAction)field.getActions().get("act1")).getUrl(), is("http://yandex.ru"));
        assertThat(((LinkAction)field.getActions().get("act2")).getUrl(), is("http://i-novus.ru"));
    }

}
