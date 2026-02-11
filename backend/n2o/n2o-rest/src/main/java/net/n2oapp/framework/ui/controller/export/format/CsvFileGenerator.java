package net.n2oapp.framework.ui.controller.export.format;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import lombok.NoArgsConstructor;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportRequest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@NoArgsConstructor
@Component
public class CsvFileGenerator implements FileGenerator {

    private static final String FILE_FORMAT = "csv";
    private char csvSeparator = ';';

    @Override
    public byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data, List<ExportRequest.ExportField> headers) {
        byte[] fileBytes = null;

        try {
            String fullFileName = fileDir + File.separator + fileName + "." + FILE_FORMAT;

            FileWriter fileWriter = new FileWriter(fullFileName, Charset.forName(charset));
            CSVWriter writer = new CSVWriter(
                    fileWriter, csvSeparator,
                    '\'',
                    '\'',
                    ICSVWriter.DEFAULT_LINE_END
            );

            List<String[]> csvData = resolveToCsvFormat(data, headers);
            writer.writeAll(csvData, false);
            writer.close();

            fileBytes = Files.readAllBytes(Path.of(fullFileName));
        } catch (IOException e) {
            e.printStackTrace();
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

    public void setSeparator(char separator) {
        this.csvSeparator = separator;
    }

    private List<String[]> resolveToCsvFormat(List<DataSet> data, List<ExportRequest.ExportField> headers) {
        if (data.isEmpty())
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
}
