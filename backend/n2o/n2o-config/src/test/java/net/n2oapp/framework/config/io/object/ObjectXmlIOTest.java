package net.n2oapp.framework.config.io.object;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи объекта
 */
public class ObjectXmlIOTest {
    ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new N2oDataProvidersIOPack());

    @Test
    public void testObjectV4XmlIO() {
        tester.ios(new ObjectElementIOv4(), new ButtonIO(), new InvokeActionElementIOV1(), new CloseActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv4.object.xml");
    }

    @Test
    public void testObjectV3XmlIO() {
        tester.ios(new ObjectElementIOv3());
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv3.object.xml");
    }
}
