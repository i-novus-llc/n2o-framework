package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тесты чтения/записи филдсетов версии 5.0
 */
class FieldsetXmlIOv5Test {

    private final ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new N2oFieldSetsV5IOPack())
            .addIO(new InputTextIOv3());

    @Test
    void testIOSetFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testSetFieldsetIOv5.fieldset.xml");
    }

    @Test
    void testIOLineFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testLineFieldsetIOv5.fieldset.xml");
    }

    @Test
    void testIOMultiFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testMultiFieldsetIOv5.fieldset.xml");
    }
}


