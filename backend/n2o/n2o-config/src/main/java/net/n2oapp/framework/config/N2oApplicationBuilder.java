package net.n2oapp.framework.config;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.PersistersBuilder;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.register.route.RoutingResult;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.factory.AwareFactorySupport;
import net.n2oapp.framework.config.register.route.N2oRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Конструктор окружения {@link N2oEnvironment} и конвеера сборки метаданных {@link ReadPipeline}
 */
public class N2oApplicationBuilder implements
        ReadersBuilder<N2oApplicationBuilder>,
        PersistersBuilder<N2oApplicationBuilder> {
    private static final Logger logger = LoggerFactory.getLogger(N2oApplicationBuilder.class);

    private MetadataEnvironment environment;

    public N2oApplicationBuilder() {
        this.environment = new N2oEnvironment();
    }

    public N2oApplicationBuilder(MetadataEnvironment environment) {
        this.environment = environment;
    }

    @SafeVarargs
    public final N2oApplicationBuilder packs(MetadataPack<? super N2oApplicationBuilder>... packs) {
        Stream.of(packs).forEach(p -> p.build(this));
        return this;
    }

    public N2oApplicationBuilder types(MetaType... types) {
        environment.getSourceTypeRegister().addAll(Arrays.asList(types));
        return this;
    }

    public N2oApplicationBuilder operations(PipelineOperation<?, ?>... operations) {
        environment.getPipelineOperationFactory().add(operations);
        return this;
    }

    @Override
    @SafeVarargs
    public final N2oApplicationBuilder readers(NamespaceReader<? extends NamespaceUriAware>... readers) {
        Stream.of(readers).forEach(environment.getNamespaceReaderFactory()::add);
        return this;
    }

    @Override
    @SafeVarargs
    public final N2oApplicationBuilder ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
        Stream.of(ios).forEach(io -> environment.getNamespaceReaderFactory().add(new ProxyNamespaceIO<>(io)));
        Stream.of(ios).forEach(io -> environment.getNamespacePersisterFactory().add(new ProxyNamespaceIO<>(io)));
        return this;
    }

    public N2oApplicationBuilder persisters(NamespacePersister<? extends NamespaceUriAware>... persisters) {
        Stream.of(persisters).forEach(environment.getNamespacePersisterFactory()::add);
        return this;
    }

    public N2oApplicationBuilder compilers(SourceCompiler... compilers) {
        environment.getSourceCompilerFactory().add(compilers);
        return this;
    }

    public N2oApplicationBuilder providers(DynamicMetadataProvider... providers) {
        environment.getDynamicMetadataProviderFactory().add(providers);
        return this;
    }

    public N2oApplicationBuilder binders(MetadataBinder... binders) {
        environment.getMetadataBinderFactory().add(binders);
        return this;
    }

    public N2oApplicationBuilder scanners(MetadataScanner... scanners) {
        environment.getMetadataScannerFactory().add(scanners);
        return this;
    }

    public N2oApplicationBuilder loaders(SourceLoader... loaders) {
        environment.getSourceLoaderFactory().add(loaders);
        return this;
    }

    public N2oApplicationBuilder routes(RouteInfo... routes) {
        Stream.of(routes).forEach(
                routeInfo -> environment.getRouteRegister().addRoute(routeInfo.getUrlPattern(), routeInfo.getContext())
        );
        return this;
    }

    public N2oApplicationBuilder sources(SourceInfo... sources) {
        Stream.of(sources).forEach(environment.getMetadataRegister()::add);
        return this;
    }

    public N2oApplicationBuilder validators(SourceValidator... validators) {
        Stream.of(validators).forEach(environment.getSourceValidatorFactory()::add);
        return this;
    }

    public N2oApplicationBuilder mergers(SourceMerger... mergers) {
        Stream.of(mergers).forEach(environment.getSourceMergerFactory()::add);
        return this;
    }

    public N2oApplicationBuilder transformers(SourceTransformer... transformers) {
        Stream.of(transformers).forEach(environment.getSourceTransformerFactory()::add);
        return this;
    }

    public N2oApplicationBuilder transformers(CompileTransformer... transformers) {
        Stream.of(transformers).forEach(environment.getCompileTransformerFactory()::add);
        return this;
    }

    public N2oApplicationBuilder extensions(ExtensionAttributeMapper... extensions) {
        Stream.of(extensions).forEach(environment.getExtensionAttributeMapperFactory()::add);
        return this;
    }

    public N2oApplicationBuilder generators(ButtonGenerator... generators) {
        Stream.of(generators).forEach(environment.getButtonGeneratorFactory()::add);
        return this;
    }

    public N2oApplicationBuilder scan() {
        build();
        List<? extends SourceInfo> sources = environment.getMetadataScannerFactory().scan();
        environment.getMetadataRegister().addAll(sources);
        logger.info("Scanned " + sources.size() + " metadata");
        return this;
    }

    public ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> read() {
        build();
        return N2oPipelineSupport.readPipeline(environment).read();
    }

    public RoutingResult route(String url) {
        build();
        return new N2oRouter(environment.getRouteRegister(), read()
                .transform().validate().cache().copy()
                .compile().transform())
                .get(url);
    }

    public MetadataEnvironment getEnvironment() {
        return environment;
    }

    private N2oApplicationBuilder build() {
        AwareFactorySupport.enrich(environment.getDynamicMetadataProviderFactory(), environment);
        AwareFactorySupport.enrich(environment.getMetadataScannerFactory(), environment);
        AwareFactorySupport.enrich(environment.getNamespaceReaderFactory(), environment);
        AwareFactorySupport.enrich(environment.getSourceLoaderFactory(), environment);
        AwareFactorySupport.enrich(environment.getNamespacePersisterFactory(), environment);
        AwareFactorySupport.enrich(environment.getSourceMergerFactory(), environment);
        AwareFactorySupport.enrich(environment.getSourceTransformerFactory(), environment);
        AwareFactorySupport.enrich(environment.getSourceValidatorFactory(), environment);
        AwareFactorySupport.enrich(environment.getSourceCompilerFactory(), environment);
        AwareFactorySupport.enrich(environment.getCompileTransformerFactory(), environment);
        AwareFactorySupport.enrich(environment.getMetadataBinderFactory(), environment);
        AwareFactorySupport.enrich(environment.getPipelineOperationFactory(), environment);
        AwareFactorySupport.enrich(environment.getSubModelsProcessor(), environment);
        return this;
    }
}
