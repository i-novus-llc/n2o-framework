package net.n2oapp.framework.config;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.*;
import net.n2oapp.framework.api.metadata.global.util.ComponentType;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.pipeline.*;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.factory.AwareFactorySupport;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.reader.PropertiesReader;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertyResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * Конструктор окружения {@link N2oEnvironment} и конвеера сборки метаданных {@link ReadPipeline}
 */
public class N2oApplicationBuilder implements XmlIOBuilder<N2oApplicationBuilder>, PipelineSupport {
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

    /**
     * Добавить i/o ридеры/персистеры метаданных
     */
    @Override
    @SafeVarargs
    public final N2oApplicationBuilder ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
        Stream.of(ios).forEach(io -> environment.getNamespaceReaderFactory().add(new ProxyNamespaceIO<>(io)));
        Stream.of(ios).forEach(io -> environment.getNamespacePersisterFactory().add(new ProxyNamespaceIO<>(io)));
        return this;
    }

    /**
     * Добавить сборщики метаданных
     */
    public N2oApplicationBuilder compilers(SourceCompiler<?, ?, ?>... compilers) {
        environment.getSourceCompilerFactory().add(compilers);
        return this;
    }

    /**
     * Добавить динамические провайдеры метаданных метаданных
     */
    public N2oApplicationBuilder providers(DynamicMetadataProvider... providers) {
        environment.getDynamicMetadataProviderFactory().add(providers);
        return this;
    }

    /**
     * Добавить биндеры метаданных
     */
    public N2oApplicationBuilder binders(MetadataBinder<?>... binders) {
        environment.getMetadataBinderFactory().add(binders);
        return this;
    }

    /**
     * Добавить сканеры метаданных
     */
    public N2oApplicationBuilder scanners(MetadataScanner<?>... scanners) {
        environment.getMetadataScannerFactory().add(scanners);
        return this;
    }

    /**
     * Добавить лоадеры метаданных
     */
    public N2oApplicationBuilder loaders(SourceLoader<?>... loaders) {
        environment.getSourceLoaderFactory().add(loaders);
        return this;
    }

    /**
     * Добавить маршруты получения метаданных
     */
    public N2oApplicationBuilder routes(RouteInfo... routes) {
        Stream.of(routes).forEach(
                routeInfo -> environment.getRouteRegister().addRoute(routeInfo.getUrlPattern(), routeInfo.getContext())
        );
        return this;
    }

    /**
     * Добавить информацию об исходных метаданных
     */
    public N2oApplicationBuilder sources(SourceInfo... sources) {
        Stream.of(sources).forEach(environment.getMetadataRegister()::add);
        return this;
    }

    /**
     * Добавить валидаторы метаданных
     */
    public N2oApplicationBuilder validators(SourceValidator<?>... validators) {
        Stream.of(validators).forEach(environment.getSourceValidatorFactory()::add);
        return this;
    }

    /**
     * Добавить мержеры метаданных
     */
    public N2oApplicationBuilder mergers(SourceMerger<?>... mergers) {
        Stream.of(mergers).forEach(environment.getSourceMergerFactory()::add);
        return this;
    }

    /**
     * Добавить трансформаторы исходных метаданных
     */
    public N2oApplicationBuilder transformers(SourceTransformer<?>... transformers) {
        Stream.of(transformers).forEach(environment.getSourceTransformerFactory()::add);
        return this;
    }

    /**
     * Добавить трансформаторы собранных метаданных
     */
    public N2oApplicationBuilder transformers(CompileTransformer<?, ?>... transformers) {
        Stream.of(transformers).forEach(environment.getCompileTransformerFactory()::add);
        return this;
    }

    /**
     * Добавить преобразователи дополнительных атрибутов метаданных
     */
    public N2oApplicationBuilder extensions(ExtensionAttributeMapper... extensions) {
        Stream.of(extensions).forEach(environment.getExtensionAttributeMapperFactory()::add);
        return this;
    }

    /**
     * Добавить генераторы метаданных
     */
    public N2oApplicationBuilder generators(ButtonGenerator... generators) {
        Stream.of(generators).forEach(environment.getButtonGeneratorFactory()::add);
        return this;
    }

    /**
     * Добавить системные свойства (key=value)
     */
    public N2oApplicationBuilder properties(String... properties) {
        PropertyResolver systemProperties = environment.getSystemProperties();
        if (!(systemProperties instanceof SimplePropertyResolver))
            throw new IllegalArgumentException("System properties is readonly");
        Stream.of(properties).forEach(p -> {
            String[] split = p.contains("=") ? p.split("=") : p.split(":");
            ((SimplePropertyResolver) systemProperties).setProperty(split[0], split[1]);
        });
        return this;
    }

    /**
     * Добавить файлы properties из classpath
     */
    public N2oApplicationBuilder propertySources(String... propertySources) {
        PropertyResolver systemProperties = environment.getSystemProperties();
        if (!(systemProperties instanceof SimplePropertyResolver))
            throw new IllegalArgumentException("System properties is readonly");
        Properties baseProperties = ((SimplePropertyResolver) systemProperties).getProperties();
        for (String propertySource : propertySources) {
            OverrideProperties properties = PropertiesReader.getPropertiesFromClasspath(propertySource);
            properties.setBaseProperties(baseProperties);
            baseProperties = properties;
        }
        ((N2oEnvironment) environment).setSystemProperties(new SimplePropertyResolver(baseProperties));
        return this;
    }

    /**
     * Запустить сканирование метаданных
     */
    public N2oApplicationBuilder scan() {
        build();
        List<? extends SourceInfo> sources = environment.getMetadataScannerFactory().scan();
        environment.getMetadataRegister().addAll(sources);
        logger.info("Scanned " + sources.size() + " metadata");
        componentTypeScan();
        return this;
    }

    private void componentTypeScan() {
        Reflections reflections = new Reflections("net.n2oapp.framework.api");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(ComponentType.class);
        set.forEach(clazz -> {
            Set<Class<?>> subTypesOf = reflections.getSubTypesOf((Class<Object>) clazz);
            subTypesOf.stream().filter(cl -> !Modifier.isAbstract(cl.getModifiers()))
                    .forEach(c -> {
                        if (c.isAnnotationPresent(ComponentType.class)) {
                            ComponentType annotation = c.getAnnotation(ComponentType.class);
                            environment.getComponentTypeRegister().add(annotation.value(), c);
                        } else {
                            environment.getComponentTypeRegister().add(c.getSimpleName(), c);
                        }
                    });
        });
    }

    /**
     * Запустить конвейер чтения метаданных
     */
    @Override
    public ReadTerminalPipeline<ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline>> read() {
        build();
        return N2oPipelineSupport.readPipeline(environment).read();
    }

    /**
     * Запустить конвейер сборки метаданных
     */
    @Override
    public CompileTerminalPipeline<CompileBindTerminalPipeline> compile() {
        build();
        return N2oPipelineSupport.compilePipeline(environment).compile();
    }

    /**
     * Запустить конвейер слияния метаданных
     */
    @Override
    public CompilePipeline merge() {
        build();
        return N2oPipelineSupport.compilePipeline(environment).merge();
    }

    /**
     * Запустить конвейер связывания метаданных с данными
     */
    public BindTerminalPipeline bind() {
        build();
        return N2oPipelineSupport.bindPipeline(environment).bind();
    }

    /**
     * Запустить конвейер записи метаданных
     */
    @Override
    public PersistTerminalPipeline persist() {
        build();
        return N2oPipelineSupport.persistPipeline(environment).persist();
    }

    /**
     * Найти контекст метаданной по маршруту
     *
     * @param url           Адрес маршрута
     * @param compiledClass Класс собранной метаданной
     * @param params        Параметры маршрута
     * @return Контекст найденной метаданной или null
     */
    public <D extends Compiled> CompileContext<D, ?> route(String url, Class<D> compiledClass, Map<String, String[]> params) {
        build();
        return new N2oRouter(environment, read()
                .transform().validate().cache().copy()
                .compile().transform())
                .get(url, compiledClass, params);
    }

    /**
     * Получить окружение сборки метаданных
     *
     * @return Окруждение сборки метаданных
     */
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
        return this;
    }
}
