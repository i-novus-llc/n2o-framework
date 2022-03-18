package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.cache.template.SyncCacheTemplate;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oMenu;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperation;
import net.n2oapp.framework.api.metadata.pipeline.PipelineOperationFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.reader.SourceLoaderFactory;
import net.n2oapp.framework.api.register.*;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.api.register.scan.MetadataScannerFactory;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineOperationFactory;
import net.n2oapp.framework.config.compile.pipeline.operation.*;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.toolbar.CrudGenerator;
import net.n2oapp.framework.config.persister.N2oMetadataPersisterFactory;
import net.n2oapp.framework.config.reader.GroovySourceReader;
import net.n2oapp.framework.config.reader.N2oNamespaceReaderFactory;
import net.n2oapp.framework.config.reader.N2oSourceLoaderFactory;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.ConfigRepository;
import net.n2oapp.framework.config.register.N2oComponentTypeRegister;
import net.n2oapp.framework.config.register.N2oMetadataRegister;
import net.n2oapp.framework.config.register.N2oSourceTypeRegister;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.route.N2oRouteRegister;
import net.n2oapp.framework.config.register.route.StubRouteRepository;
import net.n2oapp.framework.config.register.scan.N2oMetadataScannerFactory;
import net.n2oapp.framework.config.validate.N2oSourceValidatorFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;

@Configuration
@ComponentScan(basePackages = {"net.n2oapp.framework.config"}, lazyInit = true)
public class N2oEnvironmentConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DomainProcessor domainProcessor() {
        ObjectMapper objectMapper = ObjectMapperConstructor.dataObjectMapper();
        return new DomainProcessor(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextProcessor contextProcessor(Context context) {
        return new ContextProcessor(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataRegister metadataRegister() {
        return new N2oMetadataRegister();
    }

    @Bean
    @ConditionalOnMissingBean
    public ComponentTypeRegister componentTypeRegister() {
        return new N2oComponentTypeRegister();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouteRegister routeRegister(Optional<ConfigRepository<RouteInfoKey, CompileContext>> repository) {
        return new N2oRouteRegister(repository.orElse(new StubRouteRepository()));
    }

    @Bean
    @ConditionalOnMissingBean
    public ScriptProcessor scriptProcessor() {
        return new ScriptProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public SourceTypeRegister sourceTypeRegister() {
        SourceTypeRegister register = new N2oSourceTypeRegister();
        register.addAll(asList(new MetaType("object", N2oObject.class),
                new MetaType("query", N2oQuery.class),
                new MetaType("page", N2oPage.class),
                new MetaType("widget", N2oWidget.class),
                new MetaType("fieldset", N2oFieldSet.class),
                new MetaType("application", N2oApplication.class),
                new MetaType("menu", N2oMenu.class)));
        return register;
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataEnvironment n2oEnvironment(Map<String, ButtonGenerator> generators,
                                              @Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                              ConfigurableEnvironment properties,
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
                                              ButtonGeneratorFactory buttonGeneratorFactory) {
        ((CrudGenerator) generators.get("crudGenerator")).setButtonGeneratorFactory(buttonGeneratorFactory);
        N2oEnvironment environment = new N2oEnvironment();
        environment.setSystemProperties(properties);
        environment.setMessageSource(messageSourceAccessor);
        environment.setSourceTypeRegister(sourceTypeRegister);
        environment.setMetadataRegister(metadataRegister);
        environment.setRouteRegister(routeRegister);
        environment.setNamespaceReaderFactory(new N2oNamespaceReaderFactory());
        environment.setNamespacePersisterFactory(new N2oMetadataPersisterFactory());
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
        return environment;
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
        CompileTransformerFactory compileTransformerFactory(Optional<Map<String, CompileTransformer<?, ?>>> transformers) {
            return new N2oCompileTransformerFactory(transformers.orElse(Collections.emptyMap()));
        }

        @Bean
        SourceTransformerFactory sourceTransformerFactory(Optional<Map<String, SourceTransformer<?>>> transformers) {
            return new N2oSourceTransformerFactory(transformers.orElse(Collections.emptyMap()));
        }

        @Bean
        SourceMergerFactory sourceMergerFactory(Optional<Map<String, SourceMerger<?>>> mergers) {
            return new N2oSourceMergerFactory(mergers.orElse(Collections.emptyMap()));
        }

        @Bean
        MetadataBinderFactory metadataBinderFactory(Optional<Map<String, MetadataBinder<?>>> binders) {
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
        @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "false")
        SourceCacheOperation sourceCacheOperation(CacheManager cacheManager, MetadataRegister metadataRegister) {
            return new SourceCacheOperation(new SyncCacheTemplate(cacheManager), metadataRegister);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "false")
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
            ioProcessor.setFailFast(environment.getProperty("n2o.config.fail_fast", Boolean.class, true));
            return ioProcessor;
        }

        @Bean
        IOProcessor persisterProcessor(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor,
                                       NamespacePersisterFactory persisterFactory,
                                       ConfigurableEnvironment environment) {
            IOProcessorImpl ioProcessor = new IOProcessorImpl(persisterFactory);
            ioProcessor.setMessageSourceAccessor(messageSourceAccessor);
            ioProcessor.setSystemProperties(environment);
            ioProcessor.setFailFast(environment.getProperty("n2o.config.fail_fast", Boolean.class, true));
            return ioProcessor;
        }
    }
}
