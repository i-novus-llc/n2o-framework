package net.n2oapp.framework.config.io.widget.v5.table;

import net.n2oapp.framework.config.io.action.v2.CloseActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.InvokeActionElementIOV2;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.fieldset.v5.SetFieldsetElementIOv5;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.toolbar.v2.SubmenuIOv2;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.N2oCellsV3IOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;


/**
 * Тестирование чтения и записи таблицы версии 5.0
 */
public class TableXmlIOv5Test {
    @Test
    public void testTableXmlIOV5() {
        ION2oMetadataTester tester = new ION2oMetadataTester();

        tester.ios(
                new TableElementIOV5(),
                new SetFieldsetElementIOv5(),
                new InputTextIOv3(),
                new CloseActionElementIOV2(),
                new InvokeActionElementIOV2(),
                new ButtonIOv2(), new SubmenuIOv2())
                .addPack(new N2oCellsV3IOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/table/testTableWidgetIOv5.widget.xml");
    }
}
