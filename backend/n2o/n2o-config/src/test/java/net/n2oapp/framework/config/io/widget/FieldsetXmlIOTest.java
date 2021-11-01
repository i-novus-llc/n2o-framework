package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тесты чтения/записи филдсетов
 */
public class FieldsetXmlIOTest {

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new N2oFieldSetsIOPack())
            .addIO(new InputTextIOv2());

    @Test
    public void testIOSetFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testSetFieldsetIOv4.fieldset.xml",
                (N2oSetFieldSet fs) -> {
                });
    }

    @Test
    public void testIOLineFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testLineFieldsetIOv4.fieldset.xml",
                (N2oLineFieldSet fs) -> {
                });
    }

    @Test
    public void testIOMultiFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testMultiFieldsetIOv4.fieldset.xml",
                (N2oMultiFieldSet fs) -> {
                });
    }

}


