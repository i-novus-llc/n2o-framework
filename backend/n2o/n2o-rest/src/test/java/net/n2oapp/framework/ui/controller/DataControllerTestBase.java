package net.n2oapp.framework.ui.controller;

import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.reader.PropertiesReader;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.PropertyResolver;

import java.util.stream.Stream;

public abstract class DataControllerTestBase {

    protected static boolean setUpIsDone = false;

    protected N2oApplicationBuilder builder;

    @BeforeEach
    public void setUp() {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        environment.setNamespaceReaderFactory(new ReaderFactoryByMap());
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("n2o_messages", "messages");
        messageSource.setDefaultEncoding("UTF-8");
        environment.setMessageSource(new MessageSourceAccessor(messageSource));
        OverrideProperties properties = PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties");
        properties.put("n2o.engine.mapper", "spel");
        SimplePropertyResolver propertyResolver = new SimplePropertyResolver(properties);
        setUpStaticProperties(propertyResolver);
        environment.setSystemProperties(propertyResolver);
        builder = new N2oApplicationBuilder(environment);
        configure(builder);
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
    }


    protected static void setUpStaticProperties(PropertyResolver propertyResolver) {
        if (setUpIsDone) return;
        StaticProperties staticProperties = new StaticProperties();
        staticProperties.setPropertyResolver(propertyResolver);
        setUpIsDone = true;
    }

    private void configure(N2oApplicationBuilder builder) {
        builder.packs(new N2oSourceTypesPack(),
                new N2oDataProvidersPack(),
                new N2oObjectsPack(), new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oPagesPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack(),
                new N2oOperationsPack(), new N2oQueriesPack());
        builder.loaders(new SelectiveMetadataLoader(builder.getEnvironment().getNamespaceReaderFactory()));
    }


    protected ReadCompileTerminalPipeline<ReadCompileBindTerminalPipeline> compile(String... uri) {
        if (uri != null)
            Stream.of(uri).forEach(u -> builder.sources(new CompileInfo(u)));
        return builder.read().compile();
    }
}
