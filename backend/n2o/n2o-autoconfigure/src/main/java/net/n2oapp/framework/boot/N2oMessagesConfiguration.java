package net.n2oapp.framework.boot;

import net.n2oapp.cache.template.SyncCacheTemplate;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.config.compile.pipeline.operation.CompileCacheOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.LocalizedCompileCacheOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.LocalizedSourceCacheOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.SourceCacheOperation;
import net.n2oapp.framework.ui.servlet.ExposedResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Конфигурация сообщений локализации
 */
@Configuration
@AutoConfigureAfter(MessageSourceAutoConfiguration.class)
public class N2oMessagesConfiguration {

    @Value("${n2o.i18n.client.messages:client_messages}")
    private String clientBasenames;
    @Value("${n2o.i18n.client.use-code-as-default-message:false}")
    private boolean useCodeAsDefaultMessage;
    @Value("${spring.messages.encoding:UTF-8}")
    private Charset encoding;
    @Value("${spring.messages.cacheSeconds:-1}")
    private int cacheSeconds;
    @Value("${spring.messages.basename:messages}")
    private String basename;
    @Value("${n2o.i18n.default-locale:ru}")
    private String defaultLocale;


    @Bean("n2oMessageSource")
    @ConditionalOnMissingBean(name = "n2oMessageSource")
    public MessageSource n2oMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(encoding.name());
        messageSource.setCacheSeconds(cacheSeconds);
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
                StringUtils.trimAllWhitespace(basename)));
        messageSource.addBasenames("n2o_api_messages", "n2o_config_messages", "n2o_rest_messages");
        return messageSource;
    }

    @Bean("clientMessageSource")
    @ConditionalOnMissingBean(name = "clientMessageSource")
    public ExposedResourceBundleMessageSource clientMessageSource() {
        ExposedResourceBundleMessageSource messageSource = new ExposedResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames(StringUtils.commaDelimitedListToStringArray(
                StringUtils.trimAllWhitespace(clientBasenames)));
        messageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
        return messageSource;
    }

    @Bean("n2oMessageSourceAccessor")
    @ConditionalOnMissingBean(name = "n2oMessageSourceAccessor")
    @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "false")
    public MessageSourceAccessor fixedMessageSourceAccessor(@Qualifier("n2oMessageSource") MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, new Locale(defaultLocale));
    }

    @Bean("n2oMessageSourceAccessor")
    @ConditionalOnMissingBean(name = "n2oMessageSourceAccessor")
    @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "true")
    public MessageSourceAccessor messageSourceAccessor(@Qualifier("n2oMessageSource") MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean(name = "localeResolver")
    @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "false")
    public LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(new Locale(defaultLocale));
    }

    @Bean(name = "localeResolver")
    @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "true")
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale(defaultLocale));
        return slr;
    }

    @Bean
    @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "true")
    SourceCacheOperation sourceCacheOperation(CacheManager cacheManager, MetadataRegister metadataRegister) {
        return new LocalizedSourceCacheOperation(new SyncCacheTemplate(cacheManager), metadataRegister);
    }

    @Bean
    @ConditionalOnProperty(value = "n2o.i18n.enabled", havingValue = "true")
    CompileCacheOperation compileCacheOperation(CacheManager cacheManager) {
        return new LocalizedCompileCacheOperation(new SyncCacheTemplate(cacheManager));
    }

}
