package net.n2oapp.framework.ui.controller;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ExportController extends AbstractController {

    private static final String FILES_DIRECTORY_NAME = "src/main/resources/META-INF/resources/";
    private static final String CSV_FILE_NAME = "export.csv";
    private static final String CONTENT_TYPE = "text/csv";
    private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s";
    private static final char CSV_SEPARATOR = ';';
    private final DataController dataController;

    public ExportController(MetadataEnvironment environment, DataController dataController) {
        super(environment);
        this.dataController = dataController;
    }

    public ExportController(MetadataEnvironment environment, MetadataRouter router, DataController dataController) {
        super(environment, router);
        this.dataController = dataController;
    }

    public ExportResponse export(List<DataSet> data, String format, String charset) {
        ExportResponse response = new ExportResponse();
        byte[] fileBytes = createCsv(data);

        if (fileBytes == null)
           response.setStatus(500);

        response.setFile(fileBytes);
        response.setContentType(CONTENT_TYPE);
        response.setContentDisposition(String.format(CONTENT_DISPOSITION_FORMAT, CSV_FILE_NAME));
        response.setCharacterEncoding(charset);
        response.setContentLength(fileBytes == null ? 0 : fileBytes.length);

        return response;
    }

    public GetDataResponse getData(String path, Map<String, String[]> parameters, UserContext user) {
        return dataController.getData(path, parameters, user);
    }

    private byte[] createCsv(List<DataSet> data) {
        byte[] fileBytes = null;

        try {
            String filePath = getClass().getResource(FILES_DIRECTORY_NAME + CSV_FILE_NAME).toURI().getPath();
            FileWriter fileWriter = new FileWriter(filePath, UTF_8);

            CSVWriter writer = new CSVWriter(fileWriter, CSV_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER,
                    ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    ICSVWriter.DEFAULT_LINE_END);

            List<String[]> csvData = resolveToCsvFormat(data);
            writer.writeAll(csvData);

            writer.close();

            fileBytes = Files.readAllBytes(Path.of(filePath));
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return fileBytes;
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
                if (value instanceof List)
                    csvStr[i] = str.getList(key).toString();
                else
                    csvStr[i] = str.get(key).toString();
                i++;
            }

            csvData.add(csvStr);
        }

        return csvData;
    }
}
