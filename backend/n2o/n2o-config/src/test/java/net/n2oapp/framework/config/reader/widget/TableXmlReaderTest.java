package net.n2oapp.framework.config.reader.widget;


import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oAbstractTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Size;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.*;
import net.n2oapp.framework.config.reader.widget.cell.*;
import net.n2oapp.framework.config.reader.widget.widget3.TableXmlReaderV3;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;


/**
 * Тестирование чтения таблицы версии ниже 4
 */
public class TableXmlReaderTest extends BaseWidgetReaderTest {

    @Test
    public void testReaderTable3Widget() {
        N2oTable table3 = new SelectiveStandardReader()
                .addWidgetReaderV3()
                .addFieldSet3Reader()
                .addEventsReader()
                .addControlReader()
                .addReader(new TableXmlReaderV3())
                .addReader(new N2oColorCellXmlReader())
                .addReader(new N2oCheckboxCellXmlReader())
                .addReader(new N2oCustomCellXmlReader())
                .addReader(new N2oIconCellXmlReader())
                .addReader(new N2oLinkCellXmlReader())
                .addReader(new N2oTextCellXmlReader())
                .addReader(new N2oImageCellXmlReader())
                .addReader(new N2oProgressBarCellXmlReader())
                .addReader(new N2oXEditableCellReader()).readByPath("net/n2oapp/framework/config/reader/widget/table/testTableReader.widget.xml");


        assertWidgetAttribute(table3);
        assertStandardTable(table3);
        assert table3.getHasCheckboxes();
        assert table3.getAutoSelect();
        N2oSimpleColumn checkBoxColumn = ((N2oSimpleColumn) table3.getColumns()[0]);
        N2oCustomCell custom = (N2oCustomCell) ((N2oSimpleColumn) table3.getColumns()[6]).getCell();
        assert custom.getSrc().equals("test");
        assert custom.getProperties().size() == 2;
        assert custom.getProperties().get("a").equals("b");
        assert custom.getProperties().get("c").equals("d");
        N2oSimpleColumn iconColumn = ((N2oSimpleColumn) table3.getColumns()[2]);
        N2oIconCell iconCell = (N2oIconCell) iconColumn.getCell();
        assert iconCell.getType().equals(LabelType.icon);
        assert iconCell.getPosition().equals(Position.left);
        assert iconCell.getIconSwitch() != null;
        assert iconCell.getIconSwitch().getValueFieldId().equals("id");
        assert iconCell.getIconSwitch().getCases().size() == 1;
        assert iconCell.getIconSwitch().getCases().get("key").equals("value");
        assert iconCell.getIconSwitch().getDefaultCase().equals("test");
        N2oImageCell imageCell = (N2oImageCell) ((N2oSimpleColumn) table3.getColumns()[9]).getCell();
        assert imageCell.getWidth() == null;
        assert imageCell.getShape() == null;
        N2oImageCell imageCell2 = (N2oImageCell) ((N2oSimpleColumn) table3.getColumns()[8]).getCell();
        assert imageCell2.getWidth().equals("32");
        assert imageCell2.getShape().equals(ImageShape.circle);
        N2oProgressBarCell progressBarCell = (N2oProgressBarCell) ((N2oSimpleColumn) table3.getColumns()[10]).getCell();
        assert progressBarCell.getSize().equals(N2oProgressBarCell.Size.small);
        assert progressBarCell.getStriped();
        assert progressBarCell.getActive();
        N2oProgressBarCell progressBarCell1 = (N2oProgressBarCell) ((N2oSimpleColumn) table3.getColumns()[11]).getCell();
        assert progressBarCell1.getSize() == null;
        assert progressBarCell1.getStriped() == null;
        assert progressBarCell1.getActive() == null;
        assert table3.getColumns()[12].getVisibilityCondition() != null;
    }
}

