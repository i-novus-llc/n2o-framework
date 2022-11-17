package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class N2oFieldsetMergerTest extends SourceMergerTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oFieldSetsPack());
        builder.ios(new InputTextIOv3());
    }

    @Test
    public void testSetFieldsetMerge() {
        N2oSetFieldSet fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/parentSetFieldsetMerger.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/childSetFieldsetMerger.fieldset.xml")
                .get("parentSetFieldsetMerger", N2oSetFieldSet.class);

        assertThat(fieldSet.getVisible(), is("false"));
        assertThat(fieldSet.getEnabled(), is("false"));
        assertThat(fieldSet.getLabel(), is("parent-label"));
        assertThat(fieldSet.getCssClass(), is("class"));
        assertThat(fieldSet.getStyle(), is("color:red"));
        assertThat(fieldSet.getDependsOn(), is(new String[]{"a", "b"}));
        assertThat(fieldSet.getDescription(), is("desc"));
        assertThat(fieldSet.getFieldLabelAlign(), is(N2oFieldSet.FieldLabelAlign.left));
        assertThat(fieldSet.getFieldLabelLocation(), is(N2oFieldSet.FieldLabelLocation.left));
        assertThat(fieldSet.getFieldLabelWidth(), is("100px"));
        assertThat(fieldSet.getHelp(), is("help"));
        assertThat(fieldSet.getSrc(), is("test"));
        assertThat(fieldSet.getItems().length, is(1));
        assertThat(((N2oField) fieldSet.getItems()[0]).getId(), is("parentField"));
    }

    @Test
    public void testLineFieldsetMerge() {
        N2oLineFieldSet fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/parentLineFieldsetMerger.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/childLineFieldsetMerger.fieldset.xml")
                .get("parentLineFieldsetMerger", N2oLineFieldSet.class);

        assertThat(fieldSet.getVisible(), is("false"));
        assertThat(fieldSet.getEnabled(), is("false"));
        assertThat(fieldSet.getLabel(), is("parent-label"));
        assertThat(fieldSet.getCssClass(), is("class"));
        assertThat(fieldSet.getStyle(), is("color:red"));
        assertThat(fieldSet.getDependsOn(), is(new String[]{"a", "b"}));
        assertThat(fieldSet.getDescription(), is("desc"));
        assertThat(fieldSet.getFieldLabelAlign(), is(N2oFieldSet.FieldLabelAlign.left));
        assertThat(fieldSet.getFieldLabelLocation(), is(N2oFieldSet.FieldLabelLocation.left));
        assertThat(fieldSet.getFieldLabelWidth(), is("100px"));
        assertThat(fieldSet.getHelp(), is("help"));
        assertThat(fieldSet.getSrc(), is("test"));
        assertThat(fieldSet.getCollapsible(), is(false));
        assertThat(fieldSet.getHasSeparator(), is(false));
        assertThat(fieldSet.getExpand(), is(false));
        assertThat(fieldSet.getItems().length, is(1));
        assertThat(((N2oField) fieldSet.getItems()[0]).getId(), is("parentField"));
    }

    @Test
    public void testMultiFieldsetMerge() {
        N2oMultiFieldSet fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/parentMultiFieldsetMerger.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/childMultiFieldsetMerger.fieldset.xml")
                .get("parentMultiFieldsetMerger", N2oMultiFieldSet.class);

        assertThat(fieldSet.getVisible(), is("false"));
        assertThat(fieldSet.getEnabled(), is("false"));
        assertThat(fieldSet.getLabel(), is("parent-label"));
        assertThat(fieldSet.getCssClass(), is("class"));
        assertThat(fieldSet.getStyle(), is("color:red"));
        assertThat(fieldSet.getDependsOn(), is(new String[]{"a", "b"}));
        assertThat(fieldSet.getDescription(), is("desc"));
        assertThat(fieldSet.getFieldLabelAlign(), is(N2oFieldSet.FieldLabelAlign.left));
        assertThat(fieldSet.getFieldLabelLocation(), is(N2oFieldSet.FieldLabelLocation.left));
        assertThat(fieldSet.getFieldLabelWidth(), is("100px"));
        assertThat(fieldSet.getHelp(), is("help"));
        assertThat(fieldSet.getSrc(), is("test"));
//        assertThat(fieldSet.get);

        assertThat(fieldSet.getItems().length, is(1));
        assertThat(((N2oField) fieldSet.getItems()[0]).getId(), is("parentField"));
    }
}
