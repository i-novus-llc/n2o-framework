package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportResponse;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExportControllerTest {

    @Test
    public void test() {
        DataSet body1 = new DataSet();
        DataSet body2 = new DataSet();
        DataSet body3 = new DataSet();

        body1.put("id", "1");
        body1.put("name", "test1");
        body1.put("list", Arrays.asList(1, 2, 3));

        body2.put("id", "2");
        body2.put("name", "test2");
        body2.put("list", Arrays.asList(1, 2, 3));

        body3.put("id", "3");
        body3.put("name", "test3");
        body3.put("list", Arrays.asList(1, 2, 3));

        List<DataSet> list = new ArrayList<>();
        list.add(body1);
        list.add(body2);
        list.add(body3);

        ExportController exportController = new ExportController(null, null, null);
        ExportResponse export = exportController.export(list, "csv", "UTF-8");

        String act = new String(export.getFile(), StandardCharsets.UTF_8);
        String exp = "id;name;list\n" +
                "1;test1;[1, 2, 3]\n" +
                "2;test2;[1, 2, 3]\n" +
                "3;test3;[1, 2, 3]\n";

        assertThat(act, is(exp));
        assertThat(export.getCharacterEncoding(), is("UTF-8"));
        assertThat(export.getContentType(), is("csv;charset=UTF-8"));
        assertThat(export.getContentDisposition(), is("attachment;filename=export.csv"));
        assertThat(export.getContentLength(), is(exp.getBytes(StandardCharsets.UTF_8).length));
    }
}
