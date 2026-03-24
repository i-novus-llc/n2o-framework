package net.n2oapp.framework.sandbox.cases.websocket;

import jakarta.servlet.http.HttpSession;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.boot.stomp.N2oWebSocketController;
import net.n2oapp.framework.boot.stomp.WebSocketController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.sandbox.engine.thread_local.ThreadLocalProjectId;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.scanner.ProjectFileScanner;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.templates.TemplateModel;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import static net.n2oapp.framework.sandbox.utils.FileUtil.findResources;

@RestController
@RequestMapping("/websocket")
public class WebSocketMessageController {

    private static final Pattern PROJECT_ID_PATTERN = Pattern.compile("^\\w+$");
    private static final String PAGE_ROUTE = "pageRoute";

    private final Random random = new Random();
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketController wsController;
    private final RouteRegister projectRouteRegister;
    private final ContextEngine sandboxContext;
    private final SandboxPropertyResolver propertyResolver;
    private final FileStorage fileStorage;
    private final ProjectTemplateHolder templatesHolder;
    private final List<SandboxApplicationBuilderConfigurer> applicationBuilderConfigurers;
    private final Environment environment;

    public WebSocketMessageController(SimpMessagingTemplate messagingTemplate,
                                      WebSocketController wsController,
                                      RouteRegister projectRouteRegister,
                                      ContextEngine sandboxContext,
                                      SandboxPropertyResolver propertyResolver,
                                      FileStorage fileStorage,
                                      ProjectTemplateHolder templatesHolder,
                                      List<SandboxApplicationBuilderConfigurer> applicationBuilderConfigurers,
                                      Environment environment) {
        this.messagingTemplate = messagingTemplate;
        this.wsController = wsController;
        this.projectRouteRegister = projectRouteRegister;
        this.sandboxContext = sandboxContext;
        this.propertyResolver = propertyResolver;
        this.fileStorage = fileStorage;
        this.templatesHolder = templatesHolder;
        this.applicationBuilderConfigurers = applicationBuilderConfigurers;
        this.environment = environment;
    }

    @GetMapping("/{projectId}/dest/{destination}/count")
    public String sendCount(@PathVariable String projectId, @PathVariable String destination, HttpSession session) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            projectRouteRegister.clearAll();
            getBuilder(projectId);

            BadgeMessage message = new BadgeMessage();
            Integer count = random.nextInt(14) + 1;
            message.setCount(count);
            messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
            return "Сгенерированный счетчик баджа: " + count;
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @GetMapping("/{projectId}/dest/{destination}/color")
    public String sendColor(@PathVariable String projectId, @PathVariable String destination, HttpSession session) {
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            projectRouteRegister.clearAll();
            getBuilder(projectId);

            BadgeMessage message = new BadgeMessage();
            int pick = random.nextInt(BadgeColorEnum.values().length);
            String color = BadgeColorEnum.values()[pick].toString().toLowerCase();
            message.setColor(color);
            messagingTemplate.convertAndSendToUser(session.getId(), destination, message);
            return "Сгенерированный цвет баджа: " + "\"" + color + "\"";
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    @GetMapping("/{projectId}/dest/{destination}/alert")
    public Map<String, String> sendAlert(@PathVariable String projectId, @PathVariable String destination, @RequestParam Map<String, String> message, HttpSession session) {
        return sendNotification(projectId, destination, message, session);
    }

    @GetMapping("/{projectId}/dest/{destination}/setvalue")
    public Map<String, String> setValue(@PathVariable String projectId, @PathVariable String destination, @RequestParam Map<String, String> message, HttpSession session) {
        return sendNotification(projectId, destination, message, session);
    }

    private @NonNull Map<String, String> sendNotification(String projectId, String destination, Map<String, String> message, HttpSession session) {
        String pageRoute = null;
        if (message.containsKey(PAGE_ROUTE)) {
            pageRoute = message.get(PAGE_ROUTE);
            message.remove(PAGE_ROUTE);
        }
        try {
            ThreadLocalProjectId.setProjectId(projectId);
            projectRouteRegister.clearAll();
            N2oApplicationBuilder builder = getBuilder(projectId);
            getIndex(builder);
            getApplication(builder);

            wsController.convertAndSendToUser(session.getId(), destination, message, pageRoute);
            return message;
        } finally {
            sandboxContext.refresh();
            ThreadLocalProjectId.clear();
        }
    }

    private N2oApplicationBuilder getBuilder(String projectId) {
        validateProjectId(projectId);
        N2oEnvironment env = createEnvironment(projectId);
        N2oApplicationBuilder builder = new N2oApplicationBuilder(env);
        applicationBuilderConfigurers.forEach(configurer -> configurer.configure(builder));
        builder.scanners(new ProjectFileScanner(projectId, builder.getEnvironment().getSourceTypeRegister(), fileStorage, templatesHolder));
        N2oApplicationBuilder result = builder.scan();
        if (wsController instanceof N2oWebSocketController n2oWsController) {
            n2oWsController.setEnvironment(builder.getEnvironment());
            n2oWsController.setPipeline(N2oPipelineSupport.readPipeline(builder.getEnvironment()));
        }
        return result;
    }

    private void getIndex(N2oApplicationBuilder builder) {
        PageContext index = new PageContext(propertyResolver.getProperty("n2o.homepage.id"), "/");
        builder.routes(new RouteInfo("/", index));
    }

    private Application getApplication(N2oApplicationBuilder builder) {
        String applicationId = builder.getEnvironment().getSystemProperties().getProperty("n2o.application.id");
        if ("default".equals(applicationId)) {
            Optional<SourceInfo> applicationInfo = builder.getEnvironment().getMetadataRegister()
                    .find(N2oApplication.class).stream()
                    .filter(a -> !a.getId().equals("default"))
                    .findFirst();
            applicationId = applicationInfo.isPresent() ? applicationInfo.get().getId() : "default";
        }
        return builder.read().transform().validate().compile().transform().bind()
                .get(new ApplicationContext(applicationId), new DataSet());
    }

    private N2oEnvironment createEnvironment(String projectId) {
        N2oEnvironment env = new N2oEnvironment();

        TemplateModel templateModel = templatesHolder.getTemplateModel(projectId);
        propertyResolver.configure(environment, null, getApplicationProperties(projectId, templateModel));

        env.setSystemProperties(propertyResolver);
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
        env.setRouteRegister(projectRouteRegister);

        return env;
    }

    private void validateProjectId(String projectId) {
        if (projectId == null || !PROJECT_ID_PATTERN.matcher(projectId).matches())
            throw new IllegalArgumentException("Некорректный идентификатор проекта");
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
}
