package net.n2oapp.framework.ui.controller.export;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.ui.controller.AbstractController;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.format.FileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExportController extends AbstractController {

    private static final String FILES_DIRECTORY_NAME = System.getProperty("java.io.tmpdir");
    private static final String FILE_NAME = "export_data";
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
        FileGenerator generator = fileGeneratorFactory.getGenerator(lowerFormat);
        byte[] fileBytes = generator
                .createFile(FILE_NAME, FILES_DIRECTORY_NAME, charset, data, new ArrayList<>(headers.values()));

        if (fileBytes == null)
            response.setStatus(500);

        response.setFile(fileBytes);
        response.setContentType(generator.getContentType());
        response.setContentDisposition(String.format(CONTENT_DISPOSITION_FORMAT, getFileName(lowerFormat)));
        response.setCharacterEncoding(charset);
        response.setContentLength(fileBytes == null ? 0 : fileBytes.length);

        return response;
    }

    public GetDataResponse getData(String path, Map<String, String[]> params, UserContext user) {
        GetDataResponse data = dataController.getData(path, params, user);
        initDataByParentId(data, params.get("parent_id"));
        initDataBySourceField(data, params.get("source_field_id"));
        initShowedFields(data, params.get("show"));

        return data;
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

    private String getFileName(String fileFormat) {
        return FILE_NAME + "_" + System.currentTimeMillis() + "." + fileFormat;
    }

    /**
     * Функция оставляет только поля, указанные в параметре 'show'
     *
     * @param dataResponse - данные для экспорта
     * @param showedFields - имена отображаемых полей
     */
    private void initShowedFields(GetDataResponse dataResponse, String[] showedFields) {
        if (dataResponse.getList().isEmpty() || StringUtils.isEmpty(showedFields))
            return;

        for (int i = 0; i < dataResponse.getList().size(); i++) {
            DataSet dataSet = dataResponse.getList().get(i);
            DataSet orderedItem = new DataSet();
            for (String key : showedFields)
                orderedItem.put(key, dataSet.get(key));
            dataResponse.getList().set(i, orderedItem);
        }
    }
}
