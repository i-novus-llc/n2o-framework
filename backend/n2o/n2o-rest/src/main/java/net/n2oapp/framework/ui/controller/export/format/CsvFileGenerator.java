package net.n2oapp.framework.ui.controller.export.format;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static net.n2oapp.framework.ui.controller.export.FormatUtil.applyFormat;

@Component
public class CsvFileGenerator implements FileGenerator {
    private static final Logger log = LoggerFactory.getLogger(CsvFileGenerator.class);
    private static final String FILE_FORMAT = "csv";
    private final char csvSeparator;

    @Autowired
    public CsvFileGenerator(@Value("${n2o.api.generate.button.export.csv.columnSeparator}") String separator) {
        this.csvSeparator = (separator != null && !separator.isEmpty())
                ? separator.charAt(0)
                : ';';
    }

    public CsvFileGenerator() {
        this(";");
    }

    @Override
    public byte[] createFile(String charset, List<DataSet> data, List<ExportRequest.ExportField> headers) {
        byte[] fileBytes = null;
        List<DataSet> formattedData = formatData(data, headers);

        try {
            List<String[]> csvData = resolveToCsvFormat(formattedData, headers);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (CSVWriter writer = new CSVWriter(
                    new OutputStreamWriter(baos, Charset.forName(charset)),
                    csvSeparator, '\'', '\'', ICSVWriter.DEFAULT_LINE_END)) {
                writer.writeAll(csvData, false);
            }
            fileBytes = baos.toByteArray();
        } catch (IOException e) {
            log.error("Failed to generate CSV file", e);
        }

        return fileBytes;
    }

    @Override
    public String getFormat() {
        return FILE_FORMAT;
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }

    private List<String[]> resolveToCsvFormat(List<DataSet> data, List<ExportRequest.ExportField> headers) {
        if (data == null || headers == null || data.isEmpty())
            return new ArrayList<>();

        int columnCount = headers.size();
        List<String[]> csvData = new ArrayList<>();

        UnaryOperator<String> quoteWrapper = s -> "\"".concat(s).concat("\"");

        // Заголовки из title в порядке списка headers
        String[] titles = headers.stream()
                .map(field -> quoteWrapper.apply(field.getTitle()))
                .toArray(String[]::new);
        csvData.add(titles);

        // Формируем строки данных по id из headers
        for (DataSet row : data) {
            String[] csvRow = new String[columnCount];
            for (int i = 0; i < headers.size(); i++) {
                Object value = row.get(headers.get(i).getId());
                if (value != null) {
                    csvRow[i] = (value instanceof String valueStr)
                            ? quoteWrapper.apply(valueStr)
                            : value.toString();
                }
            }
            csvData.add(csvRow);
        }

        return csvData;
    }

    private List<DataSet> formatData(List<DataSet> data, List<ExportRequest.ExportField> headers) {
        if (data == null || headers == null)
            return data;

        for (DataSet dataSet : data) {
            for (ExportRequest.ExportField header : headers) {
                String fieldId = header.getId();
                String format = header.getFormat();
                if (format != null && fieldId != null) {
                    dataSet.computeIfPresent(fieldId, (key, value) -> applyFormat(value, format));
                }
            }
        }
        return data;
    }
}