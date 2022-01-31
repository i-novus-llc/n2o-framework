package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.fieldset.v4.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.metadata.pack.N2oCellsIOPack;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;


/**
 * Тестирование чтения и записи таблицы версии 4
 */
public class TableXmlIOTest {
    @Test
    public void testTableXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();

        tester.ios(
                new TableElementIOV4(),
                new SetFieldsetElementIOv4(),
                new InputTextIOv2(),
                new CloseActionElementIOV1(),
                new InvokeActionElementIOV1(),
                new ButtonIO(), new SubmenuIO())
                .addPack(new N2oCellsIOPack());

        assert tester.check("net/n2oapp/framework/config/io/widget/table/testTableWidgetIOv4.widget.xml");
    }
}
