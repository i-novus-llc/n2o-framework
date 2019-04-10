package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.config.persister.widget.TreeXmlPersister;
import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.reader.widget.widget3.TreeXmlReaderV3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import org.junit.Test;

/*
 * @author enuzhdina
 * @since 11.06.2015.
 */
public class TreeXmlIOTest extends BaseWidgetReaderTest {
    private SelectiveReader reader = new SelectiveReader()
            .addReader(new TreeXmlReaderV3());

    private SelectivePersister persister = new SelectivePersister()
            .addPersister(new TreeXmlPersister());

    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(reader)
            .addPersister(persister);

    @Test
    public void testTreeIO() {
        assert tester.check("net/n2oapp/framework/config/reader/widget/tree/testTreeReader1.widget.xml",
                (N2oTree tree) -> {
                    assertWidgetAttribute(tree);
                    assertInheritanceTree(tree);
                    assert tree.getCheckboxes().equals(true);
                    assert tree.getIconFieldId().equals("test");
                });
    }

}
