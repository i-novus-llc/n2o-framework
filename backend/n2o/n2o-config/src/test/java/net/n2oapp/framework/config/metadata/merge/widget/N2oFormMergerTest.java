package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.global.view.widget.FormModeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oActionsIOV2Pack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsV3IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsV5IOPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.jdom2.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование слияния виджетов {@code <form>}
 */
class N2oFormMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsIOV2Pack(), new N2oControlsV3IOPack(), new N2oFieldSetsV5IOPack())
                .ios(new FormElementIOV5())
                .mergers(new N2oWidgetMerger<>(), new N2oFormMerger());
    }

    @Test
    void testMergeWidget() {
        N2oForm widget = merge("net/n2oapp/framework/config/metadata/merge/widget/parentWidgetForm.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childWidgetForm.widget.xml")
                .get("parentWidgetForm", N2oForm.class);
        assertThat(widget, notNullValue());
        assertThat(widget.getDependencies()[0].getDatasource(), is("child"));
        assertThat(widget.getDatasource().getQueryId(), is("parent"));
        assertThat(widget.getDatasource().getObjectId(), is("parent"));
        assertThat(widget.getDatasource().getFilters().length, is(1));
        assertThat(widget.getVisible(), is("true"));
        assertThat(widget.getActions().length, is(2));

        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-1.0"))).get("extAttr1"), is("child1"));
        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-1.0"))).get("extAttr2"), is("child2"));

        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("extChild", "http://example.com/n2o/ext-child"))).get("extAttr"), is("child"));
        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("extParent", "http://example.com/n2o/ext-parent"))).get("extAttr"), is("parent"));
    }

    @Test
    void testMergeForm() {
        N2oForm form = merge("net/n2oapp/framework/config/metadata/merge/widget/parentFormMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childFormMerger.widget.xml")
                .get("parentFormMerger", N2oForm.class);
        assertThat(form.getMode(), is(FormModeEnum.TWO_MODELS));
        assertThat(form.getUnsavedDataPrompt(), is(true));
        assertThat(form.getDatasource().getQueryId(), is("defQueryId"));

        SourceComponent[] items = form.getItems();
        assertThat(items.length, is(2));
        assertThat(((N2oInputSelect) items[0]).getId(), is("test2"));
        assertThat(((N2oInputText) items[1]).getId(), is("test1"));
    }
}
