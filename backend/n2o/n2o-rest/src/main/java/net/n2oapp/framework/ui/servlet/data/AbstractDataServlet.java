package net.n2oapp.framework.ui.servlet.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.api.Direction;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.JsonUtil;
import net.n2oapp.framework.mvc.api.SimpleServletTemplate;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.mvc.util.ServletUtil;
import net.n2oapp.framework.ui.controller.request.GetDataRequest;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Абстрактная реализация сервлета получения данных
 */
public abstract class AbstractDataServlet extends N2oServlet {

    protected SimpleServletTemplate servletTemplate = new SimpleServletTemplate();

    protected GetDataRequest extractGetDataRequestParameters(HttpServletRequest request)
            throws ControllerArgumentException {
        GetDataRequest result = new GetDataRequest(request);
        Map<String, String> parameters = ServletUtil.decodeParameters(request);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getKey().equals("queryId")) {
                result.setQueryId(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("model")) {
                String model = entry.getValue();
                if (model != null) result.setModel(model);
                continue;
            }
            if (entry.getKey().equals("selectedId")) {
                String selectedId = entry.getValue();
                if (selectedId != null) result.setSelectedId(selectedId);
                continue;
            }
            if (entry.getKey().equals("pageId")) {
                result.setPageId(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("containerId")) {
                result.setContainerId(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("page")) {
                result.setPage(Integer.valueOf(entry.getValue()));
                continue;
            }
            if (entry.getKey().equals("size")) {
                result.setSize(Integer.valueOf(entry.getValue()));
                continue;
            }
            if (entry.getKey().equals("count")) {
                result.setCount(Integer.valueOf(entry.getValue()));
                continue;
            }
            if (entry.getKey().equals("contentType")) {
                result.setContentType(entry.getValue());
                continue;
            }
            if (entry.getKey().startsWith("sorting.")) {
                String fieldId = entry.getKey().substring("sorting.".length());
                Direction direction = Direction.valueOf(entry.getValue());
                result.getSortings().put(fieldId, direction);
                continue;
            }
            if (entry.getKey().startsWith("filter.")) {
                String fieldId = entry.getKey().substring("filter.".length());
                result.getFilters().put(fieldId, entry.getValue());
                continue;
            }
            if (entry.getKey().equals("encoding")) {
                continue;
            }
            if (entry.getKey().equals("columns")) {
                result.setColumns(Arrays.asList(entry.getValue().split(",")));
                continue;
            }
            ControllerArgumentException.throwUnknownParameter(entry.getKey());
        }

        if (request.getHeader("query-source") != null) {
            result.setQuerySource(request.getHeader("query-source"));
        }

        if (result.getQueryId() == null) {
            if (result.getPageId() == null) {
                ControllerArgumentException.throwRequiredParameter("pageId");
            }
            if (result.getContainerId() == null) {
                ControllerArgumentException.throwRequiredParameter("containerId");
            }
        }

        result.setUser(getUser(request));
        return result;
    }


    @SuppressWarnings("unchecked")
    protected <T, V> T extractSetDataRequestParameters(HttpServletRequest request, BiFunction<V, Map<String, String>, T> mapper)
            throws ControllerArgumentException, IOException {
        return mapper.apply((V) readBody(request), ServletUtil.decodeParameters(request));

    }

    protected Object readBody(HttpServletRequest request) throws IOException {
        if (request.getReader() == null) return new DataSet();
        String body = IOUtils.toString(request.getReader()).trim();
        ObjectMapper objectMapper = JsonUtil.getMapper();
        if (body.startsWith("[")) {
            return objectMapper.<List<DataSet>>readValue(body,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, DataSet.class)
            );
        } else {
            return objectMapper.readValue(body, DataSet.class);
        }
    }


}
