package net.n2oapp.framework.sandbox.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.exception.N2oException;
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
import net.n2oapp.framework.api.rest.*;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.api.util.ExternalFilesLoader;
import net.n2oapp.framework.boot.stomp.N2oWebSocketController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllIOPack;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oOperationProcessor;
import net.n2oapp.framework.engine.modules.stack.DataProcessingStack;
import net.n2oapp.framework.migrate.XmlIOVersionMigrator;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.scanner.ProjectFileScanner;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.templates.TemplateModel;
import net.n2oapp.framework.ui.controller.DataController;
import net.n2oapp.framework.ui.controller.N2oControllerFactory;
import net.n2oapp.framework.ui.controller.action.OperationController;
import net.n2oapp.framework.ui.controller.action.ValidationController;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.controller.export.format.CsvFileGenerator;
import net.n2oapp.framework.ui.controller.export.format.FileGeneratorFactory;
import net.n2oapp.framework.ui.controller.export.format.XlsxFileGenerator;
import net.n2oapp.framework.ui.controller.query.MergeValuesController;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.framework.ui.servlet.AppConfigJsonWriter;
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findFilesByUri;
import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@RestController
public class ViewController {
    @Value("${n2o.version:unknown}")
    private String n2oVersion;
    @Value("${n2o.config.path}")
    private String basePath;
    @Value("${spring.messages.basename:messages}")
    private String messageBundleBasename;
    private static final String DEFAULT_APP_ID = "default";
    private static final String DATA_REQUEST_PREFIX = "/n2o/data";

    private final DataProcessingStack dataProcessingStack;
    private final AlertMessageBuilder messageBuilder;
    private final QueryProcessor queryProcessor;
    private final N2oOperationProcessor operationProcessor;
    private final Environment environment;
    private final AlertMessagesConstructor messagesConstructor;
    private final RouteRegister projectRouteRegister;
    private final ContextEngine sandboxContext;
    private final SandboxPropertyResolver propertyResolver;
    private final FileStorage fileStorage;
    private final XsdSchemaParser schemaParser;
    private final ProjectTemplateHolder templatesHolder;
    private final ExternalFilesLoader externalFilesLoader;
    private final InvocationProcessor serviceProvider;
    private final MessageSourceAccessor messageSourceAccessor;
    private final N2oDynamicMetadataProviderFactory dynamicMetadataProviderFactory;
    private final ObjectMapper objectMapper;
    private final DomainProcessor domainProcessor;
    private final List<SandboxApplicationBuilderConfigurer> applicationBuilderConfigurers;
    private final XmlIOVersionMigrator migrator;
    private final N2oWebSocketController wsController;

