package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;


/*
 * @author enuzhdina
 * @since 16.06.2015.
 */
public class N2oSelectTreeXmlIOTest extends N2oStandardControlReaderTestBase {
    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
            .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

    @Test
    public void testSelectTreeXmlIOV1() {
        assert tester.check("net/n2oapp/framework/config/io/control/testSelectTreeReader1.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oSelectTree selectTree = (N2oSelectTree) fieldSet.getItems()[0];

                    assert selectTree.getAjax().equals(false);
                    assert selectTree.getSearch();

                    assert selectTree.getCheckboxes() == null;
                });
    }

    @Test
    public void testSelectTreeXmlIOV2() {
        assert tester.check("net/n2oapp/framework/config/io/control/testSelectTreeReader2.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oSelectTree selectTree = (N2oSelectTree) fieldSet.getItems()[0];

                    assert selectTree.getAjax().equals(true);
                    assert selectTree.getSearch();
                    assert selectTree.getCheckboxes();
                    assert selectTree.getQueryId().equals("blank");
                    assert selectTree.getParentFieldId().equals("id");
                    assert selectTree.getValueFieldId().equals("id");
                    assert selectTree.getLabelFieldId().equals("id");
                    assert selectTree.getDetailFieldId().equals("id");
                    assert selectTree.getHasChildrenFieldId().equals("id");
                    assert selectTree.getIconFieldId().equals("id");
                    assert selectTree.getMasterFieldId().equals("id");
                    assert selectTree.getSearchFilterId().equals("id");
                    assert selectTree.getEnabledFieldId().equals("test");
                    assertPreFilter(selectTree.getPreFilters()[0], true);
                });
    }

    @Test
    public void testInputSelectTreeXmlIOV1() {
        assert tester.check("net/n2oapp/framework/config/io/control/testInputSelectTreeReader1.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 2);
                    N2oInputSelectTree selectTree1 = (N2oInputSelectTree) fieldSet.getItems()[0];
//                    assertStandardSingleListAttribute(selectTree1, false);

                    assert selectTree1.getAjax().equals(false);
                    assert selectTree1.getSearch();
                    assert selectTree1.getCheckboxes() == null;

                    N2oInputSelectTree selectTree2 = (N2oInputSelectTree) fieldSet.getItems()[1];
                    assert selectTree2.getEnabledFieldId().equals("test");
                    assert selectTree2.getHasChildrenFieldId().equals("test");
                    assert selectTree2.getParentFieldId().equals("test");
                    assert selectTree2.getLabelFieldId().equals("test");
                    assert selectTree2.getDetailFieldId().equals("test");
                    assert selectTree2.getIconFieldId().equals("test");
                });
    }
}
