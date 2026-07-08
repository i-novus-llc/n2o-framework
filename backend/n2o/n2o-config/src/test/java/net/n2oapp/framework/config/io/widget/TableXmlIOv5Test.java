package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv5;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.SubmenuIOv2;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;


/**
 * Тестирование чтения и записи таблицы версии 5.0
 */
class TableXmlIOv5Test {

    @Test
    void testTableXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();

        tester.ios(new TableElementIOV5<>(),
                        new SetFieldsetElementIOv5(),
                        new InputTextIOv3(),
                        new CloseActionElementIOV2(),
                        new InvokeActionElementIOV2(),
                        new ButtonIOv2(), new SubmenuIOv2()
                )
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/table/testTableWidgetIOv5.widget.xml");
    }
}
