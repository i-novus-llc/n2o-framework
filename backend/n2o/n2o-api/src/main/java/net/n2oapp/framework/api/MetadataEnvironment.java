package net.n2oapp.framework.api;

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
import net.n2oapp.framework.api.util.SubModelsProcessor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

/**
 * Окружение сборки метаданных
 */
public interface MetadataEnvironment {

    MessageSourceAccessor getMessageSource();

    PropertyResolver getSystemProperties();

    DomainProcessor getDomainProcessor();

    ContextProcessor getContextProcessor();

    SourceTypeRegister getSourceTypeRegister();

    ComponentTypeRegister getComponentTypeRegister();

    MetadataRegister getMetadataRegister();

    RouteRegister getRouteRegister();

    MetadataScannerFactory getMetadataScannerFactory();

    SourceLoaderFactory getSourceLoaderFactory();

    DynamicMetadataProviderFactory getDynamicMetadataProviderFactory();

    NamespaceReaderFactory getNamespaceReaderFactory();

    NamespacePersisterFactory getNamespacePersisterFactory();

    SourceMergerFactory getSourceMergerFactory();

    SourceValidatorFactory getSourceValidatorFactory();

    SourceTransformerFactory getSourceTransformerFactory();

    SourceCompilerFactory getSourceCompilerFactory();

    CompileTransformerFactory getCompileTransformerFactory();

    MetadataBinderFactory getMetadataBinderFactory();

    PipelineOperationFactory getPipelineOperationFactory();

    PipelineFunction<ReadTerminalPipeline<?>> getReadPipelineFunction();

    PipelineFunction<ReadCompileTerminalPipeline<?>> getReadCompilePipelineFunction();

    PipelineFunction<ReadCompileBindTerminalPipeline> getReadCompileBindTerminalPipelineFunction();

    PipelineFunction<CompileTerminalPipeline<?>> getCompilePipelineFunction();

    PipelineFunction<BindTerminalPipeline> getBindPipelineFunction();

    ExtensionAttributeMapperFactory getExtensionAttributeMapperFactory();

    ButtonGeneratorFactory getButtonGeneratorFactory();
}
