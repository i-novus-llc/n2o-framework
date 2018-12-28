package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oWidgetList;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.config.persister.widget.WidgetListPersister;
import net.n2oapp.framework.config.persister.widget.cell.N2oBadgeXmlPersister;
import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.reader.widget.widget3.WidgetListReaderV3;
import net.n2oapp.framework.config.reader.widget.cell.N2oBadgeXmlReader;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

/**
 * @author iryabov
 * @since 17.06.2015
 */
public class WidgetListXmlIOTest extends BaseWidgetReaderTest {

    private SelectiveReader reader = new SelectiveStandardReader()
            .addReader(new WidgetListReaderV3())
            .addReader(new N2oBadgeXmlReader());

    private SelectivePersister persister = new SelectiveStandardPersister()
            .addPersister(new WidgetListPersister())
            .addPersister(new N2oBadgeXmlPersister());

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(reader)
            .addPersister(persister);

    @Test
    public void testTreeIO(){
        assert tester.check("net/n2oapp/framework/config/reader/widget/list/testWidgetListReader1.widget.xml",
                (N2oWidgetList widget) -> {
                    assert widget.getColumn() != null;
                    assert widget.getColumn().getTextFieldId().equals("test");
                    assert widget.getColumn().getCell() != null;
                    assert ((N2oBadgeCell) widget.getColumn().getCell()).getText().equals("test");
                });


    }
}
