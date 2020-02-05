package net.n2oapp.framework.boot;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.n2oapp.cache.template.SyncCacheTemplate;
import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.header.N2oHeader;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oMenu;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationFactory;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.reader.SourceLoaderFactory;
import net.n2oapp.framework.api.register.*;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.api.register.scan.MetadataScannerFactory;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.api.util.SubModelsProcessor;
import net.n2oapp.framework.boot.json.N2oJacksonModule;
import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineOperationFactory;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.compile.pipeline.operation.*;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.toolbar.CrudGenerator;
import net.n2oapp.framework.config.persister.MetadataPersister;
import net.n2oapp.framework.config.persister.N2oMetadataPersisterFactory;
import net.n2oapp.framework.config.reader.*;
import net.n2oapp.framework.config.reader.util.N2oJdomTextProcessing;
import net.n2oapp.framework.config.register.N2oMetadataRegister;
import net.n2oapp.framework.config.register.N2oSourceTypeRegister;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.route.N2oRouteRegister;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.config.register.scan.N2oMetadataScannerFactory;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.config.validate.N2oSourceValidatorFactory;
import net.n2oapp.framework.config.warmup.HeaderWarmUpper;
import net.n2oapp.properties.io.PropertiesInfoCollector;
import net.n2oapp.watchdir.WatchDir;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * Конфигурация сборки и хранения метаданных
 */
@Configuration
@ComponentScan(basePackages = {"net.n2oapp.framework.config"}, lazyInit = true)
@EnableCaching
public class N2oMetadataConfiguration {

    @Value("${n2o.config.path}")
    private String configPath;

    @Value("${n2o.project.path:}")
    private List<String> projectPaths;

    @Value("${n2o.config.readonly}")
    private boolean readonly;

    @Value("${n2o.config.ignores}")
    private List<String> ignores;

