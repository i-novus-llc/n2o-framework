package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNoneRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import org.junit.Test;
import net.n2oapp.framework.api.metadata.local.view.widget.util.PageCompiler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * User: operehod
 * Date: 11.03.2015
 * Time: 10:24
 */
public class PageCompilerTest {

    @Test
    public void testAllGetContainers() throws Exception {
        N2oStandardPage page = new N2oStandardPage();
        N2oRegion region = new N2oNoneRegion();
        N2oTable table1 = new N2oTable();
        table1.setId("t1");
        N2oForm form1 = new N2oForm();
        form1.setId("f1");
        region.setWidgets(new N2oWidget[]{table1, form1});

        N2oRegion region2 = new N2oNoneRegion();
        N2oTable table2 = new N2oTable();
        table2.setId("t2");
        N2oForm n2oForm2 = new N2oForm();
        n2oForm2.setId("f2");
        region2.setWidgets(new N2oWidget[]{table2, n2oForm2});

        page.setN2oRegions(new N2oRegion[]{region, region2});

        List<String> list = PageCompiler.getAllContainers(page, (container) -> true).stream().map(N2oWidget::getId).collect(toList());
        assert list.equals(Arrays.asList("t1", "f1", "t2", "f2"));

        list = PageCompiler.getAllContainers(page, (container) -> container.getId().equals("t1")).stream().map(N2oWidget::getId).collect(toList());
        assert list.equals(Collections.singletonList("t1"));

        list = PageCompiler.getAllContainers(page, (container) -> container.getId().equals("f2")).stream().map(N2oWidget::getId).collect(toList());
        assert list.equals(Collections.singletonList("f2"));


    }
}
