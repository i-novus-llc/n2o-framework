package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.list.N2oInputSelectTree;
import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

import java.util.List;

/*
 * @author enuzhdina
 * @since 16.06.2015.
 */
public class N2oSelectTreeXmlIOTest extends N2oStandardControlReaderTestBase {
    private ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(new SelectiveStandardReader().addFieldSet4Reader().addEventsReader())
            .addPersister(new SelectiveStandardPersister().addFieldsetPersister());

    @Test
    public void testSelectTreeXmlIOV1(){
        assert tester.check("net/n2oapp/framework/config/io/control/testSelectTreeReader1.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oSelectTree selectTree = (N2oSelectTree) fieldSet.getItems()[0];
//                    assertStandardSingleListAttribute(selectTree, false);

                    assert selectTree.getAjax().equals(false);
                    assert selectTree.getSearch();
                    assert selectTree.getGroupingNodes().getMasterFieldId().equals("id");
                    assert selectTree.getGroupingNodes().getQueryId().equals("blank");
                    assert selectTree.getGroupingNodes().getDetailFieldId().equals("id");
                    assert selectTree.getGroupingNodes().getValueFieldId().equals("id");
                    assert selectTree.getGroupingNodes().getSearchFieldId().equals("id");
                    assert selectTree.getCheckboxes() == null;
                    List<GroupingNodes.Node> nodes = selectTree.getGroupingNodes().getNodes();
                    for(int i = 0; i < 2; i++) {
                        assert nodes.size() == 1;
                        assert nodes.get(0).getLabelFieldId().equals("id");
                        assert nodes.get(0).getValueFieldId().equals("id");
                        assert nodes.get(0).getEnabled();
                        nodes = nodes.get(0).getNodes();
                    }
                    assertPreFilter(selectTree.getGroupingNodes().getPreFilters()[0], true);
                });
    }

    @Test
    public void testSelectTreeXmlIOV2(){
        assert tester.check("net/n2oapp/framework/config/io/control/testSelectTreeReader2.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 1);
                    N2oSelectTree selectTree = (N2oSelectTree) fieldSet.getItems()[0];

                    assert selectTree.getAjax().equals(true);
                    assert selectTree.getSearch();
                    assert selectTree.getCheckboxes();
                    assert selectTree.getInheritanceNodes().getQueryId().equals("blank");
                    assert selectTree.getInheritanceNodes().getParentFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getValueFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getLabelFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getDetailFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getHasChildrenFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getIconFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getMasterFieldId().equals("id");
                    assert selectTree.getInheritanceNodes().getSearchFilterId().equals("id");
                    assert selectTree.getInheritanceNodes().getEnabledFieldId().equals("test");
                    assertPreFilter(selectTree.getInheritanceNodes().getPreFilters()[0], true);
                });
    }

    @Test
    public void testInputSelectTreeXmlIOV1(){
        assert tester.check("net/n2oapp/framework/config/io/control/testInputSelectTreeReader1.fieldset.xml",
                (N2oFieldSet fieldSet) -> {
                    assertCountField(fieldSet, 2);
                    N2oInputSelectTree selectTree1 = (N2oInputSelectTree) fieldSet.getItems()[0];
//                    assertStandardSingleListAttribute(selectTree1, false);

                    assert selectTree1.getAjax().equals(false);
                    assert selectTree1.getSearch();
                    assert selectTree1.getGroupingNodes().getMasterFieldId().equals("id");
                    assert selectTree1.getGroupingNodes().getQueryId().equals("blank");
                    assert selectTree1.getGroupingNodes().getDetailFieldId().equals("id");
                    assert selectTree1.getGroupingNodes().getValueFieldId().equals("id");
                    assert selectTree1.getGroupingNodes().getSearchFieldId().equals("id");
                    assert selectTree1.getCheckboxes() == null;
                    List<GroupingNodes.Node> nodes = selectTree1.getGroupingNodes().getNodes();
                    for(int i = 0; i < 2; i++) {
                        assert nodes.size() == 1;
                        assert nodes.get(0).getLabelFieldId().equals("id");
                        assert nodes.get(0).getValueFieldId().equals("id");
                        assert nodes.get(0).getEnabled();
                        nodes = nodes.get(0).getNodes();
                    }
                    assertPreFilter(selectTree1.getGroupingNodes().getPreFilters()[0], true);

                    N2oInputSelectTree selectTree2 = (N2oInputSelectTree) fieldSet.getItems()[1];
                    assert selectTree2.getInheritanceNodes() != null;
                    assert selectTree2.getInheritanceNodes().getEnabledFieldId().equals("test");
                    assert selectTree2.getInheritanceNodes().getHasChildrenFieldId().equals("test");
                    assert selectTree2.getInheritanceNodes().getParentFieldId().equals("test");
                    assert selectTree2.getInheritanceNodes().getLabelFieldId().equals("test");
                    assert selectTree2.getInheritanceNodes().getDetailFieldId().equals("test");
                    assert selectTree2.getInheritanceNodes().getIconFieldId().equals("test");
                });
    }
}
