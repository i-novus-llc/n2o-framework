package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.list.GroupClassifierMode;
import net.n2oapp.framework.api.metadata.control.list.N2oGroupClassifierMulti;
import net.n2oapp.framework.api.metadata.control.list.N2oGroupClassifierSingle;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * @author dfirstov
 * @since 17.07.2015
 */
public class N2oGroupClassifierIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testSingle() throws Exception {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addReader(new SelectiveStandardReader().addFieldSet4Reader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());
        assert tester.check("net/n2oapp/framework/config/io/control/testGroupClassifier.xml",
                (N2oFieldSet fieldSet)  -> {
                    assertCountField(fieldSet, 3);
                    N2oGroupClassifierSingle groupClassifier = (N2oGroupClassifierSingle) fieldSet.getItems()[0];
                    assert "single".equals(groupClassifier.getId());
                    assert "singleGroupId".equals(groupClassifier.getGroupFieldId());
                    assert "info".equals(groupClassifier.getInfoFieldId());
                    assert "style".equals(groupClassifier.getInfoStyle());
                    assert groupClassifier.getMode() == GroupClassifierMode.SINGLE;
                });
    }

    @Test
    public void testMulti() throws Exception {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addReader(new SelectiveStandardReader().addFieldSet4Reader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());
        assert tester.check("net/n2oapp/framework/config/io/control/testGroupClassifier.xml",
                (N2oFieldSet fieldSet)  -> {
                    assertCountField(fieldSet, 3);
                    N2oGroupClassifierMulti groupClassifier = (N2oGroupClassifierMulti) fieldSet.getItems()[1];
                    assert "multi".equals(groupClassifier.getId());
                    assert "multiGroupId".equals(groupClassifier.getGroupFieldId());
                    assert "info".equals(groupClassifier.getInfoFieldId());
                    assert "style".equals(groupClassifier.getInfoStyle());
                    assert groupClassifier.getMode() == GroupClassifierMode.MULTI;
                });
    }

    @Test
    public void testMultiCheckbox() throws Exception {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.addReader(new SelectiveStandardReader().addFieldSet4Reader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister());
        assert tester.check("net/n2oapp/framework/config/io/control/testGroupClassifier.xml",
                (N2oFieldSet fieldSet)  -> {
                    assertCountField(fieldSet, 3);
                    N2oGroupClassifierMulti groupClassifier = (N2oGroupClassifierMulti) fieldSet.getItems()[2];
                    assert "multiCheckbox".equals(groupClassifier.getId());
                    assert "multiCheckboxGroupId".equals(groupClassifier.getGroupFieldId());
                    assert "info".equals(groupClassifier.getInfoFieldId());
                    assert "style".equals(groupClassifier.getInfoStyle());
                    assert groupClassifier.getMode() == GroupClassifierMode.MULTI_CHECKBOX;
                });
    }
}
