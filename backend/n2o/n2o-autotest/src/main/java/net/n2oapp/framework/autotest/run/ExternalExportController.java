package net.n2oapp.framework.autotest.run;

import net.n2oapp.framework.ui.controller.export.ExternalRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер внешнего сервиса экспорта для автотестов.
 * Принимает ExternalRequest и возвращает CSV файл.
 */
@RestController
public class ExternalExportController {

    private static final String DELIMITER = ";";
    private static final String[][] TEST_DATA = {
            {"1", "ey88ee-rugh34-asd4", "РМИС Республика Адыгея(СТП)", "Республика Адыгея"},
            {"2", "ey88ee-ruqah34-54eqw", "РМИС Республика Татарстан(тестовая для ПСИ)", "Республика Татарстан"},
            {"3", "ey88ea-ruaah34-54eqw", "ТМК", ""},
            {"4", "ey88ee-asd52a-54eqw", "МИС +МЕД", "Республика Адыгея"},
            {"5", "ey88fe-asd52a-54eqb", "РМИС Комстромской области", "Комстромская область"}
    };

    @GetMapping("/external/export/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ExternalExportController is available");
    }

    @PostMapping(value = "/external/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> export(@RequestBody ExternalRequest request) {
        Charset charset = resolveCharset(request.getCharset());
        String csv = generateCsv(request);
        byte[] content = csv.getBytes(charset);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", request.getFilename() + ".csv");
        headers.setContentLength(content.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }

    private String generateCsv(ExternalRequest request) {
        List<ExternalRequest.ExportField> fields = request.getFields();
        if (fields == null)
            return "";

        StringBuilder sb = new StringBuilder();
        if (!fields.isEmpty())
            appendRow(sb, fields.stream().map(ExternalRequest.ExportField::getTitle).toList());

        String nameFilter = findNameFilterValue(request.getFilters());
        for (String[] row : TEST_DATA) {
            if (matchesNameFilter(row, nameFilter))
                appendRow(sb, fields.stream().map(f -> getValueByFieldId(row, f.getId())).toList());
        }
        return sb.toString();
    }

    private void appendRow(StringBuilder sb, List<String> values) {
        sb.append(values.stream()
                        .map(v -> "\"" + escapeQuotes(v) + "\"")
                        .collect(Collectors.joining(DELIMITER)))
                .append("\n");
    }

    private String findNameFilterValue(List<ExternalRequest.ExportFilter> filters) {
        if (filters == null)
            return null;
        return filters.stream()
                .filter(f -> "name".equals(f.getId()) && f.getValue() != null)
                .map(ExternalRequest.ExportFilter::getValue)
                .findFirst()
                .orElse(null);
    }

    private boolean matchesNameFilter(String[] row, String nameFilter) {
        return nameFilter == null || row[2].toLowerCase().contains(nameFilter.toLowerCase());
    }

    private String getValueByFieldId(String[] row, String fieldId) {
        return switch (fieldId) {
            case "id" -> row[0];
            case "id_ips" -> row[1];
            case "name" -> row[2];
            case "region" -> row[3];
            default -> "";
        };
    }

    private String escapeQuotes(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    private Charset resolveCharset(String charsetName) {
        if (charsetName == null)
            return StandardCharsets.UTF_8;
        if (charsetName.equalsIgnoreCase("windows-1251"))
            return Charset.forName("windows-1251");
        return StandardCharsets.UTF_8;
    }
}
