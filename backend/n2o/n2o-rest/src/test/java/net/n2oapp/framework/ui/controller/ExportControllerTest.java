package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.controller.export.format.CsvFileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import net.n2oapp.framework.ui.controller.export.format.XlsxFileGenerator;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExportControllerTest extends DataControllerTestBase {

    private final FileGeneratorFactory factory = new FileGeneratorFactory(List.of(new CsvFileGenerator()));

    @Test
    void testCsvExport() {
        List<DataSet> list = testData();

        ExportController exportController = new ExportController(builder.getEnvironment(), null, factory);
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "Идентификатор");
        headers.put("name", "Наименование");
        headers.put("list", "Список");
        headers.put("type.name", "Тип");
        ExportResponse export = exportController.export(list, "csv", "UTF-8", headers);

        String act = new String(export.getFile(), StandardCharsets.UTF_8);
        String exp = """
                "Идентификатор";"Наименование";"Список";"Тип"
                1;"test1";[1, 2, 3];"test1"
                2;"test2";[1, 2, 3];"test2"
                3;"test3";[1, 2, 3];"test3"
                """;

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

    @Test
    void testXlsxExport() {
        List<DataSet> list = testData();

        FileGeneratorFactory xlsxFactory = new FileGeneratorFactory(List.of(new XlsxFileGenerator()));

        ExportController exportController = new ExportController(builder.getEnvironment(), null, xlsxFactory);
        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("id", "Идентификатор");
        headers.put("name", "Наименование");
        headers.put("list", "Список");
        headers.put("type.name", "Тип");

        ExportResponse export = exportController.export(list, "xlsx", "UTF-8", headers);

        assertNotNull(export.getFile());
        assertThat(export.getCharacterEncoding(), is("UTF-8"));
        assertThat(export.getContentType(), is("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        assertThat(export.getContentDisposition().matches("attachment;filename=export_data_\\d{13}\\.xlsx"), is(true));
        assertThat(export.getContentLength(), is(export.getFile().length));
    }

    private List<DataSet> testData() {
        DataSet body1 = new DataSet();
        DataSet body2 = new DataSet();
        DataSet body3 = new DataSet();

        body1.put("id", 1);
        body1.put("name", "test1");
        body1.put("list", Arrays.asList(1, 2, 3));
        body1.put("type", Map.of("name", "test1"));

        body2.put("id", 2);
        body2.put("name", "test2");
        body2.put("list", Arrays.asList(1, 2, 3));
        body2.put("type", Map.of("name", "test2"));

        body3.put("id", 3);
        body3.put("name", "test3");
        body3.put("list", Arrays.asList(1, 2, 3));
        body3.put("type", Map.of("name", "test3"));

        List<DataSet> list = new ArrayList<>();
        list.add(body1);
        list.add(body2);
        list.add(body3);
        return list;
    }
}
