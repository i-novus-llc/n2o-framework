package net.n2oapp.framework.autotest.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.config.AppConfig;
import net.n2oapp.framework.api.config.ConfigBuilder;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.N2oResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.N2oConfigBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.ui.controller.export.format.CsvFileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.query.MergeValuesController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.framework.ui.controller.query.SimpleDefaultValuesController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Контроллер n2o запросов для автотестов
 */
@RestController
public class N2oController {

    private N2oApplicationBuilder builder;

    private DataProcessingStack dataProcessingStack;
    private AlertMessageBuilder messageBuilder;
    private AlertMessagesConstructor messagesConstructor;
    private QueryProcessor queryProcessor;
    private N2oOperationProcessor operationProcessor;
    private ConfigBuilder<AppConfig> configBuilder;
    private DomainProcessor domainProcessor;
    private static final String DEFAULT_APP_ID = "default";

    @Value("${n2o.config.path}")
    private String basePath;

    @Value("${n2o.application.id}")
    private String defaultApplicationId;

    @Autowired
    public N2oController(DataProcessingStack dataProcessingStack, AlertMessageBuilder messageBuilder,
                         QueryProcessor queryProcessor, N2oOperationProcessor operationProcessor,
                         DomainProcessor domainProcessor, AlertMessagesConstructor messagesConstructor) {
        this.queryProcessor = queryProcessor;
        this.dataProcessingStack = dataProcessingStack;
        this.messageBuilder = messageBuilder;
        this.operationProcessor = operationProcessor;
        this.domainProcessor = domainProcessor;
        this.messagesConstructor = messagesConstructor;
    }

    @GetMapping("/n2o/config")
    public AppConfig config() {
        List<SourceInfo> apps = builder.getEnvironment().getMetadataRegister().find(N2oApplication.class);
        Assert.isTrue(!apps.isEmpty(), "Not found application.xml file");

        String applicationId = builder.getEnvironment().getSystemProperties().getProperty("n2o.application.id");
        if (applicationId.equals(DEFAULT_APP_ID)) {
            Optional<SourceInfo> applicationInfo = builder.getEnvironment().getMetadataRegister().find(N2oApplication.class).stream().filter(a -> !a.getId().equals(DEFAULT_APP_ID)).findFirst();
            applicationId = applicationInfo.isPresent() ? applicationInfo.get().getId() : DEFAULT_APP_ID;
        }
        configBuilder.menu(builder.read().transform().validate().compile().transform().bind().get(new ApplicationContext(applicationId), new DataSet()));

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
        dataController.setMessageBuilder(messageBuilder);
        GetDataResponse response = dataController.getData(path, request.getParameterMap(), null);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping({"/n2o/data/**", "/n2o/data/", "/n2o/data"})
    public ResponseEntity<SetDataResponse> setData(@RequestBody Object body, HttpServletRequest request) {
        String path = getPath(request, "/n2o/data");
        DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());
        dataController.setMessageBuilder(messageBuilder);
        SetDataResponse dataResponse = dataController.setData(path, request.getParameterMap(), getHeaders(request), getBody(body), null);
        return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
    }

    @GetMapping({"/n2o/export/**", "/n2o/export/", "/n2o/export"})
    public ResponseEntity<byte[]> export(HttpServletRequest request) {
        FileGeneratorFactory fileGeneratorFactory = new FileGeneratorFactory(List.of(new CsvFileGenerator()));
        DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());
        ExportController exportController = new ExportController(dataController, fileGeneratorFactory);

        String url = request.getParameter("url");
        String format = request.getParameter("format");
        String charset = request.getParameter("charset");

        String dataPrefix = "/n2o/data";
        String path = RouteUtil.parsePath(url.substring(url.indexOf(dataPrefix) + dataPrefix.length()));

        GetDataResponse dataResponse = exportController.getData(
                path,
                RouteUtil.parseQueryParams(RouteUtil.parseQuery(url)),
                null);
        ExportResponse exportResponse = exportController.export(dataResponse.getList(), format, charset);

        return ResponseEntity.status(exportResponse.getStatus())
                .contentLength(exportResponse.getContentLength())
                .header(HttpHeaders.CONTENT_TYPE, exportResponse.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, exportResponse.getContentDisposition())
                .header(HttpHeaders.CONTENT_ENCODING, exportResponse.getCharacterEncoding())
                .body(exportResponse.getFile());
    }

    @ExceptionHandler(N2oException.class)
    public ResponseEntity<N2oResponse> sendErrorMessage(N2oException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new N2oResponse());
    }

    private DataSet getBody(Object body) {
        if (body instanceof Map)
            return new DataSet((Map<? extends String, ?>) body);
        else {
            DataSet dataSet = new DataSet("$list", body);
            dataSet.put("$count", body != null ? ((List) body).size() : 0);
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
                subModelsProcessor, messageBuilder, environment, messagesConstructor));
        beans.put("operationController", new OperationController(dataProcessingStack,
                operationProcessor, messageBuilder, environment, messagesConstructor));
        beans.put("mergeValuesController", new MergeValuesController(dataProcessingStack, queryProcessor, subModelsProcessor,
                messageBuilder, environment));
        beans.put("simpleDefaultValuesController", new SimpleDefaultValuesController(dataProcessingStack, queryProcessor,
                subModelsProcessor, messageBuilder, environment));
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
