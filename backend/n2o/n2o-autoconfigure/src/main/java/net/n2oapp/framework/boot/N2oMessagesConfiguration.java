package net.n2oapp.framework.boot;

import net.n2oapp.framework.ui.servlet.ExposedResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;

/**
 * Конфигурация сообщений локализации
 */
@Configuration
@AutoConfigureAfter(MessageSourceAutoConfiguration.class)
public class N2oMessagesConfiguration {

    @Value("${n2o.i18n.client.messages:client_messages}")
    private String clientBasenames;
    @Value("${spring.messages.encoding:UTF-8}")
    private Charset encoding;
    @Value("${spring.messages.cacheSeconds:-1}")
    private int cacheSeconds;
    @Value("${spring.messages.basename:messages}")
    private String basename;


    @Bean("n2oMessageSource")
    @Primary
    @ConditionalOnMissingBean(name = "n2oMessageSource")
    public MessageSource n2oMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(encoding.name());
        messageSource.setCacheSeconds(cacheSeconds);
        messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
                StringUtils.trimAllWhitespace(basename)));
        messageSource.addBasenames("n2o_messages", "n2o_content");
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean(name = "clientMessageSource")
    public ExposedResourceBundleMessageSource clientMessageSource() {
        ExposedResourceBundleMessageSource messageSource = new ExposedResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames(StringUtils.commaDelimitedListToStringArray(
                StringUtils.trimAllWhitespace(clientBasenames)));
        return messageSource;
    }

    @Bean("n2oMessageSourceAccessor")
    @ConditionalOnMissingBean(name = "n2oMessageSourceAccessor")
    public MessageSourceAccessor messageSourceAccessor(@Qualifier("n2oMessageSource") MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }
}
