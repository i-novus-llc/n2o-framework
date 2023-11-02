package net.n2oapp.framework.ui.controller.export;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.ui.controller.AbstractController;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportController extends AbstractController {

    private static final String FILES_DIRECTORY_NAME = System.getProperty("java.io.tmpdir");
    private static final String FILE_NAME = "export_data";
    private static final String CONTENT_TYPE = "text/";
    private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s";
    private final DataController dataController;
    private final FileGeneratorFactory fileGeneratorFactory;

    public ExportController(MetadataEnvironment environment, DataController dataController, FileGeneratorFactory fileGeneratorFactory) {
        super(environment);
        this.dataController = dataController;
        this.fileGeneratorFactory = fileGeneratorFactory;
    }

    public ExportResponse export(List<DataSet> data, String format, String charset, Map<String, String> headers) {
        ExportResponse response = new ExportResponse();
        String lowerFormat = format.toLowerCase();
        byte[] fileBytes = fileGeneratorFactory.getGenerator(lowerFormat)
                .createFile(FILE_NAME, FILES_DIRECTORY_NAME, charset, data, resolveHeaders(data, headers));

        if (fileBytes == null)
            response.setStatus(500);

        response.setFile(fileBytes);
        response.setContentType(CONTENT_TYPE + lowerFormat);
        response.setContentDisposition(String.format(CONTENT_DISPOSITION_FORMAT, getFileName(lowerFormat)));
        response.setCharacterEncoding(charset);
        response.setContentLength(fileBytes == null ? 0 : fileBytes.length);

        return response;
    }

    public GetDataResponse getData(String path, Map<String, String[]> params, UserContext user) {
        return dataController.getData(path, params, user);
    }

    public Map<String, String> getHeaders(String path, Map<String, String[]> params) {
        QueryRequestInfo queryRequestInfo = this.createQueryRequestInfo(path, params, null);
        return getFieldsNames(queryRequestInfo.getQuery().getDisplayFields());
    }

    private List<String> resolveHeaders(List<DataSet> data, Map<String, String> headers) {
        ArrayList<String> resolvedHeaders = new ArrayList<>();
        if (!data.isEmpty())
            for (String key : data.get(0).flatKeySet())
                resolvedHeaders.add(headers.get(key));

        return resolvedHeaders;
    }

    private Map<String, String> getFieldsNames(List<AbstractField> fields) {
        Map<String, String> names = new HashMap<>();
        for (AbstractField field : fields) {
            if (field instanceof QueryReferenceField) {
                names.putAll(getFieldsNames(List.of(((QueryReferenceField) field).getFields())));
                continue;
            }
            QuerySimpleField simpleField = (QuerySimpleField) field;
            names.put(simpleField.getId(), simpleField.getName());
        }

        return names;
    }

    private String getFileName(String fileFormat) {
        return FILE_NAME + "_" + System.currentTimeMillis() + "." + fileFormat;
    }
}
