package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.config.io.page.v4.SimplePageElementIOv4;
import net.n2oapp.framework.config.metadata.pack.N2oTableSettingsIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsV5IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения и записи пользовательских настроек таблицы
 */
class TableSettingsIOv1Test {

    @Test
    void testTableSettingsIOv1() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SimplePageElementIOv4())
                .addPack(new N2oWidgetsV5IOPack())
                .addPack(new N2oTableSettingsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/table/testTableSettingsIOv1.page.xml");
    }
}
