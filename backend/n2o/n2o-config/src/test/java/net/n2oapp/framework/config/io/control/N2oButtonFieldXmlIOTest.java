package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Created by dednakov on 04.05.2017.
 */
public class N2oButtonFieldXmlIOTest extends N2oStandardControlReaderTestBase {

    @Test
    public void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
                .addPersister(new SelectiveStandardPersister().addFieldsetPersister().addEventPersister());

        assert tester.check("net/n2oapp/framework/config/io/control/testButtonFieldReader.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                /*    assertCountField(fieldSet, 3);
                    N2oButtonField buttonField = (N2oButtonField) fieldSet.getItems()[0];
                    assert buttonField.getId().equals("openModal");
                    assert buttonField.getLabel().equals("openModal");
                    assert buttonField.getEvent() != null;
                    assert ((N2oShowModal)buttonField.getEvent()).getContainerId().equals("test");
                    assert ((N2oShowModal)buttonField.getEvent()).getDetailFieldId().equals("id");
                    assert ((N2oShowModal)buttonField.getEvent()).getPageName().equals("test");
                    assert ((N2oShowModal)buttonField.getEvent()).getPageId().equals("test");
                    assert ((N2oShowModal)buttonField.getEvent()).getMasterFieldId().equals("id");
                    assert ((N2oShowModal)buttonField.getEvent()).getMinWidth().equals("100");
                    assert ((N2oShowModal)buttonField.getEvent()).getMaxWidth().equals("100");
                    assert ((N2oShowModal)buttonField.getEvent()).getWidth().equals("100");
                    assert ((N2oShowModal)buttonField.getEvent()).getResultContainerId().equals("test");
                    N2oPreFilter filter = ((N2oShowModal)buttonField.getEvent()).getPreFilters().get(0);
                    assert filter.getRef().equals("test");
                    assert filter.getType().name().toLowerCase().equals("eq");
                    assert filter.getFieldId().equals("id");
                    assert filter.getValue().equals("value");
                    assert buttonField.getProperties().get("test").equals("test");

                    N2oButtonField buttonField1 = (N2oButtonField) fieldSet.getItems()[1];
                    assert buttonField1.getId().equals("openPage");
                    assert buttonField1.getLabel().equals("openPage");
                    assert buttonField1.getEvent() != null;
                    assert ((N2oOpenPage)buttonField1.getEvent()).getContainerId().equals("test");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getDetailFieldId().equals("id");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getPageName().equals("test");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getPageId().equals("test");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getMasterFieldId().equals("id");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getMinWidth().equals("100");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getMaxWidth().equals("100");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getWidth().equals("100");
                    assert ((N2oOpenPage)buttonField1.getEvent()).getResultContainerId().equals("test");
                    N2oPreFilter filter1 = ((N2oOpenPage)buttonField1.getEvent()).getPreFilters().get(0);
                    assert filter1.getRef().equals("test");
                    assert filter1.getType().name().toLowerCase().equals("eq");
                    assert filter1.getFieldId().equals("id");
                    assert filter1.getValue().equals("value");
                    assert buttonField1.getProperties().get("test").equals("test");

                    N2oButtonField buttonField2 = (N2oButtonField) fieldSet.getItems()[2];
                    assert buttonField2.getId().equals("openAnchor");
                    assert buttonField2.getLabel().equals("openAnchor");
                    assert buttonField2.getCssClass().equals("btn-link");
                    assert ((Anchor)buttonField2.getEvent()).getHref().equals("test");
                    assert ((Anchor)buttonField2.getEvent()).getTarget().equals(Target.newWindow);
                    assert buttonField2.getProperties().get("test").equals("test");*/
                });
    }
}
