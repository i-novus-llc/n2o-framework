package net.n2oapp.framework.ui.controller.export.format;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import lombok.NoArgsConstructor;
import net.n2oapp.criteria.dataset.DataSet;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Component
public class CsvFileGenerator implements FileGenerator {

    private char CSV_SEPARATOR = ';';
    private final String FILE_FORMAT = "csv";

    @Override
    public byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data) {
        byte[] fileBytes = null;

        try {
            String fullFileName = fileDir + "/" + fileName + "." + FILE_FORMAT;

            FileWriter fileWriter = new FileWriter(fullFileName, Charset.forName(charset));
            CSVWriter writer = new CSVWriter(
                    fileWriter, CSV_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER,
                    ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    ICSVWriter.DEFAULT_LINE_END
            );

            List<String[]> csvData = resolveToCsvFormat(data);
            writer.writeAll(csvData);

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
        this.CSV_SEPARATOR = separator;
    }

    private List<String[]> resolveToCsvFormat(List<DataSet> data) {
        Set<String> keys = data.get(0).flatKeySet();
        int columnCount = keys.size();
        String[] headers = new String[columnCount];

        Iterator<String> keysIterator = keys.iterator();

        for (int i = 0; i < columnCount; i++) {
            headers[i] = keysIterator.next();
        }

        List<String[]> csvData = new ArrayList<>();
        csvData.add(headers);

        for (DataSet str : data) {
            String[] csvStr = new String[columnCount];
            int i = 0;
            for (String key : keys) {
                Object value = str.get(key);
                if (value != null) {
                    csvStr[i] = value.toString();
                }
                i++;
            }

            csvData.add(csvStr);
        }

        return csvData;
    }
}
