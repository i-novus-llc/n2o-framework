package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тесты чтения/записи филдсетов
 */
class FieldsetXmlIOTest {

    private final ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new N2oFieldSetsIOPack())
            .addIO(new InputTextIOv2());

    @Test
    void testIOSetFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testSetFieldsetIOv4.fieldset.xml");
    }

    @Test
    void testIOLineFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testLineFieldsetIOv4.fieldset.xml");
    }

    @Test
    void testIOMultiFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testMultiFieldsetIOv4.fieldset.xml");
    }

}


