package net.n2oapp.framework.autotest.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.api.config.ConfigBuilder;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.config.metadata.compile.context.HeaderContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.BulkActionController;
import net.n2oapp.framework.ui.controller.action.BulkActionMergeController;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.query.CopyValuesController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.framework.ui.controller.query.SimpleDefaultValuesController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер n2o запросов для автотестов
 */
@RestController
public class N2oController {

    private N2oApplicationBuilder builder;

    private DataProcessingStack dataProcessingStack;
    private ErrorMessageBuilder errorMessageBuilder;
    private QueryProcessor queryProcessor;
    private N2oOperationProcessor operationProcessor;
    private ConfigBuilder<AppConfig> configBuilder;
    private DomainProcessor domainProcessor;

    @Value("${n2o.config.path}")
    private String basePath;

    @Autowired
    public N2oController(DataProcessingStack dataProcessingStack, ErrorMessageBuilder errorMessageBuilder,
                         QueryProcessor queryProcessor, N2oOperationProcessor operationProcessor,
                         DomainProcessor domainProcessor) {
        this.queryProcessor = queryProcessor;
        this.dataProcessingStack = dataProcessingStack;
        this.errorMessageBuilder = errorMessageBuilder;
        this.operationProcessor = operationProcessor;
        this.domainProcessor = domainProcessor;
    }

    @GetMapping("/n2o/config")
    public AppConfig config() {
        List<SourceInfo> headers = builder.getEnvironment().getMetadataRegister().find(N2oHeader.class);
        Assert.isTrue(!headers.isEmpty(), "No header metadata found");
        configBuilder.menu(builder.read().transform().validate().compile().transform().bind().get(new HeaderContext(headers.get(0).getId()), new DataSet()));
        return configBuilder.get();
    }

    @GetMapping({"/n2o/page/**", "/n2o/page/", "/n2o/page"})
    public Page page(HttpServletRequest request) {
        String path = getPath(request, "/n2o/page");
        CompileContext<Page, ?> context = builder.route(path, Page.class, request.getParameterMap());
        N2oSubModelsProcessor n2oSubModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
        n2oSubModelsProcessor.setEnvironment(builder.getEnvironment());
        return builder.read().transform().validate().compile().transform().bind().get(context, context.getParams(path, request.getParameterMap()), n2oSubModelsProcessor);
    }

    @GetMapping({"/n2o/data/**", "/n2o/data/", "/n2o/data"})
    public ResponseEntity<GetDataResponse> getData(HttpServletRequest request) {
        String path = getPath(request, "/n2o/data");
        DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

        GetDataResponse response = dataController.getData(path, request.getParameterMap(), null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({"/n2o/data/**", "/n2o/data/", "/n2o/data"})
    public ResponseEntity<SetDataResponse> setData(@RequestBody Object body, HttpServletRequest request) {
        String path = getPath(request, "/n2o/data");
        DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

        SetDataResponse dataResponse = dataController.setData(path, request.getParameterMap(), getHeaders(request), getBody(body), null);
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    private DataSet getBody(Object body) {
        if (body instanceof Map)
            return new DataSet((Map<? extends String, ?>) body);
        else {
            DataSet dataSet = new DataSet("$list", body);
            dataSet.put("$count", body != null ? ((List)body).size() : 0);
            return dataSet;
        }
    }

    public void setUp(N2oApplicationBuilder builder) {
        this.builder = builder;
        configBuilder = new N2oConfigBuilder<>(new AppConfig(), new ObjectMapper(),
                builder.getEnvironment().getSystemProperties(),
                builder.getEnvironment().getContextProcessor());
    }

    public void addConfigProperty(String key, Object value) {
        this.configBuilder.add(key, value);
    }

    private ControllerFactory createControllerFactory(MetadataEnvironment environment) {
        N2oSubModelsProcessor subModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
        subModelsProcessor.setEnvironment(environment);
        Map<String, Object> beans = new HashMap<>();
        beans.put("queryController", new QueryController(dataProcessingStack, queryProcessor,
                subModelsProcessor, errorMessageBuilder, environment));
        beans.put("operationController", new OperationController(dataProcessingStack,
                operationProcessor, errorMessageBuilder, environment));
        beans.put("copyValuesController", new CopyValuesController(dataProcessingStack, queryProcessor, subModelsProcessor,
                errorMessageBuilder, environment));
        beans.put("bulkActionController", new BulkActionController(dataProcessingStack,
                operationProcessor, environment));
        beans.put("simpleDefaultValuesController", new SimpleDefaultValuesController(dataProcessingStack, queryProcessor,
                subModelsProcessor, errorMessageBuilder, environment));
        beans.put("bulkActionMergeController", new BulkActionMergeController(dataProcessingStack,
                operationProcessor, environment));
        return new N2oControllerFactory(beans);
    }

    private String getPath(HttpServletRequest request, String prefix) {
        String path = request.getRequestURI().substring(request.getRequestURI().indexOf(prefix) + prefix.length());
        return RouteUtil.normalize(!path.isEmpty() ? path : "/");
    }

    private Map<String, String[]> getHeaders(HttpServletRequest req) {
        Map<String, String[]> headers = new HashMap<>();
        Enumeration<String> iter = req.getHeaderNames();
        while (iter.hasMoreElements()) {
            String name = iter.nextElement();
            headers.put(name, new String[]{req.getHeader(name)});
        }
        return headers;
    }

}
