package net.n2oapp.framework.config.test;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.pack.N2oOperationsPack;
import net.n2oapp.framework.config.metadata.pack.N2oSourceTypesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.reader.PropertiesReader;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * Базовый класс для тестирования конвейерной сборки метаданных
 */
public abstract class N2oTestBase {
    protected N2oApplicationBuilder builder;

    public void setUp() throws Exception {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        environment.setNamespaceReaderFactory(new ReaderFactoryByMap());
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("n2o_api_messages", "n2o_api_messages",  "n2o_config_messages", "test_messages", "messages");
        messageSource.setDefaultEncoding("UTF-8");
        Locale locale = new Locale("ru");
        LocaleContextHolder.setLocale(locale);
        environment.setMessageSource(new MessageSourceAccessor(messageSource));

        OverrideProperties n2oProperties = PropertiesReader.getPropertiesFromClasspath("META-INF/n2o.properties");
        OverrideProperties appProperties = PropertiesReader.getPropertiesFromClasspath("application.properties");
        appProperties.setBaseProperties(n2oProperties);
        environment.setSystemProperties(new SimplePropertyResolver(appProperties));

        builder = new N2oApplicationBuilder(environment);
        configure(builder);
        CompileInfo.setSourceTypes(builder.getEnvironment().getSourceTypeRegister());
    }

    protected void configure(N2oApplicationBuilder builder) {
        builder.packs(new N2oSourceTypesPack(),
                new N2oOperationsPack());
        builder.loaders(new SelectiveMetadataLoader(builder.getEnvironment().getNamespaceReaderFactory()));
    }

}
