package net.n2oapp.framework.ui.controller;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.controller.export.format.CsvFileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import net.n2oapp.framework.ui.controller.export.format.XlsxFileGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class ExportControllerTest extends DataControllerTestBase {

    private final FileGeneratorFactory factory = new FileGeneratorFactory(List.of(new CsvFileGenerator()));

    @Test
    void testCsvExport() {
        List<DataSet> list = testData();

        ExportController exportController = new ExportController(builder.getEnvironment(), null, factory);
        List<ExportRequest.ExportField> headers = List.of(
                createExportField("id", "Идентификатор", null),
                createExportField("name", "Наименование", null),
                createExportField("list", "Список", null),
                createExportField("type.name", "Тип", null)
        );
        ExportResponse export = exportController.export(list, "csv", "UTF-8", headers, "Пациенты");

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
        assertThat(export.getContentDisposition()
                        .equals("attachment; filename=\"=?UTF-8?Q?=D0=9F=D0=B0=D1=86=D0=B8=D0=B5=D0=BD=D1=82=D1=8B.csv?=\"; " +
                                "filename*=UTF-8''%D0%9F%D0%B0%D1%86%D0%B8%D0%B5%D0%BD%D1%82%D1%8B.csv"),
                is(true));
        assertThat(export.getContentLength(), is(exp.getBytes(StandardCharsets.UTF_8).length));

        List<ExportRequest.ExportField> headersCp1251 = List.of(
                createExportField("id", "Идентификатор", null),
                createExportField("name", "Наименование", null),
                createExportField("list", "Список", null),
                createExportField("type.name", "Тип", null)
        );
        export = exportController.export(list, "csv", "Cp1251", headersCp1251, "Пациенты");

        Charset cp1251 = Charset.forName("cp1251");
        act = new String(export.getFile(), cp1251);

        assertThat(act, is(exp));
        assertThat(export.getCharacterEncoding(), is("Cp1251"));
        assertThat(export.getContentType(), is("text/csv"));
        assertThat(export.getContentDisposition()
                        .equals("attachment; filename=\"=?UTF-8?Q?=D0=9F=D0=B0=D1=86=D0=B8=D0=B5=D0=BD=D1=82=D1=8B.csv?=\"; " +
                                "filename*=UTF-8''%D0%9F%D0%B0%D1%86%D0%B8%D0%B5%D0%BD%D1%82%D1%8B.csv"),
                is(true));
        assertThat(export.getContentLength(), is(exp.getBytes(cp1251).length));
    }

    @Test
    void testXlsxExport() {
        List<DataSet> list = testData();

        FileGeneratorFactory xlsxFactory = new FileGeneratorFactory(List.of(new XlsxFileGenerator()));

        ExportController exportController = new ExportController(builder.getEnvironment(), null, xlsxFactory);
        List<ExportRequest.ExportField> headers = List.of(
                createExportField("id", "Идентификатор", null),
                createExportField("name", "Наименование", null),
                createExportField("list", "Список", null),
                createExportField("type.name", "Тип", null)
        );

        ExportResponse export = exportController.export(list, "xlsx", "UTF-8", headers, "Пациенты");

        assertNotNull(export.getFile());
        assertThat(export.getCharacterEncoding(), is("UTF-8"));
        assertThat(export.getContentType(), is("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        assertThat(export.getContentDisposition()
                        .equals("attachment; filename=\"=?UTF-8?Q?=D0=9F=D0=B0=D1=86=D0=B8=D0=B5=D0=BD=D1=82=D1=8B.xlsx?=\"; " +
                                "filename*=UTF-8''%D0%9F%D0%B0%D1%86%D0%B8%D0%B5%D0%BD%D1%82%D1%8B.xlsx"),
                is(true));
        assertThat(export.getContentLength(), is(export.getFile().length));
    }

    /**
     * CSV/Formula injection
     * Проверяет, что значения, начинающиеся с любого опасного символа
     * (=, +, -, @, табуляция, CR), получают префикс-апостроф и не попадают
     * в выгрузку в «опасном» виде.
     */
    @ParameterizedTest(name = "Injection: [{0}]")
    @ValueSource(strings = {
            "=cmd|'/c calc'!A1",
            "=1+1",
            "+1+1",
            "-1+1",
            "@SUM(1,1)",
            "\t=1+1",
            "\r=1+1"
    })
    void testCsvExportNeutralizesFormulaInjection(String malicious) {
        List<DataSet> data = List.of(
                new DataSet(Map.of("id", 1, "name", malicious, "desc", "safe")));
        List<ExportRequest.ExportField> headers = List.of(
                createExportField("id", "ID", null),
                createExportField("name", "Name", null),
                createExportField("desc", "Desc", null));

        ExportController controller = new ExportController(builder.getEnvironment(), null, factory);
        ExportResponse response = controller.export(data, "csv", "UTF-8", headers, "test");
        String csv = new String(response.getFile(), StandardCharsets.UTF_8);

        // Нейтрализованный вариант присутствует
        assertTrue(csv.contains("\"'" + malicious + "\""),
                "Значение должно быть нейтрализовано апострофом. Actual:\n" + csv);
        // Небезопасный вариант отсутствует
        assertFalse(csv.contains("\"" + malicious + "\""),
                "Опасное значение не должно появляться без апострофа. Actual:\n" + csv);
    }

    /**
     * Значения, начинающиеся с безопасных символов, не должны модифицироваться,
     * а уже экранированные апострофом — не должны получить второй апостроф.
     */
    @Test
    void testCsvExportKeepsSafeValuesIntact() {
        List<DataSet> data = List.of(
                new DataSet(Map.of("id", 1, "name", "safe")),
                new DataSet(Map.of("id", 2, "name", "123-456-789 01")),
                new DataSet(Map.of("id", 3, "name", "'already-safe")));
        List<ExportRequest.ExportField> headers = List.of(
                createExportField("id", "ID", null),
                createExportField("name", "Name", null));

        ExportController controller = new ExportController(builder.getEnvironment(), null, factory);
        String csv = new String(
                controller.export(data, "csv", "UTF-8", headers, "test").getFile(),
                StandardCharsets.UTF_8);

        assertTrue(csv.contains("\"safe\""), csv);
        assertTrue(csv.contains("\"123-456-789 01\""), csv);
        // Уже с апострофом — не должно стать "''already-safe"
        assertTrue(csv.contains("\"'already-safe\""), csv);
        assertFalse(csv.contains("\"''already-safe\""), csv);
    }

    /**
     * Двойная кавычка внутри значения не должна закрывать поле и порождать
     * новую ячейку с формулой без апострофа.
     */
    @Test
    void testCsvExportEscapesQuotesInsideValue() {
        List<DataSet> data = List.of(
                new DataSet(Map.of("id", 1, "name", "x\";=cmd|'/c calc'!A1;\"")));
        List<ExportRequest.ExportField> headers = List.of(
                createExportField("id", "ID", null),
                createExportField("name", "Name", null));

        ExportController controller = new ExportController(builder.getEnvironment(), null, factory);
        String csv = new String(
                controller.export(data, "csv", "UTF-8", headers, "test").getFile(),
                StandardCharsets.UTF_8);

        // Кавычки внутри значения должны быть удвоены (RFC 4180),
        // а само значение обернуто в кавычки, чтобы не разорвать ячейку.
        String expectedSafeCell = "1;\"x\"\";=cmd|'/c calc'!A1;\"\"\"";
        assertTrue(csv.contains(expectedSafeCell),
                "Значение с внутренними кавычками должно быть правильно экранировано. Actual:\n" + csv);

        // Проверяем, что значение не разорвало ячейку, и формула не "выпала" в отдельную колонку
        String unsafeCell = "1;\"x\";=cmd|'/c calc'!A1;\"";
        assertFalse(csv.contains(unsafeCell),
                "Опасное значение не должно разрывать ячейку. Actual:\n" + csv);
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

    private ExportRequest.ExportField createExportField(String id, String title, String format) {
        ExportRequest.ExportField field = new ExportRequest.ExportField();
        field.setId(id);
        field.setTitle(title);
        field.setFormat(format);
        return field;
    }
}
