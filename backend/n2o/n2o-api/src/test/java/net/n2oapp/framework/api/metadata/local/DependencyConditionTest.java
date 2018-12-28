package net.n2oapp.framework.api.metadata.local;

import org.junit.Test;
import net.n2oapp.framework.api.metadata.local.view.page.DependencyCondition;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.*;

/**
 * User: operhod
 * Date: 04.06.14
 * Time: 16:06
 */
public class DependencyConditionTest {


    @Test
    public void test() throws Exception {
        String pageId = "page";
        Collection<String> pageContainers = Arrays.asList("patient", "document");
        DependencyCondition forTest = new DependencyCondition(
                "patient.id == 1 && document.id == null", pageId, pageContainers, new ScriptProcessor());
        assert forTest.getCondition().replace(";", "").replace("\n", "").equals("page.patient.id == 1 && page.document.id == null");
        assert forTest.getContainerIds().size() == 2;
        assert forTest.getContainerIds().contains("page.patient");
        assert forTest.getContainerIds().contains("page.document");
    }

}
