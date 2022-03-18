package net.n2oapp.framework.config.compile.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.N2oWebAppEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.validate.SourceValidatorFactory;
import net.n2oapp.framework.api.reader.SourceLoaderFactory;
import net.n2oapp.framework.api.register.ComponentTypeRegister;
import net.n2oapp.framework.api.register.DynamicMetadataProviderFactory;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.api.register.scan.MetadataScannerFactory;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.reader.N2oSourceLoaderFactory;
import net.n2oapp.framework.config.register.N2oComponentTypeRegister;
import net.n2oapp.framework.config.register.N2oMetadataRegister;
import net.n2oapp.framework.config.register.N2oSourceTypeRegister;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import net.n2oapp.framework.config.register.route.N2oRouteRegister;
import net.n2oapp.framework.config.register.scan.N2oMetadataScannerFactory;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.validate.N2oSourceValidatorFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.PropertyResolver;

/**
 * Окружение сборки метаданных
 */
public class N2oEnvironment implements MetadataEnvironment {
    private MetadataRegister metadataRegister;
    private RouteRegister routeRegister;
    private SourceTypeRegister sourceTypeRegister;
    private ComponentTypeRegister componentTypeRegister;

    private MessageSourceAccessor messageSource;
    private DomainProcessor domainProcessor;
    private PropertyResolver systemProperties;
    private ContextProcessor contextProcessor;

    private MetadataScannerFactory metadataScannerFactory;
    private SourceLoaderFactory sourceLoaderFactory;
    private SourceValidatorFactory sourceValidatorFactory;
    private SourceCompilerFactory sourceCompilerFactory;
    private CompileTransformerFactory compileTransformerFactory;
    private SourceTransformerFactory sourceTransformerFactory;
    private SourceMergerFactory sourceMergerFactory;
    private MetadataBinderFactory metadataBinderFactory;
    private NamespaceReaderFactory namespaceReaderFactory;
    private NamespacePersisterFactory namespacePersisterFactory;
    private DynamicMetadataProviderFactory dynamicMetadataProviderFactory;
    private PipelineOperationFactory pipelineOperationFactory;
    private ExtensionAttributeMapperFactory extensionAttributeMapperFactory;
    private ButtonGeneratorFactory buttonGeneratorFactory;

    private PipelineFunction<ReadTerminalPipeline<?>> readPipelineFunction = p -> p.read().transform().validate().cache();
    private PipelineFunction<ReadCompileTerminalPipeline<?>> readCompilePipelineFunction = p -> p.read().transform().validate().cache().copy().compile().transform().cache();
    private PipelineFunction<ReadCompileBindTerminalPipeline> readCompileBindTerminalPipelineFunction = p -> p
            .read().transform().validate().cache().copy()
            .compile().transform().cache().copy().bind();
    private PipelineFunction<CompileTerminalPipeline<?>> compilePipelineFunction = p -> p.merge().transform().compile().transform();
    private PipelineFunction<BindTerminalPipeline> bindPipelineFunction = p -> p.bind();

    public N2oEnvironment() {
        this.metadataRegister = new N2oMetadataRegister();
        this.routeRegister = new N2oRouteRegister();
        this.sourceTypeRegister = new N2oSourceTypeRegister();
        this.componentTypeRegister = new N2oComponentTypeRegister();

        this.messageSource = new MessageSourceAccessor(new ResourceBundleMessageSource());
        this.systemProperties = new N2oWebAppEnvironment();
        this.domainProcessor = new DomainProcessor(new ObjectMapper());
        this.contextProcessor = new ContextProcessor(new TestContextEngine());

        this.namespaceReaderFactory = new ReaderFactoryByMap();
        this.namespacePersisterFactory = new PersisterFactoryByMap();
        this.dynamicMetadataProviderFactory = new N2oDynamicMetadataProviderFactory();
        this.metadataScannerFactory = new N2oMetadataScannerFactory();
        this.sourceLoaderFactory = new N2oSourceLoaderFactory();
        this.sourceValidatorFactory = new N2oSourceValidatorFactory();
        this.sourceCompilerFactory = new N2oSourceCompilerFactory();
        this.compileTransformerFactory = new N2oCompileTransformerFactory();
        this.sourceTransformerFactory = new N2oSourceTransformerFactory();
        this.extensionAttributeMapperFactory = new N2oExtensionAttributeMapperFactory();
        this.sourceMergerFactory = new N2oSourceMergerFactory();
        this.metadataBinderFactory = new N2oMetadataBinderFactory();
        this.pipelineOperationFactory = new N2oPipelineOperationFactory();
        this.buttonGeneratorFactory = new N2oButtonGeneratorFactory();
    }

