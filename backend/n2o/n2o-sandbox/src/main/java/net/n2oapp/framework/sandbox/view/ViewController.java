package net.n2oapp.framework.sandbox.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.metadata.SecurityPageBinder;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.saga.AlertSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.api.rest.ControllerFactory;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.N2oResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.query.MongodbEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.register.scanner.DefaultXmlInfoScanner;
import net.n2oapp.framework.config.register.scanner.JavaInfoScanner;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;
import net.n2oapp.framework.sandbox.resource.TemplatesHolder;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.resource.model.CategoryModel;
import net.n2oapp.framework.sandbox.scanner.ProjectFileScanner;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@RestController
public class ViewController {
    private Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Value("${n2o.version:unknown}")
    private String n2oVersion;
    @Value("${n2o.config.path}")
    private String basePath;
    @Value("${spring.messages.basename}")
    private String messageBundleBasename;

    @Autowired
    private DataProcessingStack dataProcessingStack;
    @Autowired
    private AlertMessageBuilder messageBuilder;
    @Autowired
    private QueryProcessor queryProcessor;
    @Autowired
    private N2oOperationProcessor operationProcessor;
    @Autowired
    private Environment environment;
    @Autowired
    private SecurityProvider securityProvider;
    @Autowired
    private RouteRegister projectRouteRegister;
    @Autowired
    private ContextEngine sandboxContext;
    @Autowired
    private SandboxPropertyResolver propertyResolver;
    @Autowired
    private SandboxRestClient restClient;
    @Autowired
    private TemplatesHolder templatesHolder;
    @Autowired
    private XsdSchemaParser schemaParser;

    private MessageSourceAccessor messageSourceAccessor;
    private N2oDynamicMetadataProviderFactory dynamicMetadataProviderFactory;
    private ObjectMapper objectMapper;
    private DomainProcessor domainProcessor;

    public ViewController(Optional<Map<String, DynamicMetadataProvider>> providers,
                          @Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
        this.dynamicMetadataProviderFactory = new N2oDynamicMetadataProviderFactory(providers.orElse(Collections.emptyMap()));
        this.objectMapper = new ObjectMapper();
        this.domainProcessor = new DomainProcessor(objectMapper);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/version")
    public String getVersion() {
        return n2oVersion;
    }

    @GetMapping("/templates")
    public List<CategoryModel> getProjectTemplates() {
        return templatesHolder.getProjectTemplates();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/schemas")
    public ResponseEntity<Resource> loadSchema(@RequestParam(name = "name") String schemaNamespace) throws IOException {
        Resource schema = schemaParser.getSchema(schemaNamespace);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + schema.getFilename() + "\"")
                .body(schema);
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/config"})
    public Map<String, Object> getConfig(@PathVariable(value = "projectId") String projectId) {
        Map<String, Object> addedValues = new HashMap<>();
        addedValues.put("project", projectId);

        N2oApplicationBuilder builder;
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            builder = getBuilder(projectId, null);
            addedValues.put("menu", getMenu(builder));
            addedValues.put("user", getUserInfo(projectId));

            AppConfigJsonWriter appConfigJsonWriter = new AppConfigJsonWriter();
            String path = basePath + "/" + projectId + "/config.json";
            if (new File(path).isFile()) {
                appConfigJsonWriter.setOverridePath("file:" + path);
            }
            appConfigJsonWriter.setPropertyResolver(builder.getEnvironment().getSystemProperties());
            appConfigJsonWriter.setContextProcessor(builder.getEnvironment().getContextProcessor());
            appConfigJsonWriter.loadValues();

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
            N2oApplicationBuilder builder = getBuilder(projectId, session);
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
                                                   HttpServletRequest request, HttpSession session) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = getBuilder(projectId, null);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/data");
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

            GetDataResponse response = dataController.getData(path, request.getParameterMap(),
                    getUserContext(projectId));
            return ResponseEntity.status(response.getStatus()).body(response);
        } finally {
            ThreadLocalProjectId.clear();
        }
    }

    @PutMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<SetDataResponse> putData(@PathVariable(value = "projectId") String projectId,
                                                   @RequestBody Object body,
                                                   HttpServletRequest request, HttpSession session) {
        return setData(projectId, body, request, session);
    }

    @DeleteMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<SetDataResponse> deleteData(@PathVariable(value = "projectId") String projectId,
                                                      @RequestBody Object body,
                                                      HttpServletRequest request, HttpSession session) {
        return setData(projectId, body, request, session);
    }

