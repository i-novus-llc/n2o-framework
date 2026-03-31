package net.n2oapp.framework.ui.controller.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.ui.controller.AbstractController;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.format.FileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportController extends AbstractController {

    private static final String FILES_DIRECTORY_NAME = System.getProperty("java.io.tmpdir");
    private static final Pattern FIRST_WORD_PATTERN = Pattern.compile("^\\s*([\\w.]+)");
    private final DataController dataController;
    private final FileGeneratorFactory fileGeneratorFactory;
    private final RestTemplate restTemplate = new RestTemplate();

    public ExportController(MetadataEnvironment environment, DataController dataController,
                            FileGeneratorFactory fileGeneratorFactory) {
        super(environment);
        this.dataController = dataController;
        this.fileGeneratorFactory = fileGeneratorFactory;
    }

    public ExportResponse export(List<DataSet> data, String fileFormat, String charset, List<ExportRequest.ExportField> headers, String filename) {
        ExportResponse response = new ExportResponse();
        String lowerFileFormat = fileFormat.toLowerCase();
        FileGenerator generator = fileGeneratorFactory.getGenerator(lowerFileFormat);
        String fileName = getFileName(filename, lowerFileFormat);
        byte[] fileBytes = generator.createFile(filename, FILES_DIRECTORY_NAME, charset, data, headers);

        if (fileBytes == null)
            response.setStatus(500);

        response.setFile(fileBytes);
        response.setContentType(generator.getContentType());
        response.setContentDisposition(encodeContentDisposition(fileName));
        response.setCharacterEncoding(charset);
        response.setContentLength(fileBytes == null ? 0 : fileBytes.length);

        return response;
    }

    public ExportResponse exportByExternalService(ExportRequest request, OutputStream outputStream) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ExternalRequest externalRequest = new ExternalRequest();
        externalRequest.setFormat(request.getFormat());
        externalRequest.setCharset(request.getCharset());
        externalRequest.setFilename(request.getFilename());
        if (request.getFields() != null) {
            externalRequest.setFields(request.getFields().stream()
                    .map(f -> {
                        ExternalRequest.ExportField field = new ExternalRequest.ExportField();
                        field.setId(f.getId());
                        field.setTitle(f.getTitle());
                        field.setFormat(f.getFormat());
                        return field;
                    })
                    .toList());
        }
        Map<String, String[]> params = RouteUtil.parseQueryParams(RouteUtil.parseQuery(request.getUrl()));
        if (params.containsKey("page")) {
            externalRequest.setPage(Integer.parseInt(params.get("page")[0]));
        }
        if (params.containsKey("size")) {
            externalRequest.setSize(Integer.parseInt(params.get("size")[0]));
        }
        externalRequest.setFilters(buildFiltersFromQuery(request.getUrl(), params));
        ExportResponse responseMetadata = new ExportResponse();
        restTemplate.execute(
                request.getExternalUrl(),
                HttpMethod.POST,
                req -> {
                    req.getHeaders().putAll(headers);
                    new ObjectMapper().writeValue(
                            req.getBody(),
                            externalRequest
                    );
                },
                response -> {
                    responseMetadata.setStatus(response.getStatusCode().value());
                    responseMetadata.setContentType(response.getHeaders().getContentType() != null
                            ? response.getHeaders().getContentType().toString()
                            : MediaType.APPLICATION_OCTET_STREAM_VALUE);

                    String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
                    responseMetadata.setContentDisposition(Objects.requireNonNullElseGet(contentDisposition,
                            () -> encodeContentDisposition(getFileName(externalRequest.getFilename(), externalRequest.getFormat().toLowerCase()))));

                    long contentLength = response.getHeaders().getContentLength();
                    responseMetadata.setContentLength(contentLength > 0 ? (int) contentLength : -1);

                    StreamUtils.copy(response.getBody(), outputStream);
                    return null;
                });

        return responseMetadata;
    }

    public GetDataResponse getData(String path, Map<String, String[]> params, UserContext user) {
        GetDataResponse data = dataController.getData(path, params, user);
        initDataByParentId(data, params.get("parent_id"));
        initDataBySourceField(data, params.get("source_field_id"));

        return data;
    }

    /**
     * Метод оставляет родительские данные, которые соответствуют переданному значению.
     * Это используется при экспорте вложенных данных, которые зависят от родителя.
     * Пример json:
     * <pre>{@code
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
     * }</pre>
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
     * <pre>{@code
     *      {
     *          id: 1,
     *          child: [
     *              {
     *                  id: 1,
     *                  name: 1
     *              },
     *              {
     *                  id: 2,
     *                  name: 2
     *              }
     *          ]
     *      }
     * }</pre>
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

    private List<ExternalRequest.ExportFilter> buildFiltersFromQuery(String url, Map<String, String[]> params) {
        String prefix = "n2o/data";
        String dataUrl = RouteUtil.parsePath(url.substring(url.indexOf(prefix) + prefix.length()));
        QueryContext queryCtx = (QueryContext) router.get(dataUrl, CompiledQuery.class, params);
        DataSet data = queryCtx.getParams(dataUrl, params);
        CompiledQuery query = environment.getReadCompileBindTerminalPipelineFunction()
                .apply(new N2oPipelineSupport(environment))
                .get(queryCtx, data);

        List<ExternalRequest.ExportFilter> filters = new ArrayList<>();
        Map<String, String> paramToFilterIdMap = query.getParamToFilterIdMap();
        if (paramToFilterIdMap == null || data == null) {
            return filters;
        }
        Map<String, N2oQuery.Filter> filterFieldsMap = query.getFilterFieldsMap();
        for (Map.Entry<String, String> entry : paramToFilterIdMap.entrySet()) {
            String param = entry.getKey();
            String filterId = RouteUtil.normalizeParam(entry.getValue());
            if (data.containsKey(param)) {
                Object value = data.get(param);
                if (value != null) {
                    ExternalRequest.ExportFilter filter = new ExternalRequest.ExportFilter();
                    filter.setId(extractFilterId(filterFieldsMap, filterId));
                    filter.setValue(value.toString());
                    filters.add(filter);
                }
            }
        }
        return filters;
    }

    private String extractFilterId(Map<String, N2oQuery.Filter> filterFieldsMap, String filterId) {
        N2oQuery.Filter queryFilter = filterFieldsMap.get(filterId);
        if (queryFilter != null && queryFilter.getText() != null) {
            Matcher matcher = FIRST_WORD_PATTERN.matcher(queryFilter.getText());
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return filterId;
    }
}