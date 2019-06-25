package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.metadata.compile.widget.FormCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.N2oFormMerger;
import net.n2oapp.framework.config.metadata.compile.widget.N2oWidgetMerger;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест слияния виджетов
 */
public class N2oWidgetMergerTest extends SourceMergerTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsPack(), new N2oFieldSetsPack(), new N2oControlsPack())
                .ios(new FormElementIOV4())
                .compilers(new FormCompiler())
                .mergers(new N2oWidgetMerger<>(), new N2oFormMerger());
    }

    @Test
    public void testMergeWidget() {
        N2oForm widget = merge("net/n2oapp/framework/config/metadata/local/merger/widget/parentWidgetForm.widget.xml",
                "net/n2oapp/framework/config/metadata/local/merger/widget/childWidgetForm.widget.xml")
                .get("childWidgetForm", N2oForm.class);
        assertThat(widget, notNullValue());
        assertThat(widget.getDependsOn(), is("child"));
        assertThat(widget.getName(), is("Child"));
        assertThat(widget.getQueryId(), is("parent"));
        assertThat(widget.getObjectId(), is("parent"));
        assertThat(widget.getPreFilters().length, is(2));
        assertThat(widget.getPreFields().length, is(2));
        assertThat(widget.getVisible(), is("true"));

        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-1.0"))).get("extAttr1"), is("child1"));
        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-1.0"))).get("extAttr2"), is("child2"));

        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("extChild", "http://example.com/n2o/ext-child"))).get("extAttr"), is("child"));
        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("extParent", "http://example.com/n2o/ext-parent"))).get("extAttr"), is("parent"));
    }

    @Test
    public void testMergeForm() {
        N2oForm widget = merge("net/n2oapp/framework/config/metadata/local/merger/widget/parentFormMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/local/merger/widget/childFormMerger.widget.xml")
                .get("childFormMerger", N2oForm.class);
        assertThat(widget.getMode(), is(FormMode.TWO_MODELS));
    }
}
