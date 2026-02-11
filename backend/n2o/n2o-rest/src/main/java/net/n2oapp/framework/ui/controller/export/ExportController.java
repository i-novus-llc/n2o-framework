package net.n2oapp.framework.ui.controller.export;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.ui.controller.AbstractController;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.format.FileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import org.springframework.http.ContentDisposition;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.n2oapp.framework.ui.controller.export.FormatUtil.*;

public class ExportController extends AbstractController {

    private static final String FILES_DIRECTORY_NAME = System.getProperty("java.io.tmpdir");
    private final DataController dataController;
    private final FileGeneratorFactory fileGeneratorFactory;

    public ExportController(MetadataEnvironment environment, DataController dataController, FileGeneratorFactory fileGeneratorFactory) {
        super(environment);
        this.dataController = dataController;
        this.fileGeneratorFactory = fileGeneratorFactory;
    }

    public ExportResponse export(List<DataSet> data, String fileFormat, String charset, List<ExportRequest.ExportField> headers, String filename) {
        ExportResponse response = new ExportResponse();
        List<DataSet> formattedData = formatData(data, headers);
        String lowerFileFormat = fileFormat.toLowerCase();
        FileGenerator generator = fileGeneratorFactory.getGenerator(lowerFileFormat);
        String fileName = getFileName(filename, lowerFileFormat);
        byte[] fileBytes = generator.createFile(filename, FILES_DIRECTORY_NAME, charset, formattedData, headers);

        if (fileBytes == null)
            response.setStatus(500);

        response.setFile(fileBytes);
        response.setContentType(generator.getContentType());
        response.setContentDisposition(encodeContentDisposition(fileName));
        response.setCharacterEncoding(charset);
        response.setContentLength(fileBytes == null ? 0 : fileBytes.length);

        return response;
    }

    public GetDataResponse getData(String path, Map<String, String[]> params, UserContext user) {
        GetDataResponse data = dataController.getData(path, params, user);
        initDataByParentId(data, params.get("parent_id"));
        initDataBySourceField(data, params.get("source_field_id"));

        return data;
    }

    private List<DataSet> formatData(List<DataSet> data, List<ExportRequest.ExportField> headers) {
        if (data == null || headers == null)
            return data;

        for (DataSet dataSet : data) {
            for (ExportRequest.ExportField header : headers) {
                String fieldId = header.getId();
                String format = header.getFormat();
                if (format != null && fieldId != null) {
                    Object value = dataSet.get(fieldId);
                    if (value != null) {
                        String formattedValue = applyFormat(value, format);
                        dataSet.put(fieldId, formattedValue);
                    }
                }
            }
        }
        return data;
    }

    private String applyFormat(Object value, String format) {
        if (format == null || value == null)
            return value != null ? value.toString() : null;

        String formatLower = format.toLowerCase().trim();

        if (formatLower.startsWith("number ")) {
            String pattern = format.substring(7).trim();
            return formatNumber(value, pattern);
        } else if (formatLower.startsWith("date ")) {
            String pattern = format.substring(5).trim();
            return formatDate(value, pattern);
        } else if (formatLower.equals("password")) {
            return maskPassword(value.toString());
        } else if (formatLower.equals("phone")) {
            return formatPhone(value.toString());
        } else if (formatLower.equals("snils")) {
            return formatSnils(value.toString());
        }

        return value.toString();
    }

    /**
     * Метод оставляет родительские данные, которые соответствуют переданному значению.
     * Это используется при экспорте вложенных данных, которые зависят от родителя.
     * Пример json:
     * [
     *     {
     *          id: 1,
     *          child: {
     *              id: 1,
     *              name: 2
     *          }
     *     },
     *     {
     *         id: 2,
     *         child: {
     *              id: 1,
     *              name: 3
     *         }
     *     }
     * ]
     * Если будет передан 'parent-id' равный 1, то останется только родитель с id=1, соответственно и останутся его потомки.
     * Иначе оставляет исходные данные.
     */
    private void initDataByParentId(GetDataResponse data, String[] parentIds) {
        if (parentIds == null)
            return;
        data.setList(
                data.getList().stream()
                        .filter(dataSet -> Objects.nonNull(dataSet.get("id")))
                        .filter(dataSet -> dataSet.get("id").toString().equals(parentIds[0]))
                        .toList()
        );
    }

    /**
     * Оставляет в данных второй уровень сложности
     * Пример json:
     * {
     *     id: 1,
     *     child: [
     *          {
     *              id: 1,
     *              name: 1
     *          },
     *          {
     *              id: 2,
     *              name: 2
     *          }
     *     ]
     * }
     * При переданном sourceFieldId равным 'child' останутся данные, вложенные по ключу child.
     * Иначе оставляет исходные данные.
     *
     * @param sourceFieldId - ключ для нахождения второго уровня вложенности
     */
    private void initDataBySourceField(GetDataResponse data, String[] sourceFieldId) {
        if (sourceFieldId == null)
            return;
        ArrayList<DataSet> dataSets = new ArrayList<>();
        data.getList().forEach(ds -> dataSets.addAll((List<DataSet>) ds.getList(sourceFieldId[0])));
        data.setList(dataSets);
    }

    protected String getFileName(String customFilename, String fileFormat) {
        return customFilename + "." + fileFormat;
    }

    private String encodeContentDisposition(String fileName) {
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        return contentDisposition.toString();
    }
}