    public ViewController(DataProcessingStack dataProcessingStack,
                          AlertMessageBuilder messageBuilder,
                          QueryProcessor queryProcessor,
                          N2oOperationProcessor operationProcessor,
                          Environment environment,
                          AlertMessagesConstructor messagesConstructor,
                          RouteRegister projectRouteRegister,
                          ContextEngine sandboxContext,
                          SandboxPropertyResolver propertyResolver,
                          FileStorage fileStorage,
                          XsdSchemaParser schemaParser,
                          ProjectTemplateHolder templatesHolder,
                          ExternalFilesLoader externalFilesLoader,
                          InvocationProcessor serviceProvider,
                          Optional<Map<String, DynamicMetadataProvider>> providers,
                          ObjectMapper objectMapper,
                          @Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                          N2oWebSocketController wsController,
                          List<SandboxApplicationBuilderConfigurer> applicationBuilderConfigurers) {
        this.dataProcessingStack = dataProcessingStack;
        this.messageBuilder = messageBuilder;
        this.queryProcessor = queryProcessor;
        this.operationProcessor = operationProcessor;
        this.environment = environment;
        this.messagesConstructor = messagesConstructor;
        this.projectRouteRegister = projectRouteRegister;
        this.sandboxContext = sandboxContext;
        this.propertyResolver = propertyResolver;
        this.fileStorage = fileStorage;
        this.schemaParser = schemaParser;
        this.templatesHolder = templatesHolder;
        this.externalFilesLoader = externalFilesLoader;
        this.serviceProvider = serviceProvider;
        this.dynamicMetadataProviderFactory = new N2oDynamicMetadataProviderFactory(providers.orElse(Collections.emptyMap()));
        this.objectMapper = objectMapper;
        this.domainProcessor = new DomainProcessor(objectMapper);
        this.messageSourceAccessor = messageSourceAccessor;
        this.wsController = wsController;
        this.applicationBuilderConfigurers = applicationBuilderConfigurers;

        N2oApplicationBuilder builder = new N2oApplicationBuilder(new N2oEnvironment());
        builder.packs(new N2oAllIOPack());
        this.migrator = new XmlIOVersionMigrator(builder);
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/n2o/version", "/n2o/version/"})
    public String getVersion() {
        return n2oVersion;
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/n2o/templates/{fileName}", "/n2o/templates/{fileName}/"})
    public String getTemplateFile(@PathVariable String fileName) {
        return getTemplate(fileName);
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/n2o/schemas", "/n2o/schemas/"})
    public ResponseEntity<Resource> loadSchema(@RequestParam(name = "name") String schemaNamespace) throws IOException {
        Resource schema = schemaParser.getSchema(schemaNamespace);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + schema.getFilename() + "\"")
                .body(schema);
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/config", "/view/{projectId}/n2o/config/"})
    public Map<String, Object> getConfig(@PathVariable(value = "projectId") String projectId) {
        Map<String, Object> addedValues = new HashMap<>();
        addedValues.put("project", projectId);

        N2oApplicationBuilder builder;
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            projectRouteRegister.clearAll();
            builder = getBuilder(projectId);
            addedValues.put("menu", getMenu(builder));

            AppConfigJsonWriter appConfigJsonWriter = new SandboxAppConfigJsonWriter(projectId, fileStorage);
            appConfigJsonWriter.setPropertyResolver(builder.getEnvironment().getSystemProperties());
            appConfigJsonWriter.setContextProcessor(builder.getEnvironment().getContextProcessor());
            appConfigJsonWriter.build();

            return appConfigJsonWriter.getValues(addedValues);
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/page/**", "/view/{projectId}/n2o/page", "/view/{projectId}/n2o/page/"})
    public Page getPage(@PathVariable(value = "projectId") String projectId,
                        HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            projectRouteRegister.clearAll();
            N2oApplicationBuilder builder = getBuilder(projectId);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/page");
            CompileContext<Page, ?> context = builder.route(path, Page.class, request.getParameterMap());

            N2oSubModelsProcessor n2oSubModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
            n2oSubModelsProcessor.setEnvironment(builder.getEnvironment());

            return builder.read().transform().validate().compile().transform().bind().get(context, context.getParams(path, request.getParameterMap()), n2oSubModelsProcessor);
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/count/**", "/view/{projectId}/n2o/count", "/view/{projectId}/n2o/count/"})
    public ResponseEntity<Integer> getCount(@PathVariable(value = "projectId") String projectId,
                        HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = getBuilder(projectId);

            String path = getPath(request, "/n2o/count");
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

            GetDataResponse response = dataController.getData(path, request.getParameterMap(),
                    new UserContext(sandboxContext));
            return ResponseEntity.status(response.getStatus()).body(response.getPaging().getCount());
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/export/**", "/view/{projectId}/n2o/export", "/view/{projectId}/n2o/export/"})
    public ResponseEntity<byte[]> export(@PathVariable(value = "projectId") String projectId, HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = getBuilder(projectId);
            getIndex(builder);
            getMenu(builder);

            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());
            FileGeneratorFactory fileGeneratorFactory = new FileGeneratorFactory(List.of(new CsvFileGenerator(), new XlsxFileGenerator()));
            ExportController exportController = new ExportController(builder.getEnvironment(), dataController, fileGeneratorFactory);

            String url = request.getParameter("url");
            String format = request.getParameter("format");
            String charset = request.getParameter("charset");

            String dataPrefix = DATA_REQUEST_PREFIX;
            String path = RouteUtil.parsePath(url.substring(url.indexOf(dataPrefix) + dataPrefix.length()));
            Map<String, String[]> params = RouteUtil.parseQueryParams(RouteUtil.parseQuery(url));
            if (params == null)
                throw new N2oException("Query-параметр запроса пустой");

            GetDataResponse dataResponse = exportController.getData(path, params, new UserContext(sandboxContext));
            Map<String, String> headers = exportController.getHeaders(path, params);
            ExportResponse exportResponse = exportController.export(dataResponse.getList(), format, charset, headers);

            return ResponseEntity.status(exportResponse.getStatus())
                    .contentLength(exportResponse.getContentLength())
                    .header(HttpHeaders.CONTENT_TYPE, exportResponse.getContentType())
                    .header(HttpHeaders.CONTENT_DISPOSITION, exportResponse.getContentDisposition())
                    .header(HttpHeaders.CONTENT_ENCODING, exportResponse.getCharacterEncoding())
                    .body(exportResponse.getFile());
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data", "/view/{projectId}/n2o/data/"})
    public ResponseEntity<GetDataResponse> getData(@PathVariable(value = "projectId") String projectId,
                                                   HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = getBuilder(projectId);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, DATA_REQUEST_PREFIX);
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());

            GetDataResponse response = dataController.getData(path, request.getParameterMap(),
                    new UserContext(sandboxContext));
            return ResponseEntity.status(response.getStatus()).body(response);
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data", "/view/{projectId}/n2o/data/"})
    public ResponseEntity<SetDataResponse> putData(@PathVariable(value = "projectId") String projectId,
                                                   @RequestBody Object body,
                                                   HttpServletRequest request) {
        return setData(projectId, body, request);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data", "/view/{projectId}/n2o/data/"})
    public ResponseEntity<SetDataResponse> deleteData(@PathVariable(value = "projectId") String projectId,
                                                      @RequestBody Object body,
                                                      HttpServletRequest request) {
        return setData(projectId, body, request);
    }

    @CrossOrigin(origins = "*")
    @PostMapping({"/view/{projectId}/n2o/data/**", "/view/{projectId}/n2o/data", "/view/{projectId}/n2o/data/"})
    public ResponseEntity<SetDataResponse> setData(@PathVariable(value = "projectId") String projectId,
                                                   @RequestBody Object body,
                                                   HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);

            N2oApplicationBuilder builder = getBuilder(projectId);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, DATA_REQUEST_PREFIX);
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()), builder.getEnvironment());
            dataController.setMessageBuilder(messageBuilder);
            SetDataResponse dataResponse = dataController.setData(path,
                    request.getParameterMap(),
                    getHeaders(request),
                    getBody(body),
                    new UserContext(sandboxContext));
            return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = {"/view/{projectId}/n2o/validation/**", "/view/{projectId}/n2o/validation", "/view/{projectId}/n2o/validation/"})
    public ResponseEntity<ValidationDataResponse> validateData(@PathVariable(value = "projectId") String projectId,
                                                               @RequestBody Object body,
                                                               HttpServletRequest request) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            N2oApplicationBuilder builder = getBuilder(projectId);
            getIndex(builder);
            getMenu(builder);
            String path = getPath(request, "/n2o/validation");
            DataController dataController = new DataController(createControllerFactory(builder.getEnvironment()),
                    builder.getEnvironment());
            dataController.setMessageBuilder(messageBuilder);
            ValidationDataResponse dataResponse = dataController.validateData(path, getBody(body));
            return ResponseEntity.status(dataResponse.getStatus()).body(dataResponse);
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping({"/n2o/migrate"})
    public String migrate(@RequestBody String oldXml) {
        return migrator.migrate(oldXml);
    }

    /**
     * Обработчик исключений N2O
     */
    @ExceptionHandler(N2oException.class)
    public ResponseEntity<N2oResponse> sendErrorMessage(N2oException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(initErrorDataResponse(e));
    }

    /**
     * Обработчик исключений
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<N2oResponse> sendErrorMessage(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(initErrorDataResponse(e));
    }

    private N2oResponse initErrorDataResponse(Exception e) {
        log.error(e.getMessage(), e);
        MetaSaga meta = new MetaSaga();
        meta.setAlert(new AlertSaga());
        meta.getAlert().setMessages(messagesConstructor.constructMessages(e));
        N2oResponse dataResponse = new N2oResponse();
        dataResponse.setMeta(meta);
        return dataResponse;
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
        String applicationId = builder.getEnvironment().getSystemProperties().getProperty("n2o.application.id");
        if (DEFAULT_APP_ID.equals(applicationId)) {
            Optional<SourceInfo> applicationInfo = builder.getEnvironment().getMetadataRegister().find(N2oApplication.class).stream().filter(a -> !a.getId().equals(DEFAULT_APP_ID)).findFirst();
            applicationId = applicationInfo.isPresent() ? applicationInfo.get().getId() : DEFAULT_APP_ID;
        }

        return builder.read().transform().validate().compile().transform().bind().get(new ApplicationContext(applicationId), new DataSet());
    }

    private DataSet getBody(Object body) {
        if (body instanceof Map)
            return new DataSet((Map<String, ?>) body);
        else {
            DataSet dataSet = new DataSet("$list", body);
            dataSet.put("$count", body != null ? ((List<?>) body).size() : 0);
            return dataSet;
        }
    }

    private N2oApplicationBuilder getBuilder(String projectId) {
        N2oEnvironment env = createEnvironment(projectId);
        N2oApplicationBuilder builder = new N2oApplicationBuilder(env);
        applicationBuilderConfigurers.forEach(configurer -> configurer.configure(builder));
        builder.scanners(new ProjectFileScanner(projectId, builder.getEnvironment().getSourceTypeRegister(), fileStorage, templatesHolder));
        return builder.scan();
    }

    private void getIndex(N2oApplicationBuilder builder) {
        PageContext index = new PageContext(propertyResolver.getProperty("n2o.homepage.id"), "/");
        builder.routes(new RouteInfo("/", index));
    }

    private String getPath(HttpServletRequest request, String prefix) {
        String path = request.getRequestURI().substring(request.getRequestURI().indexOf(prefix) + prefix.length());
        return RouteUtil.normalize(!path.isEmpty() ? path : "/");
    }

    /**
     * Ищет *.access.xml файлы в папке проекта, и
     * передает имя первого попавшегося файла
     *
     * @param projectId Идентификатор проекта
     * @return Имя файла (без .access.xml) или null,
     * если папка проекта не содержит файлов указанного формата
     */
    private String getAccessFilename(String projectId, TemplateModel templateModel) {
        String format = ".access.xml";
        if (templateModel == null) {
            List<FileModel> projectFiles = fileStorage.getProjectFiles(projectId);
            if (projectFiles != null) {
                return getFirstFilenameByFormat(format, projectFiles);
            }
        } else {
            List<FileModel> files = findResources(templateModel.getTemplateId());
            return getFirstFilenameByFormat(format, files);
        }
        return null;
    }

    private String getFirstFilenameByFormat(String format, List<FileModel> files) {
        Optional<String> first = files.stream()
                .map(FileModel::getFile)
                .filter(name -> name.endsWith(format))
                .findFirst();
        if (first.isPresent()) {
            String filename = first.get();
            return filename.substring(0, (filename.length() - format.length()));
        }
        return null;
    }

    private String getApplicationProperties(String projectId, TemplateModel templateModel) {
        String filename = "application.properties";
        if (templateModel == null) {
            return fileStorage.getFileContent(projectId, filename);
        } else {
            List<FileModel> files = findResources(templateModel.getTemplateId());
            Optional<FileModel> first = files.stream().filter(f -> f.getFile().equals(filename)).findFirst();
            return first.map(FileModel::getSource).orElse(null);
        }
    }

    private N2oEnvironment createEnvironment(String projectId) {
        N2oEnvironment env = new N2oEnvironment();
        String path = basePath + File.separator + projectId;

        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        Map<String, String> runtimeProperties = new HashMap<>();
        runtimeProperties.put("n2o.access.schema.id", getAccessFilename(projectId, templateModel));
        configurePropertyResolver(runtimeProperties, getApplicationProperties(projectId, templateModel));

        env.setSystemProperties(propertyResolver);
        env.setMessageSource(getMessageSourceAccessor(path));
        env.setContextProcessor(new ContextProcessor(sandboxContext));
        ReaderFactoryByMap readerFactory = new ReaderFactoryByMap(env);
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
        env.setExternalFilesLoader(externalFilesLoader);
        wsController.setEnvironment(env);
        wsController.setPipeline(N2oPipelineSupport.readPipeline(env));

        return env;
    }

    private void configurePropertyResolver(Map<String, String> runtimeProperties, String applicationPropertyFile) {
        propertyResolver.configure(environment, runtimeProperties, applicationPropertyFile);
    }

    private ControllerFactory createControllerFactory(MetadataEnvironment environment) {
        Map<String, Object> beans = new HashMap<>();
        N2oSubModelsProcessor subModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
        subModelsProcessor.setEnvironment(environment);
        beans.put("queryController", new QueryController(dataProcessingStack, queryProcessor,
                subModelsProcessor, messageBuilder, messagesConstructor));
        beans.put("operationController", new OperationController(dataProcessingStack,
                operationProcessor, messageBuilder, messagesConstructor));
        beans.put("validationController", new ValidationController(serviceProvider, domainProcessor));
        beans.put("mergeValuesController", new MergeValuesController(dataProcessingStack, queryProcessor, subModelsProcessor,
                messageBuilder));
        return new N2oControllerFactory(beans);
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
