package net.n2oapp.framework.ui.controller.export.format;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.ui.controller.export.FormatUtil;
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

    /**
     * Оборачивает значение в двойные кавычки, удваивая кавычки внутри (RFC 4180),
     * чтобы значение не могло «выйти» из поля и начать новую ячейку.
     */
    private static final UnaryOperator<String> QUOTE = s -> s == null ? "\"\"" : "\"" + s.replace("\"", "\"\"") + "\"";

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
        List<DataSet> formattedData = formatData(data, headers);
        byte[] fileBytes = null;
        try {
            List<String[]> csvData = resolveToCsvFormat(formattedData, headers);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (CSVWriter writer = new CSVWriter(
                    new OutputStreamWriter(baos, Charset.forName(charset)),
                    csvSeparator,
                    ICSVWriter.NO_QUOTE_CHARACTER,
                    ICSVWriter.NO_ESCAPE_CHARACTER,
                    ICSVWriter.DEFAULT_LINE_END)) {
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
        if (data == null || headers == null || data.isEmpty()) {
            return List.of();
        }

        List<String[]> csvData = new ArrayList<>(data.size() + 1);

        // Заголовки: санитайзер + обёртка в кавычки
        csvData.add(headers.stream()
                .map(field -> QUOTE.apply(FormatUtil.sanitizeFormula(field.getTitle())))
                .toArray(String[]::new));

        // Данные
        for (DataSet row : data) {
            csvData.add(headers.stream()
                    .map(header -> formatCell(row.get(header.getId())))
                    .toArray(String[]::new));
        }

        return csvData;
    }

    /**
     * Форматирует ячейку для CSV:
     * <ul>
     *     <li>{@code null} → пустая строка;</li>
     *     <li>{@link String} → санитизация + обёртка в кавычки;</li>
     *     <li>{@link Number} → как есть (Excel видит число, не формулу);</li>
     *     <li>остальное (List, Map, кастомные объекты) → санитизация {@code toString()} и обёртка в кавычки при необходимости.</li>
     * </ul>
     */
    private String formatCell(Object value) {
        return switch (value) {
            case null -> "";
            case String s -> QUOTE.apply(FormatUtil.sanitizeFormula(s));
            case Number n -> n.toString();
            default -> quoteIfNeeded(FormatUtil.sanitizeFormula(value.toString()));
        };
    }

    /**
     * Оборачивает значение в кавычки, если оно содержит разделитель, кавычки или перевод строки.
     */
    private String quoteIfNeeded(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuote = value.indexOf(csvSeparator) >= 0
                || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0
                || value.indexOf('\r') >= 0;
        return needsQuote ? QUOTE.apply(value) : value;
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