package net.n2oapp.framework.config.io.object;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * Тестирование чтения\записи объекта
 */
public class ObjectXmlIOTest {
    ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(new SelectiveStandardReader().addObjectReader().addDataProviders())
            .addPersister(new SelectiveStandardPersister().addObjectPersister().addDataProviders());

    @Test
    public void testObjectV4XmlIO() {
        tester.ios(new ObjectElementIOv4(), new ButtonIO(), new InvokeActionElementIOV1(), new CloseActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv4.object.xml");
    }

    @Test
    public void testObjectV3XmlIO() {
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv3.object.xml");
    }
}
