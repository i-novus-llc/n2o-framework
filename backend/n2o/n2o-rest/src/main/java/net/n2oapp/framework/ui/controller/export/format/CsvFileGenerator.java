package net.n2oapp.framework.ui.controller.export.format;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import lombok.NoArgsConstructor;
import net.n2oapp.criteria.dataset.DataSet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

@NoArgsConstructor
@Component
public class CsvFileGenerator implements FileGenerator {

    private static final String FILE_FORMAT = "csv";
    private char csvSeparator = ';';

    @Override
    public byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data, List<String> headers) {
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

    public void setSeparator(char separator) {
        this.csvSeparator = separator;
    }

    private List<String[]> resolveToCsvFormat(List<DataSet> data, List<String> headers) {
        if (data.isEmpty())
            return new ArrayList<>();

        Set<String> keys = data.get(0).flatKeySet();
        int columnCount = keys.size();
        List<String[]> csvData = new ArrayList<>();

        UnaryOperator<String> quoteWrapper = s -> "\"".concat(s).concat("\"");
        headers.replaceAll(quoteWrapper);
        csvData.add(headers.toArray(new String[0]));

        for (DataSet str : data) {
            String[] csvStr = new String[columnCount];
            int i = 0;
            for (String key : keys) {
                Object value = str.get(key);
                if (value != null)
                    csvStr[i] = value instanceof String valueStr ?
                            quoteWrapper.apply(valueStr) :
                            value.toString();
                i++;
            }

            csvData.add(csvStr);
        }

        return csvData;
    }
}