    @PostMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data/", "/view/{projectId}/n2o/data"})
    public ResponseEntity<SetDataResponse> setData(@PathVariable(value = "projectId") String projectId,
                                                   @RequestBody Object body,
                                                   HttpServletRequest request, HttpSession session) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);

            N2oApplicationBuilder builder = getBuilder(projectId, null);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/data");
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());
            SetDataResponse dataResponse = dataController.setData(path,
                    request.getParameterMap(),
                    getHeaders(request),
                    getBody(body),
                    getUserContext(projectId));
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

    private N2oApplicationBuilder getBuilder(@PathVariable("projectId") String projectId, HttpSession session) {
        N2oEnvironment env = createEnvironment(projectId, session);

        N2oApplicationBuilder builder = new N2oApplicationBuilder(env);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack(), new N2oAllIOPack(), new N2oApplicationPack(),
                new N2oLoadersPack(), new N2oOperationsPack(), new N2oSourceTypesPack(),
                new AccessSchemaPack(), new N2oAllValidatorsPack());
        builder.scanners(new DefaultXmlInfoScanner(),
                new XmlInfoScanner("classpath:META-INF/conf/*.xml"),
                new ProjectFileScanner(projectId, session, builder.getEnvironment().getSourceTypeRegister(), restClient),
                new JavaInfoScanner((N2oDynamicMetadataProviderFactory) env.getDynamicMetadataProviderFactory()));
        builder.binders(new SecurityPageBinder(securityProvider));
        builder.loaders(new ProjectFileLoader(builder.getEnvironment().getNamespaceReaderFactory()));

        builder.transformers(new TestEngineQueryTransformer(), new MongodbEngineQueryTransformer());
        return builder.scan();
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

    /**
     * Ищет *.access.xml файлы в папке проекта, и
     * передает имя первого попавшегося файла
     *
     * @param path Путь к файлу
     * @return Имя файла (без .access.xml) или null,
     * если папка проекта не содержит файлов указанного формата
     */
    private String getAccessFilename(String path) {
        String format = ".access.xml";
        File[] files = new File(path).listFiles((d, name) -> name.endsWith(format));

        if (files != null && files.length != 0) {
            String filename = files[0].getName();
            return filename.substring(0, (filename.length() - format.length()));
        }
        return null;
    }

    private N2oEnvironment createEnvironment(String projectId, HttpSession session) {
        N2oEnvironment env = new N2oEnvironment();
        String path = basePath + "/" + projectId;

        Map<String, String> runtimeProperties = new HashMap<>();
        runtimeProperties.put("n2o.access.schema.id", getAccessFilename(path));
        configurePropertyResolver(runtimeProperties, projectId, session);

        env.setSystemProperties(propertyResolver);
        env.setMessageSource(getMessageSourceAccessor(path));
        env.setContextProcessor(new ContextProcessor(sandboxContext));
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap();
        env.setNamespaceReaderFactory(readerFactory);
        PersisterFactoryByMap persisterFactory = new PersisterFactoryByMap();
        env.setNamespacePersisterFactory(persisterFactory);
        IOProcessorImpl persistProcessor = new IOProcessorImpl(persisterFactory);
        persistProcessor.setSystemProperties(env.getSystemProperties());
        env.setReadPipelineFunction(p -> p.read().transform().validate());
        env.setReadCompilePipelineFunction(p -> p.read().transform().validate().compile().transform());
        env.setReadCompileBindTerminalPipelineFunction(p -> p.read().transform().validate().compile().transform().bind());
        env.setDynamicMetadataProviderFactory(dynamicMetadataProviderFactory);
        env.setRouteRegister(projectRouteRegister);

        return env;
    }

    private void configurePropertyResolver(Map<String, String> runtimeProperties, String projectId, HttpSession session) {
        propertyResolver.configure(environment, runtimeProperties, restClient.getFile(projectId, "application.properties", session));
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

    private MessageSourceAccessor getMessageSourceAccessor(String projectPath) {
        File projectFolder = new File(projectPath);
        ClassLoader loader;
        try {
            File[] messageFiles = projectFolder.listFiles(f -> f.getName().contains("messages") && f.getName().endsWith("properties"));
            if (messageFiles == null || messageFiles.length < 1) {
                return messageSourceAccessor;
            }
            loader = new URLClassLoader(new URL[]{projectFolder.toURI().toURL()});
        } catch (MalformedURLException e) {
            return messageSourceAccessor;
        }
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBundleClassLoader(loader);
        messageSource.setBasenames(messageBundleBasename.split(","));
        messageSource.setDefaultEncoding("UTF-8");
        return new MessageSourceAccessor(messageSource);
    }

    private UserContext getUserContext(String projectId) {
        return new UserContext(sandboxContext);
    }

    private Map<String, Object> getUserInfo(String projectId) {
        UserContext userContext = getUserContext(projectId);
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

    private FileModel findPropertyFile(ProjectModel project) {
        return project.getFiles().stream()
                .filter(f -> "application".equals(FileNameUtil.getNameFromFile(f.getFile()))).findFirst().orElse(null);
    }
}
