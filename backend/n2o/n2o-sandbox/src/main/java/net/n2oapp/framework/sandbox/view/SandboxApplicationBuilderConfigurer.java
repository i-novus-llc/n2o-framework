package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.metadata.SecurityPageBinder;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.metadata.compile.query.MongodbEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.scanner.DefaultXmlInfoScanner;
import net.n2oapp.framework.config.register.scanner.JavaInfoScanner;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.client.model.FileModel;
import net.n2oapp.framework.sandbox.client.model.ProjectModel;
import net.n2oapp.framework.sandbox.loader.ProjectFileLoader;
import net.n2oapp.framework.sandbox.scanner.ProjectFileScanner;
import net.n2oapp.framework.sandbox.utils.FileNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SandboxApplicationBuilderConfigurer {
    @Value("${n2o.config.path}")
    protected String basePath;
    @Value("${spring.messages.basename:messages}")
    protected String messageBundleBasename;

    @Autowired
    protected Environment environment;
    @Autowired
    protected SecurityProvider securityProvider;
    @Autowired
    protected RouteRegister projectRouteRegister;
    @Autowired
    private ContextEngine sandboxContext;
    @Autowired
    private SandboxRestClient restClient;
    @Autowired
    private SandboxPropertyResolver propertyResolver;

    protected MessageSourceAccessor messageSourceAccessor;
    protected N2oDynamicMetadataProviderFactory dynamicMetadataProviderFactory;

    public SandboxApplicationBuilderConfigurer(Optional<Map<String, DynamicMetadataProvider>> providers, @Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor) {
        this.dynamicMetadataProviderFactory = new N2oDynamicMetadataProviderFactory(providers.orElse(Collections.emptyMap()));
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public N2oApplicationBuilder getBuilder(String projectId, HttpSession session) {
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

    protected N2oEnvironment createEnvironment(String projectId, HttpSession session) {
        N2oEnvironment env = new N2oEnvironment();
        String path = basePath + "/" + projectId;

        Map<String, String> runtimeProperties = new HashMap<>();
        runtimeProperties.put("n2o.access.schema.id", getAccessFilename(projectId, session));
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

    protected void configurePropertyResolver(Map<String, String> runtimeProperties, String projectId, HttpSession session) {
        propertyResolver.configure(environment, runtimeProperties, restClient.getFile(projectId, "application.properties", session));
    }

    protected MessageSourceAccessor getMessageSourceAccessor(String projectPath) {
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

    protected FileModel findPropertyFile(ProjectModel project) {
        return project.getFiles().stream()
                .filter(f -> "application".equals(FileNameUtil.getNameFromFile(f.getFile()))).findFirst().orElse(null);
    }

    /**
     * Ищет *.access.xml файлы в папке проекта, и
     * передает имя первого попавшегося файла
     *
     * @param projectId Идентификатор проекта
     * @param session   Сессия проекта
     * @return Имя файла (без .access.xml) или null,
     * если папка проекта не содержит файлов указанного формата
     */
    protected String getAccessFilename(String projectId, HttpSession session) {
        String format = ".access.xml";
        ProjectModel project = restClient.getProject(projectId, session);
        if (project != null && project.getFiles() != null) {
            Optional<String> first = project.getFiles().stream()
                    .map(FileModel::getFile)
                    .filter(name -> name.endsWith(format))
                    .findFirst();
            if (first.isPresent()) {
                String filename = first.get();
                return filename.substring(0, (filename.length() - format.length()));
            }
        }
        return null;
    }
}