    @Bean(name = "n2oObjectMapper")
    public ObjectMapper n2oObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DomainProcessor.JAVA_DATE_FORMAT));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new N2oJacksonModule(new SimpleDateFormat(DomainProcessor.JAVA_DATE_FORMAT)));
        return objectMapper;
    }

    @Bean
    public DomainProcessor domainProcessor(@Qualifier("n2oObjectMapper") ObjectMapper objectMapper) {
        return new DomainProcessor(objectMapper);
    }

    @Bean
    public N2oEventBus n2oEventBus(ApplicationEventPublisher publisher) {
        return new N2oEventBus(publisher);
    }

    @Bean
    public MetadataPersister metadataPersister() {
        return new MetadataPersister(readonly);
    }

    @Bean
    public PropertiesInfoCollector propertiesMetaInfoCollector() {
        return new PropertiesInfoCollector("classpath*:META-INF/n2o.properties");
    }

    @Bean
    @Primary
    public MetadataRegister metadataRegister() {
        return new N2oMetadataRegister();
    }

    @Bean
    public WatchDir watchDir() {
        return new WatchDir();
    }

    @Bean
    public ConfigMetadataLocker configMetadataLocker() {
        return new ConfigMetadataLockerImpl(configPath);
    }


    @Bean
    public RouteRegister routeRegister() {
        return new N2oRouteRegister();
    }

    @Bean
    public MetadataRouter n2oRouter(N2oRouteRegister routeRegister, MetadataEnvironment env) {
        return new N2oRouter(env, N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy().compile().transform());
    }


    @Bean
    @ConditionalOnMissingBean
    public SubModelsProcessor subModelsProcessor(QueryProcessor queryProcessor) {
        return new N2oSubModelsProcessor(queryProcessor);
    }

    @Bean
    public ScriptProcessor scriptProcessor() {
        return new ScriptProcessor();
    }

    @Bean
    public N2oJdomTextProcessing n2oJdomTextProcessing(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor n2oMessageSourceAccessor,
                                                       ConfigurableEnvironment environment) {
        return new N2oJdomTextProcessing(n2oMessageSourceAccessor, environment);
    }

    @Bean
    public SourceTypeRegister sourceTypeRegister() {
        SourceTypeRegister register = new N2oSourceTypeRegister();
        register.addAll(asList(new MetaType("object", N2oObject.class),
                new MetaType("query", N2oQuery.class),
                new MetaType("page", N2oPage.class),
                new MetaType("widget", N2oWidget.class),
                new MetaType("fieldset", N2oFieldSet.class),
                new MetaType("header", N2oHeader.class),
                new MetaType("menu", N2oMenu.class)));
        return register;
    }

    @Bean
    public MetadataEnvironment n2oEnvironment(Map<String, ButtonGenerator> generators,
                                              @Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                              ConfigurableEnvironment springEnv,
                                              DomainProcessor domainProcessor,
                                              ContextProcessor contextProcessor,
                                              SourceTypeRegister sourceTypeRegister,
                                              MetadataRegister metadataRegister,
                                              RouteRegister routeRegister,
                                              MetadataScannerFactory metadataScannerFactory,
                                              SourceLoaderFactory sourceReaderFactory,
                                              SourceValidatorFactory sourceValidatorFactory,
                                              NamespacePersisterFactory persisterFactory,
                                              NamespaceReaderFactory readerFactory,
                                              SourceCompilerFactory sourceCompilerFactory,
                                              CompileTransformerFactory compileTransformerFactory,
                                              SourceTransformerFactory sourceTransformerFactory,
                                              SourceMergerFactory sourceMergerFactory,
                                              MetadataBinderFactory metadataBinderFactory,
                                              PipelineOperationFactory pipelineOperationFactory,
                                              DynamicMetadataProviderFactory dynamicMetadataProviderFactory,
                                              ExtensionAttributeMapperFactory extensionAttributeMapperFactory,
                                              ButtonGeneratorFactory buttonGeneratorFactory,
                                              SubModelsProcessor subModelsProcessor) {
        ((CrudGenerator) generators.get("crudGenerator")).setButtonGeneratorFactory(buttonGeneratorFactory);
        N2oEnvironment environment = new N2oEnvironment();
        environment.setSystemProperties(springEnv);
        environment.setMessageSource(messageSourceAccessor);
        environment.setSourceTypeRegister(sourceTypeRegister);
        environment.setMetadataRegister(metadataRegister);
        environment.setRouteRegister(routeRegister);
        environment.setMetadataScannerFactory(metadataScannerFactory);
        environment.setDynamicMetadataProviderFactory(dynamicMetadataProviderFactory);
        environment.setSourceLoaderFactory(sourceReaderFactory);
        environment.setNamespacePersisterFactory(persisterFactory);
        environment.setNamespaceReaderFactory(readerFactory);
        environment.setSourceValidatorFactory(sourceValidatorFactory);
        environment.setSourceCompilerFactory(sourceCompilerFactory);
        environment.setCompileTransformerFactory(compileTransformerFactory);
        environment.setSourceTransformerFactory(sourceTransformerFactory);
        environment.setSourceMergerFactory(sourceMergerFactory);
        environment.setPipelineOperationFactory(pipelineOperationFactory);
        environment.setMetadataBinderFactory(metadataBinderFactory);
        environment.setDomainProcessor(domainProcessor);
        environment.setContextProcessor(contextProcessor);
        environment.setExtensionAttributeMapperFactory(extensionAttributeMapperFactory);
        environment.setButtonGeneratorFactory(buttonGeneratorFactory);
        environment.setSubModelsProcessor(subModelsProcessor);
        return environment;
    }

    @Bean
    public N2oApplicationBuilder n2oApplicationBuilder(MetadataEnvironment n2oEnvironment,
                                                       Optional<Map<String, MetadataPack<? super N2oApplicationBuilder>>> beans) {
        N2oApplicationBuilder applicationBuilder = new N2oApplicationBuilder(n2oEnvironment);
        Map<String, MetadataPack<? super N2oApplicationBuilder>> packs = OverrideBean.removeOverriddenBeans(beans.orElse(Collections.emptyMap()));
        applicationBuilder.packs(packs.values().toArray(new MetadataPack[packs.values().size()]));
        return applicationBuilder;
    }

    @Bean(destroyMethod = "stop")
    public ConfigStarter configStarter(N2oEventBus eventBus,
                                       ConfigMetadataLocker locker,
                                       WatchDir watchDir,
                                       N2oApplicationBuilder applicationBuilder,
                                       XmlInfoScanner xmlInfoScanner) {
        Collection<String> configPaths = PathUtil.getConfigPaths(configPath, projectPaths, xmlInfoScanner.getPattern(), ignores);
        return new ConfigStarter(applicationBuilder, eventBus, locker, watchDir, configPaths);
    }

    @Bean
    public HeaderWarmUpper headerWarmUpper(N2oApplicationBuilder applicationBuilder, Environment environment) {
        HeaderWarmUpper headerWarmUpper = new HeaderWarmUpper();
        headerWarmUpper.setEnvironment(environment);
        headerWarmUpper.setApplicationBuilder(applicationBuilder);
        return headerWarmUpper;
    }

    @Configuration
    static class MetadataLoaderConfiguration {


        @Bean
        public XmlMetadataLoader xmlMetadataReader(NamespaceReaderFactory elementReaderFactory) {
            return new XmlMetadataLoader(elementReaderFactory);
        }

        @Bean
        public JavaSourceLoader javaSourceReader(N2oDynamicMetadataProviderFactory dynamicMetadataProviderFactory) {
            return new JavaSourceLoader(dynamicMetadataProviderFactory);//todo сейчас не кешируются объекты, если их вернулось множество
        }

        @Bean
        public GroovySourceReader groovySourceReader() {
            return new GroovySourceReader();
        }
    }

    @Configuration
    static class MetadataFactoryConfiguration {

        @Bean
        MetadataScannerFactory metadataScannerFactory(Optional<Map<String, MetadataScanner>> scaners) {
            return new N2oMetadataScannerFactory(scaners.orElse(Collections.emptyMap()));
        }

        @Bean
        NamespacePersisterFactory persisterFactory(ApplicationContext context) {
            N2oMetadataPersisterFactory metadataPersisterFactory = new N2oMetadataPersisterFactory();
            metadataPersisterFactory.setApplicationContext(context);
            return metadataPersisterFactory;
        }

        @Bean
        NamespaceReaderFactory readerFactory(ApplicationContext context) {
            N2oNamespaceReaderFactory metadataReaderFactory = new N2oNamespaceReaderFactory();
            metadataReaderFactory.setApplicationContext(context);
            return metadataReaderFactory;
        }

        @Bean
        PipelineOperationFactory pipelineOperationFactory(Optional<Map<String, PipelineOperation>> operations) {
            return new N2oPipelineOperationFactory(operations.orElse(Collections.emptyMap()));
        }


        @Bean
        SourceLoaderFactory sourceLoaderFactory(Map<String, SourceLoader> beans) {
            N2oSourceLoaderFactory configReaderFactory = new N2oSourceLoaderFactory(beans);
            return configReaderFactory;
        }

        @Bean
        N2oDynamicMetadataProviderFactory dynamicMetadataProviderFactory(Optional<Map<String, DynamicMetadataProvider>> providers) {
            return new N2oDynamicMetadataProviderFactory(providers.orElse(Collections.emptyMap()));
        }

        @Bean
        SourceValidatorFactory sourceValidatorFactory(Optional<Map<String, SourceValidator>> validators) {
            return new N2oSourceValidatorFactory(validators.orElse(Collections.emptyMap()));
        }

        @Bean
        SourceCompilerFactory sourceCompilerFactory(Map<String, SourceCompiler> compilers) {
            return new N2oSourceCompilerFactory(compilers);
        }

        @Bean
        CompileTransformerFactory compileTransformerFactory(Optional<Map<String, CompileTransformer>> transformers) {
            return new N2oCompileTransformerFactory(transformers.orElse(Collections.emptyMap()));
        }

        @Bean
        SourceTransformerFactory sourceTransformerFactory(Optional<Map<String, SourceTransformer>> transformers) {
            return new N2oSourceTransformerFactory(transformers.orElse(Collections.emptyMap()));
        }

        @Bean
        SourceMergerFactory sourceMergerFactory(Optional<Map<String, SourceMerger>> mergers) {
            return new N2oSourceMergerFactory(mergers.orElse(Collections.emptyMap()));
        }

        @Bean
        MetadataBinderFactory metadataBinderFactory(Optional<Map<String, MetadataBinder>> binders) {
            return new N2oMetadataBinderFactory(binders.orElse(Collections.emptyMap()));
        }

        @Bean
        ExtensionAttributeMapperFactory extensionAttributeMapperFactory(Optional<Map<String, ExtensionAttributeMapper>> extMappers) {
            return new N2oExtensionAttributeMapperFactory(extMappers.orElse(Collections.emptyMap()));
        }

        @Bean
        ButtonGeneratorFactory toolbarItemGeneratorFactory(Map<String, ButtonGenerator> generators) {
            return new N2oButtonGeneratorFactory(generators);
        }
    }

    @Configuration
    static class PipelineOperationConfiguration {

        @Bean
        @ConditionalOnMissingBean
        ReadOperation readOperation(MetadataRegister configRegister, SourceLoaderFactory readerFactory) {
            return new ReadOperation(configRegister, readerFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        MergeOperation mergeOperation(SourceMergerFactory sourceMergerFactory) {
            return new MergeOperation(sourceMergerFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        ValidateOperation validateOperation(SourceValidatorFactory sourceValidatorFactory) {
            return new ValidateOperation(sourceValidatorFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        SourceCacheOperation sourceCacheOperation(CacheManager cacheManager, MetadataRegister metadataRegister) {
            return new SourceCacheOperation(new SyncCacheTemplate(cacheManager), metadataRegister);
        }

        @Bean
        @ConditionalOnMissingBean
        CompileCacheOperation compileCacheOperation(CacheManager cacheManager) {
            return new CompileCacheOperation(new SyncCacheTemplate(cacheManager));
        }

        @Bean
        @ConditionalOnMissingBean
        SourceTransformOperation sourceTransformOperation(SourceTransformerFactory factory) {
            return new SourceTransformOperation(factory);
        }

        @Bean
        @ConditionalOnMissingBean
        CompileTransformOperation compileTransformOperation(CompileTransformerFactory factory) {
            return new CompileTransformOperation(factory);
        }

        @Bean
        @ConditionalOnMissingBean
        CompileOperation compileOperation(SourceCompilerFactory sourceCompilerFactory) {
            return new CompileOperation(sourceCompilerFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        BindOperation bindOperation(MetadataBinderFactory binderFactory) {
            return new BindOperation(binderFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        CopyOperation cloneOperation() {
            return new CopyOperation();
        }

    }

    @Configuration
    static class MetadataIOConfiguration {
        @Bean
        IOProcessor readerProcessor(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                    NamespaceReaderFactory readerFactory,
                                    ConfigurableEnvironment environment) {
            IOProcessorImpl ioProcessor = new IOProcessorImpl(readerFactory);
            ioProcessor.setMessageSourceAccessor(messageSourceAccessor);
            ioProcessor.setSystemProperties(environment);
            return ioProcessor;
        }

        @Bean
        IOProcessor persisterProcessor(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                       NamespacePersisterFactory persisterFactory,
                                       ConfigurableEnvironment environment) {
            IOProcessorImpl ioProcessor = new IOProcessorImpl(persisterFactory);
            ioProcessor.setMessageSourceAccessor(messageSourceAccessor);
            ioProcessor.setSystemProperties(environment);
            return ioProcessor;
        }
    }
}
