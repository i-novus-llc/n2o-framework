package net.n2oapp.framework.ui.controller.export;

import lombok.RequiredArgsConstructor;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExportController {

    private static final String FILES_DIRECTORY_NAME = System.getProperty("java.io.tmpdir");
    private static final String FILE_NAME = "export";
    private static final String CONTENT_TYPE = "text/";
    private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s";
    private final DataController dataController;
    private final FileGeneratorFactory fileGeneratorFactory;

    public ExportResponse export(List<DataSet> data, String format, String charset) {
        ExportResponse response = new ExportResponse();
        String lowerFormat = format.toLowerCase();
        byte[] fileBytes = fileGeneratorFactory.getGenerator(lowerFormat)
                .createFile(FILE_NAME, FILES_DIRECTORY_NAME, charset, data);

        if (fileBytes == null)
            response.setStatus(500);

        response.setFile(fileBytes);
        response.setContentType(CONTENT_TYPE + format.toLowerCase());
        response.setContentDisposition(String.format(CONTENT_DISPOSITION_FORMAT, FILE_NAME + "." + format.toLowerCase()));
        response.setCharacterEncoding(charset);
        response.setContentLength(fileBytes == null ? 0 : fileBytes.length);

        return response;
    }

    public GetDataResponse getData(String path, Map<String, String[]> parameters, UserContext user) {
        return dataController.getData(path, parameters, user);
    }
}
