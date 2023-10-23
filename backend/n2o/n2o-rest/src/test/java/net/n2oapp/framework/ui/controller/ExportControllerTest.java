package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.controller.export.format.CsvFileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExportControllerTest extends DataControllerTestBase {

    private final FileGeneratorFactory factory = new FileGeneratorFactory(List.of(new CsvFileGenerator()));

    @Test
    void test() {
        DataSet body1 = new DataSet();
        DataSet body2 = new DataSet();
        DataSet body3 = new DataSet();

        body1.put("id", 1);
        body1.put("name", "test1");
        body1.put("list", Arrays.asList(1, 2, 3));

        body2.put("id", 2);
        body2.put("name", "test2");
        body2.put("list", Arrays.asList(1, 2, 3));

        body3.put("id", 3);
        body3.put("name", "test3");
        body3.put("list", Arrays.asList(1, 2, 3));

        List<DataSet> list = new ArrayList<>();
        list.add(body1);
        list.add(body2);
        list.add(body3);

        ExportController exportController = new ExportController(builder.getEnvironment(), null, factory);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("id", "Идентификатор");
        headers.put("name", "Наименование");
        headers.put("list", "Список");
        ExportResponse export = exportController.export(list, "csv", "UTF-8", headers);

        String act = new String(export.getFile(), StandardCharsets.UTF_8);
        String exp = "\"\"\"Идентификатор\"\"\";\"\"\"Наименование\"\"\";\"\"\"Список\"\"\"\n" +
                "1;\"\"\"test1\"\"\";[1, 2, 3]\n" +
                "2;\"\"\"test2\"\"\";[1, 2, 3]\n" +
                "3;\"\"\"test3\"\"\";[1, 2, 3]\n";

        assertThat(act, is(exp));
        assertThat(export.getCharacterEncoding(), is("UTF-8"));
        assertThat(export.getContentType(), is("text/csv"));
        assertThat(export.getContentDisposition().matches("attachment;filename=export_data_\\d{13}\\.csv"), is(true));
        assertThat(export.getContentLength(), is(exp.getBytes(StandardCharsets.UTF_8).length));

        export = exportController.export(list, "csv", "Cp1251", headers);

        Charset cp1251 = Charset.forName("cp1251");
        act = new String(export.getFile(), cp1251);

        assertThat(act, is(exp));
        assertThat(export.getCharacterEncoding(), is("Cp1251"));
        assertThat(export.getContentType(), is("text/csv"));
        assertThat(export.getContentDisposition().matches("attachment;filename=export_data_\\d{13}\\.csv"), is(true));
        assertThat(export.getContentLength(), is(exp.getBytes(cp1251).length));
    }
}
