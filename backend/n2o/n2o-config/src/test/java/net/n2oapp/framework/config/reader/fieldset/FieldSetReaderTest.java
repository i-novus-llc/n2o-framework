package net.n2oapp.framework.config.reader.fieldset;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * @author iryabov
 * @since 14.10.2016
 */
public class FieldSetReaderTest {

    @Test
    public void testReader3() {
        N2oFieldSet fieldSet = new SelectiveStandardReader().addFieldSet3Reader()
                .readByPath("net/n2oapp/framework/config/reader/fieldset/testFieldsetReader3.fieldset.xml");
        assert N2oFieldSet.FieldLabelLocation.left.equals(fieldSet.getFieldLabelLocation());
        assert N2oFieldSet.FieldLabelAlign.left.equals(fieldSet.getFieldLabelAlign());
        assert "test".equals(fieldSet.getLabel());
        assert "test".equals(fieldSet.getCssClass());
        assert "test".equals(fieldSet.getDependencyCondition());
        assert "test".equals(fieldSet.getSrc());
        assert "test".equals(fieldSet.getStyle());
        assert ((N2oInputText) ((N2oFieldsetRow)(fieldSet.getItems()[0])).getItems()[0]).getId().equals("test1");
        assert ((N2oInputText) fieldSet.getItems()[1]).getId().equals("test2");

    }

    @Test
    public void testReader2() {
        N2oFieldSet fieldSet = new SelectiveStandardReader().addFieldSet2Reader().addControlReader()
                .readByPath("net/n2oapp/framework/config/reader/fieldset/testFieldsetReader2.fieldset.xml");
        assert N2oFieldSet.FieldLabelLocation.left.equals(fieldSet.getFieldLabelLocation());
        assert "test".equals(fieldSet.getLabel());
        assert "test".equals(fieldSet.getCssClass());
        assert "test".equals(fieldSet.getDependencyCondition());
        assert "test".equals(fieldSet.getStyle());
        assert ((N2oInputText) ((N2oFieldsetRow)(fieldSet.getItems()[0])).getItems()[0]).getId().equals("test1");
        assert ((N2oInputText) fieldSet.getItems()[1]).getId().equals("test2");
    }

}
