package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.config.io.action.CloseActionElementIOV1;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.fieldset.SetFieldsetElementIOv4;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.io.widget.table.cell.*;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;


/*
 *Тестирование чтения и записи таблицы версии 4
 */
public class TableXmlIOTest {
    @Test
    public void testTableXmlIOV4() {
        ION2oMetadataTester tester = new ION2oMetadataTester();

        tester.ios(
                new TableElementIOV4(),
                new LinkCellElementIOv2(),
                new CustomCellElementIOv2(),
                new EditCellElementIOv2(),
                new IconCellElementIOv2(),
                new CheckboxCellElementIOv2(),
                new ImageCellElementIOv2(),
                new ProgressCellElementIOv2(),
                new TextCellElementIOv2(),
                new ToolbarCellElementIOv2(),
                new CloseActionElementIOV1(),
                new BadgeCellElementIOv2(),
                new ListCellIOElementIOv2(),
                new SetFieldsetElementIOv4(),
                new InputTextIOv2(),
                new InvokeActionElementIOV1(),
                new ButtonIO(), new SubmenuIO()
        );

        assert tester.check("net/n2oapp/framework/config/io/widget/table/testTableWidgetIOv4.widget.xml");
    }
}
