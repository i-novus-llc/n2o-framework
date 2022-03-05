package net.n2oapp.framework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.register.route.MetadataRouter;
import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oPipelineSupport;
import net.n2oapp.framework.config.persister.MetadataPersister;
import net.n2oapp.framework.config.reader.ConfigMetadataLockerImpl;
import net.n2oapp.framework.config.reader.util.N2oJdomTextProcessing;
import net.n2oapp.framework.config.register.route.N2oRouter;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.warmup.HeaderWarmUpper;
import net.n2oapp.properties.io.PropertiesInfoCollector;
import net.n2oapp.watchdir.WatchDir;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * Конфигурация сборки и хранения метаданных
 */
@Configuration
@EnableCaching
@Import(N2oEnvironmentConfiguration.class)
public class N2oMetadataConfiguration {

    @Value("${n2o.config.path}")
    private String configPath;

    @Value("${n2o.project.path:}")
    private List<String> projectPaths;

    @Value("${n2o.config.readonly}")
    private boolean readonly;

    @Value("${n2o.config.ignores}")
    private List<String> ignores;

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper n2oObjectMapper() {
        return ObjectMapperConstructor.metaObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public N2oEventBus n2oEventBus(ApplicationEventPublisher publisher) {
        return new N2oEventBus(publisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataPersister metadataPersister() {
        return new MetadataPersister(readonly);
    }

    @Bean
    @ConditionalOnMissingBean
    public PropertiesInfoCollector propertiesMetaInfoCollector() {
        return new PropertiesInfoCollector("classpath*:META-INF/n2o.properties");
    }

    @Bean
    @ConditionalOnMissingBean
    public WatchDir watchDir() {
        return new WatchDir();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigMetadataLocker configMetadataLocker() {
        return new ConfigMetadataLockerImpl(configPath);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataRouter n2oRouter(MetadataEnvironment env) {
        return new N2oRouter(env, N2oPipelineSupport.readPipeline(env)
                .read().transform().validate().cache().copy().compile().transform());
    }

    @Bean
    @ConditionalOnMissingBean
    public N2oJdomTextProcessing n2oJdomTextProcessing(@Qualifier("n2oMessageSourceAccessor") MessageSourceAccessor n2oMessageSourceAccessor,
                                                       ConfigurableEnvironment environment) {
        return new N2oJdomTextProcessing(n2oMessageSourceAccessor, environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public N2oApplicationBuilder n2oApplicationBuilder(MetadataEnvironment n2oEnvironment,
                                                       Optional<Map<String, MetadataPack<? super N2oApplicationBuilder>>> beans) {
        N2oApplicationBuilder applicationBuilder = new N2oApplicationBuilder(n2oEnvironment);
        Map<String, MetadataPack<? super N2oApplicationBuilder>> packs = OverrideBean.removeOverriddenBeans(beans.orElse(Collections.emptyMap()));
        applicationBuilder.packs(packs.values().toArray(new MetadataPack[packs.values().size()]));
        return applicationBuilder;
    }

    @Bean(destroyMethod = "stop")
    @ConditionalOnMissingBean
    public ConfigStarter configStarter(N2oEventBus eventBus,
                                       ConfigMetadataLocker locker,
                                       WatchDir watchDir,
                                       N2oApplicationBuilder applicationBuilder,
                                       XmlInfoScanner xmlInfoScanner) {
        Collection<String> configPaths = PathUtil.getConfigPaths(configPath, projectPaths, xmlInfoScanner.getPattern(), ignores);
        return new ConfigStarter(applicationBuilder, eventBus, locker, watchDir, configPaths);
    }

    @Bean
    @ConditionalOnMissingBean
    public HeaderWarmUpper headerWarmUpper(N2oApplicationBuilder applicationBuilder, Environment environment) {
        HeaderWarmUpper headerWarmUpper = new HeaderWarmUpper();
        headerWarmUpper.setEnvironment(environment);
        headerWarmUpper.setApplicationBuilder(applicationBuilder);
        return headerWarmUpper;
    }
}
