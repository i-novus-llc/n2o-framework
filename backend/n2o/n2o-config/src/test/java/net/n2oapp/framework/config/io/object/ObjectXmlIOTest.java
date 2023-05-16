package net.n2oapp.framework.config.io.object;

import net.n2oapp.framework.config.io.action.v2.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи объекта
 */
public class ObjectXmlIOTest {
    
    ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new N2oDataProvidersIOPack());

    @Test
    void testObjectV4XmlIO() {
        tester.ios(new ObjectElementIOv4(), new ButtonIOv2(), new InvokeActionElementIOV2(), new CloseActionElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv4.object.xml");
    }

    @Test
    void testObjectV3XmlIO() {
        tester.ios(new ObjectElementIOv3());
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv3.object.xml");
    }
}