    public N2oEnvironment(MetadataEnvironment copy) {
        this.metadataRegister = copy.getMetadataRegister();
        this.routeRegister = copy.getRouteRegister();
        this.sourceTypeRegister = copy.getSourceTypeRegister();

        this.messageSource = copy.getMessageSource();
        this.systemProperties = copy.getSystemProperties();
        this.domainProcessor = copy.getDomainProcessor();
        this.contextProcessor = copy.getContextProcessor();

        this.namespaceReaderFactory = copy.getNamespaceReaderFactory();
        this.namespacePersisterFactory = copy.getNamespacePersisterFactory();
        this.dynamicMetadataProviderFactory = copy.getDynamicMetadataProviderFactory();
        this.metadataScannerFactory = copy.getMetadataScannerFactory();
        this.sourceLoaderFactory = copy.getSourceLoaderFactory();
        this.sourceValidatorFactory = copy.getSourceValidatorFactory();
        this.sourceCompilerFactory = copy.getSourceCompilerFactory();
        this.compileTransformerFactory = copy.getCompileTransformerFactory();
        this.sourceTransformerFactory = copy.getSourceTransformerFactory();
        this.extensionAttributeMapperFactory = copy.getExtensionAttributeMapperFactory();
        this.sourceMergerFactory = copy.getSourceMergerFactory();
        this.metadataBinderFactory = copy.getMetadataBinderFactory();
        this.pipelineOperationFactory = copy.getPipelineOperationFactory();
        this.buttonGeneratorFactory = copy.getButtonGeneratorFactory();

        this.readPipelineFunction = copy.getReadPipelineFunction();
        this.readCompilePipelineFunction = copy.getReadCompilePipelineFunction();
        this.readCompileBindTerminalPipelineFunction = copy.getReadCompileBindTerminalPipelineFunction();
        this.compilePipelineFunction = copy.getCompilePipelineFunction();
        this.bindPipelineFunction = copy.getBindPipelineFunction();
    }

    @Override
    public MetadataRegister getMetadataRegister() {
        return metadataRegister;
    }

    public void setMetadataRegister(MetadataRegister metadataRegister) {
        this.metadataRegister = metadataRegister;
    }

    @Override
    public RouteRegister getRouteRegister() {
        return routeRegister;
    }

    public void setRouteRegister(RouteRegister routeRegister) {
        this.routeRegister = routeRegister;
    }

    @Override
    public SourceLoaderFactory getSourceLoaderFactory() {
        return sourceLoaderFactory;
    }

    public void setSourceLoaderFactory(SourceLoaderFactory sourceLoaderFactory) {
        this.sourceLoaderFactory = sourceLoaderFactory;
    }

    @Override
    public SourceValidatorFactory getSourceValidatorFactory() {
        return sourceValidatorFactory;
    }

    public void setSourceValidatorFactory(SourceValidatorFactory sourceValidatorFactory) {
        this.sourceValidatorFactory = sourceValidatorFactory;
    }

    @Override
    public SourceCompilerFactory getSourceCompilerFactory() {
        return sourceCompilerFactory;
    }

    public void setSourceCompilerFactory(SourceCompilerFactory sourceCompilerFactory) {
        this.sourceCompilerFactory = sourceCompilerFactory;
    }

    @Override
    public CompileTransformerFactory getCompileTransformerFactory() {
        return compileTransformerFactory;
    }

    public void setCompileTransformerFactory(CompileTransformerFactory compileTransformerFactory) {
        this.compileTransformerFactory = compileTransformerFactory;
    }

    @Override
    public SourceTransformerFactory getSourceTransformerFactory() {
        return sourceTransformerFactory;
    }

    public void setSourceTransformerFactory(SourceTransformerFactory sourceTransformerFactory) {
        this.sourceTransformerFactory = sourceTransformerFactory;
    }

    @Override
    public SourceMergerFactory getSourceMergerFactory() {
        return sourceMergerFactory;
    }

    public void setSourceMergerFactory(SourceMergerFactory sourceMergerFactory) {
        this.sourceMergerFactory = sourceMergerFactory;
    }

    @Override
    public MetadataBinderFactory getMetadataBinderFactory() {
        return metadataBinderFactory;
    }

    public void setMetadataBinderFactory(MetadataBinderFactory metadataBinderFactory) {
        this.metadataBinderFactory = metadataBinderFactory;
    }

    @Override
    public PropertyResolver getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(PropertyResolver systemProperties) {
        this.systemProperties = systemProperties;
    }

    @Override
    public ContextProcessor getContextProcessor() {
        return contextProcessor;
    }

    public void setContextProcessor(ContextProcessor contextProcessor) {
        this.contextProcessor = contextProcessor;
    }

