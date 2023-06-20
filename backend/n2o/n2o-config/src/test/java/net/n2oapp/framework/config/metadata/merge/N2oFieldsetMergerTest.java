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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class N2oFieldsetMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
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
    void testSetFieldsetMerge() {
        N2oSetFieldSet fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/set/parentSetFieldsetMerger.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/set/childSetFieldsetMerger.fieldset.xml")
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
    void testLineFieldsetMerge() {
        N2oLineFieldSet fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/line/parentLineFieldsetMerger.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/line/childLineFieldsetMerger.fieldset.xml")
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

        fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/line/parentLineFieldsetMerger2.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/line/childLineFieldsetMerger2.fieldset.xml")
                .get("parentLineFieldsetMerger2", N2oLineFieldSet.class);

        assertThat(fieldSet.getCollapsible(), is(true));
        assertThat(fieldSet.getHasSeparator(), is(true));
        assertThat(fieldSet.getExpand(), is(true));
        assertThat(fieldSet.getItems().length, is(1));
        assertThat(((N2oField) fieldSet.getItems()[0]).getId(), is("parentField"));
    }

    @Test
    void testMultiFieldsetMerge() {
        N2oMultiFieldSet fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/multi/parentMultiFieldsetMerger.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/multi/childMultiFieldsetMerger.fieldset.xml")
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
        assertThat(fieldSet.getAddButtonLabel(), is("addLabel"));
        assertThat(fieldSet.getChildrenLabel(), is("childrenLabel"));
        assertThat(fieldSet.getFirstChildrenLabel(), is("firstChildrenLabel"));
        assertThat(fieldSet.getRemoveAllButtonLabel(), is("removeAllLabel"));
        assertThat(fieldSet.getCanAdd(), is(false));
        assertThat(fieldSet.getCanCopy(), is(false));
        assertThat(fieldSet.getCanRemove(), is(false));
        assertThat(fieldSet.getCanRemoveAll(), is(false));
        assertThat(fieldSet.getCanRemoveFirst(), is(false));
        assertThat(fieldSet.getItems().length, is(1));
        assertThat(((N2oField) fieldSet.getItems()[0]).getId(), is("parentField"));

        fieldSet = merge("net/n2oapp/framework/config/metadata/merge/fieldset/multi/parentMultiFieldsetMerger2.fieldset.xml",
                "net/n2oapp/framework/config/metadata/merge/fieldset/multi/childMultiFieldsetMerger2.fieldset.xml")
                .get("parentMultiFieldsetMerger2", N2oMultiFieldSet.class);

        assertThat(fieldSet.getAddButtonLabel(), is("p_addLabel"));
        assertThat(fieldSet.getChildrenLabel(), is("p_childrenLabel"));
        assertThat(fieldSet.getFirstChildrenLabel(), is("p_firstChildrenLabel"));
        assertThat(fieldSet.getRemoveAllButtonLabel(), is("p_removeAllLabel"));
        assertThat(fieldSet.getCanAdd(), is(true));
        assertThat(fieldSet.getCanCopy(), is(true));
        assertThat(fieldSet.getCanRemove(), is(true));
        assertThat(fieldSet.getCanRemoveAll(), is(true));
        assertThat(fieldSet.getCanRemoveFirst(), is(true));
        assertThat(fieldSet.getItems().length, is(1));
        assertThat(((N2oField) fieldSet.getItems()[0]).getId(), is("parentField"));
    }
}
