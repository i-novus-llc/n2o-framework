package net.n2oapp.framework.sandbox.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.N2oResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.query.CopyValuesController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.framework.ui.controller.query.SimpleDefaultValuesController;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static net.n2oapp.framework.sandbox.utils.ProjectUtil.findFilesByUri;

@RestController
public class ViewController {
    private Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Value("${n2o.version:unknown}")
    private String n2oVersion;

    @Autowired
    private DataProcessingStack dataProcessingStack;
    @Autowired
    private AlertMessageBuilder messageBuilder;
    @Autowired
    private QueryProcessor queryProcessor;
    @Autowired
    private N2oOperationProcessor operationProcessor;
    @Autowired
    private ContextEngine sandboxContext;
    @Autowired
    private SandboxRestClient restClient;
    @Autowired
    private XsdSchemaParser schemaParser;
    @Autowired
    private SandboxApplicationBuilderConfigurer builderConfigurer;

    private ObjectMapper objectMapper;
    private DomainProcessor domainProcessor;

    public ViewController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.domainProcessor = new DomainProcessor(objectMapper);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/n2o/version")
    public String getVersion() {
        return n2oVersion;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/n2o/templates/{fileName}")
    public String getTemplateFile(@PathVariable String fileName) {
        return getTemplate(fileName);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/n2o/schemas")
    public ResponseEntity<Resource> loadSchema(@RequestParam(name = "name") String schemaNamespace) throws IOException {
        Resource schema = schemaParser.getSchema(schemaNamespace);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + schema.getFilename() + "\"")
                .body(schema);
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/config"})
    public Map<String, Object> getConfig(@PathVariable(value = "projectId") String projectId, HttpSession session) {
        Map<String, Object> addedValues = new HashMap<>();
        addedValues.put("project", projectId);

        N2oApplicationBuilder builder;
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            builder = builderConfigurer.getBuilder(projectId, null);
            addedValues.put("menu", getMenu(builder));
            addedValues.put("user", getUserInfo());

            AppConfigJsonWriter appConfigJsonWriter = new SandboxAppConfigJsonWriter(projectId, restClient, session);
            appConfigJsonWriter.setPropertyResolver(builder.getEnvironment().getSystemProperties());
            appConfigJsonWriter.setContextProcessor(builder.getEnvironment().getContextProcessor());
            appConfigJsonWriter.build();

            return appConfigJsonWriter.getValues(addedValues);
        } finally {
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/page/**", "/view/{projectId}/n2o/page/", "/view/{projectId}/n2o/page"})
    public Page getPage(@PathVariable(value = "projectId") String projectId,
                        HttpServletRequest request, HttpSession session) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = builderConfigurer.getBuilder(projectId, session);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/page");
            CompileContext<Page, ?> context = builder.route(path, Page.class, request.getParameterMap());

            N2oSubModelsProcessor n2oSubModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
            n2oSubModelsProcessor.setEnvironment(builder.getEnvironment());

            return builder.read().transform().validate().compile().transform().bind().get(context, context.getParams(path, request.getParameterMap()), n2oSubModelsProcessor);
        } finally {
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<GetDataResponse> getData(@PathVariable(value = "projectId") String projectId,
                                                   HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = builderConfigurer.getBuilder(projectId, null);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/data");
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

            GetDataResponse response = dataController.getData(path, request.getParameterMap(),
                    getUserContext());
            return ResponseEntity.status(response.getStatus()).body(response);
        } finally {
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<SetDataResponse> putData(@PathVariable(value = "projectId") String projectId,
                                                   @RequestBody Object body,
                                                   HttpServletRequest request) {
        return setData(projectId, body, request);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<SetDataResponse> deleteData(@PathVariable(value = "projectId") String projectId,
                                                      @RequestBody Object body,
                                                      HttpServletRequest request) {
        return setData(projectId, body, request);
    }

    @CrossOrigin(origins = "*")
    @PostMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<SetDataResponse> setData(@PathVariable(value = "projectId") String projectId,
                                                   @RequestBody Object body,
                                                   HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);

            N2oApplicationBuilder builder = builderConfigurer.getBuilder(projectId, null);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/data");
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());
            SetDataResponse dataResponse = dataController.setData(path,
                    request.getParameterMap(),
                    getHeaders(request),
                    getBody(body),
                    getUserContext());
            return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
        } finally {
            ThreadLocalProjectId.clear();
        }
    }

    /**
     * Обработчик исключений
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<N2oResponse> sendErrorMessage(Exception e) {
        logger.error(e.getMessage(), e);
        MetaSaga meta = new MetaSaga();
        meta.setAlert(new AlertSaga());
        meta.getAlert().setMessages(Collections.singletonList(messageBuilder.build(e)));
        N2oResponse dataResponse = new N2oResponse();
        dataResponse.setMeta(meta);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dataResponse);
    }

    private String getTemplate(String fileName) {
        String type = getFileType(fileName);
        if (type != null) {
            FileModel fileModel = findFilesByUri("/templates").stream()
                    .filter(f -> type.equals(f.getFile())).findFirst().orElse(null);
            if (fileModel != null)
                return fileModel.getSource();
        }
        return "";
    }

    private String getFileType(String fileName) {
        String[] spl = fileName.toLowerCase().split("\\.");
        if (spl.length > 2 && "xml".equals(spl[spl.length - 1])) {
            return spl[spl.length - 2] + "." + spl[spl.length - 1];
        }
        return null;
    }

    private Map<String, Object> getMenu(N2oApplicationBuilder builder) {
        return objectMapper.convertValue(getApplication(builder), Map.class);
    }

    private Application getApplication(N2oApplicationBuilder builder) {
        String applicationId = builder.getEnvironment().getSystemProperties().getProperty("n2o.application.id", String.class, null);
        if ("default".equals(applicationId)) {
            List<SourceInfo> applications = builder.getEnvironment().getMetadataRegister().find(N2oApplication.class);
            for (SourceInfo si : applications) {
                applicationId = si.getId();
                if (si.getScannerClass().isAssignableFrom(XmlInfoScanner.class)) break;
            }
        }
        return builder.read().transform().validate().compile().transform().bind().get(new ApplicationContext(applicationId), new DataSet());
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

    private void getIndex(N2oApplicationBuilder builder) {
        PageContext index = new PageContext("index", "/");
        builder.routes(new RouteInfo("/", index));
        builder.scan().read().transform().validate().compile().transform().get(index);
    }

    private String getPath(HttpServletRequest request, String prefix) {
        String path = request.getRequestURI().substring(request.getRequestURI().indexOf(prefix) + prefix.length());
        return RouteUtil.normalize(!path.isEmpty() ? path : "/");
    }

    private ControllerFactory createControllerFactory(MetadataEnvironment environment) {
        Map<String, Object> beans = new HashMap<>();
        N2oSubModelsProcessor subModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
        subModelsProcessor.setEnvironment(environment);
        beans.put("queryController", new QueryController(dataProcessingStack, queryProcessor,
                subModelsProcessor, messageBuilder, environment));
        //N2oOperationProcessor operationProcessor = new N2oOperationProcessor(invocationProcessor, operationExceptionHandler);
        beans.put("operationController", new OperationController(dataProcessingStack,
                operationProcessor, messageBuilder, environment));
        beans.put("copyValuesController", new CopyValuesController(dataProcessingStack, queryProcessor, subModelsProcessor,
                messageBuilder, environment));
        beans.put("simpleDefaultValuesController", new SimpleDefaultValuesController(dataProcessingStack, queryProcessor,
                subModelsProcessor, messageBuilder, environment));
        ControllerFactory factory = new N2oControllerFactory(beans);
        return factory;
    }

    private UserContext getUserContext() {
        sandboxContext.refresh();
        return new UserContext(sandboxContext);
    }

    private Map<String, Object> getUserInfo() {
        UserContext userContext = getUserContext();
        Map<String, Object> user = new HashMap<>();
        user.put("username", userContext.get("username"));
        user.put("roles", userContext.get("roles"));
        user.put("permissions", userContext.get("permissions"));
        return user;
    }

    private Map<String, String[]> getHeaders(HttpServletRequest req) {
        Map<String, String[]> result = new HashMap<>();
        Enumeration<String> iter = req.getHeaderNames();
        while (iter.hasMoreElements()) {
            String name = iter.nextElement();
            result.put(name, new String[]{req.getHeader(name)});
        }
        return result;
    }
}
