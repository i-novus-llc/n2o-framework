package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.config.persister.widget.TreeXmlPersister;
import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.reader.widget.widget3.TreeXmlReaderV3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import org.junit.Test;

import java.util.List;

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
    public void testTreeIO(){
        assert tester.check("net/n2oapp/framework/config/reader/widget/tree/testTreeReader1.widget.xml",
                (N2oTree inheritanceTree) -> {
                    assertWidgetAttribute(inheritanceTree);
                    assertInheritanceTree(inheritanceTree);
                    assert inheritanceTree.getCheckboxes().equals(true);
                    assert inheritanceTree.getSearch().equals(true);
                    assert inheritanceTree.getInheritanceNodes().getSearchFieldId().equals("id");
                    assert inheritanceTree.getInheritanceNodes().getEnabledFieldId().equals("id");
                    assert inheritanceTree.getInheritanceNodes().getIconFieldId().equals("test");
                    assert !inheritanceTree.getAutoSelect();
                });

        assert tester.check("net/n2oapp/framework/config/reader/widget/tree/testTreeReader2.widget.xml",
                (N2oTree groupingNodesTree) -> {
                    assertGroupingNodesTree(groupingNodesTree);
                    assert groupingNodesTree.getSearch().equals(true);
                    assert groupingNodesTree.getGroupingNodes().getSearchFieldId().equals("id");
                    assert !groupingNodesTree.getAutoSelect();
                    List<GroupingNodes.Node> nodes = groupingNodesTree.getGroupingNodes().getNodes();
                    for(int i = 0; i < 2; i++){
                        assert nodes.get(0).getEnabled().equals(true);
                        assert nodes.get(0).getIcon().equals("test");
                        nodes = nodes.get(0).getNodes();
                    }
                });
    }

}