    @Override
    public DomainProcessor getDomainProcessor() {
        return domainProcessor;
    }

    public void setDomainProcessor(DomainProcessor domainProcessor) {
        this.domainProcessor = domainProcessor;
    }

    @Override
    public MetadataScannerFactory getMetadataScannerFactory() {
        return metadataScannerFactory;
    }

    public void setMetadataScannerFactory(MetadataScannerFactory metadataScannerFactory) {
        this.metadataScannerFactory = metadataScannerFactory;
    }

    @Override
    public SourceTypeRegister getSourceTypeRegister() {
        return sourceTypeRegister;
    }

    @Override
    public ComponentTypeRegister getComponentTypeRegister() {
        return componentTypeRegister;
    }

    public void setSourceTypeRegister(SourceTypeRegister sourceTypeRegister) {
        this.sourceTypeRegister = sourceTypeRegister;
    }

    @Override
    public NamespaceReaderFactory getNamespaceReaderFactory() {
        return namespaceReaderFactory;
    }

    public void setNamespaceReaderFactory(NamespaceReaderFactory namespaceReaderFactory) {
        this.namespaceReaderFactory = namespaceReaderFactory;
    }

    @Override
    public NamespacePersisterFactory getNamespacePersisterFactory() {
        return namespacePersisterFactory;
    }

    public void setNamespacePersisterFactory(NamespacePersisterFactory namespacePersisterFactory) {
        this.namespacePersisterFactory = namespacePersisterFactory;
    }

    @Override
    public DynamicMetadataProviderFactory getDynamicMetadataProviderFactory() {
        return dynamicMetadataProviderFactory;
    }

    public void setDynamicMetadataProviderFactory(DynamicMetadataProviderFactory dynamicMetadataProviderFactory) {
        this.dynamicMetadataProviderFactory = dynamicMetadataProviderFactory;
    }

    @Override
    public PipelineOperationFactory getPipelineOperationFactory() {
        return pipelineOperationFactory;
    }

    public void setPipelineOperationFactory(PipelineOperationFactory pipelineOperationFactory) {
        this.pipelineOperationFactory = pipelineOperationFactory;
    }

    @Override
    public MessageSourceAccessor getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSourceAccessor messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public PipelineFunction<ReadTerminalPipeline<?>> getReadPipelineFunction() {
        return readPipelineFunction;
    }

    public void setReadPipelineFunction(PipelineFunction<ReadTerminalPipeline<?>> readPipelineFunction) {
        this.readPipelineFunction = readPipelineFunction;
    }

    @Override
    public PipelineFunction<ReadCompileTerminalPipeline<?>> getReadCompilePipelineFunction() {
        return readCompilePipelineFunction;
    }

    public void setReadCompileBindTerminalPipelineFunction(PipelineFunction<ReadCompileBindTerminalPipeline> readCompileBindTerminalPipelineFunction) {
        this.readCompileBindTerminalPipelineFunction = readCompileBindTerminalPipelineFunction;
    }

    @Override
    public PipelineFunction<ReadCompileBindTerminalPipeline> getReadCompileBindTerminalPipelineFunction() {
        return readCompileBindTerminalPipelineFunction;
    }

    public void setReadCompilePipelineFunction(PipelineFunction<ReadCompileTerminalPipeline<?>> readCompilePipelineFunction) {
        this.readCompilePipelineFunction = readCompilePipelineFunction;
    }

    @Override
    public PipelineFunction<CompileTerminalPipeline<?>> getCompilePipelineFunction() {
        return compilePipelineFunction;
    }

    @Override
    public PipelineFunction<BindTerminalPipeline> getBindPipelineFunction() {
        return bindPipelineFunction;
    }

    public void setCompilePipelineFunction(PipelineFunction<CompileTerminalPipeline<?>> compilePipelineFunction) {
        this.compilePipelineFunction = compilePipelineFunction;
    }

    public void setBindPipelineFunction(PipelineFunction<BindTerminalPipeline> bindPipelineFunction) {
        this.bindPipelineFunction = bindPipelineFunction;
    }

    @Override
    public ExtensionAttributeMapperFactory getExtensionAttributeMapperFactory() {
        return extensionAttributeMapperFactory;
    }

    @Override
    public ButtonGeneratorFactory getButtonGeneratorFactory() {
        return buttonGeneratorFactory;
    }

    public void setButtonGeneratorFactory(ButtonGeneratorFactory buttonGeneratorFactory) {
        this.buttonGeneratorFactory = buttonGeneratorFactory;
    }

    public void setExtensionAttributeMapperFactory(ExtensionAttributeMapperFactory extensionAttributeMapperFactory) {
        this.extensionAttributeMapperFactory = extensionAttributeMapperFactory;
    }
}
