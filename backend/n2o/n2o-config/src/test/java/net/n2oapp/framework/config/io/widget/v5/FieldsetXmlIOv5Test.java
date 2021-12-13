package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тесты чтения/записи филдсетов версии 5.0
 */
public class FieldsetXmlIOv5Test {

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addPack(new N2oFieldSetsV5IOPack())
            .addIO(new InputTextIOv3());

    @Test
    public void testIOSetFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testSetFieldsetIOv5.fieldset.xml",
                (N2oSetFieldSet fs) -> {
                });
    }

    @Test
    public void testIOLineFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testLineFieldsetIOv5.fieldset.xml",
                (N2oLineFieldSet fs) -> {
                });
    }

    @Test
    public void testIOMultiFieldset() {
        assert tester.check("net/n2oapp/framework/config/io/widget/fieldset/testMultiFieldsetIOv5.fieldset.xml",
                (N2oMultiFieldSet fs) -> {
                });
    }

}


