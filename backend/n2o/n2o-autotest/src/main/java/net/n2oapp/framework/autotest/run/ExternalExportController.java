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

/**
 * Контроллер внешнего сервиса экспорта для автотестов.
 * Принимает ExternalRequest и возвращает CSV файл.
 */
@RestController
public class ExternalExportController {

    private static final String DELIMITER = ";";

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
        StringBuilder sb = new StringBuilder();

        // Header row
        List<ExternalRequest.ExportField> fields = request.getFields();
        if (fields != null && !fields.isEmpty()) {
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) sb.append(DELIMITER);
                sb.append("\"").append(escapeQuotes(fields.get(i).getTitle())).append("\"");
            }
            sb.append("\n");
        }

        // Data rows - generate test data based on filters
        List<ExternalRequest.ExportFilter> filters = request.getFilters();
        boolean hasNameFilter = false;
        String nameFilterValue = null;
        if (filters != null) {
            for (ExternalRequest.ExportFilter filter : filters) {
                if ("name".equals(filter.getId()) && filter.getValue() != null) {
                    hasNameFilter = true;
                    nameFilterValue = filter.getValue();
                    break;
                }
            }
        }

        // Generate test data
        String[][] testData = {
                {"1", "ey88ee-rugh34-asd4", "РМИС Республика Адыгея(СТП)", "Республика Адыгея"},
                {"2", "ey88ee-ruqah34-54eqw", "РМИС Республика Татарстан(тестовая для ПСИ)", "Республика Татарстан"},
                {"3", "ey88ea-ruaah34-54eqw", "ТМК", ""},
                {"4", "ey88ee-asd52a-54eqw", "МИС +МЕД", "Республика Адыгея"},
                {"5", "ey88fe-asd52a-54eqb", "РМИС Комстромской области", "Комстромская область"}
        };

        for (String[] row : testData) {
            if (hasNameFilter && !row[2].toLowerCase().contains(nameFilterValue.toLowerCase())) {
                continue;
            }
            if (fields != null) {
                for (int i = 0; i < fields.size(); i++) {
                    if (i > 0) sb.append(DELIMITER);
                    String fieldId = fields.get(i).getId();
                    String value = getValueByFieldId(row, fieldId);
                    sb.append("\"").append(escapeQuotes(value)).append("\"");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
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
