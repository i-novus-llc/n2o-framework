package net.n2oapp.framework.config.reader.util;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.properties.reader.PropertiesReader;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.PropertyResolver;

import java.util.Properties;

/**
 * User: operehod
 * Date: 14.10.2014
 * Time: 11:34
 */
@Deprecated
public class N2oJdomTextProcessing implements JdomTextProcessing {
    private MessageSourceAccessor messageSourceAccessor;
    private PropertyResolver systemProperties = new SimplePropertyResolver(new Properties());

    public N2oJdomTextProcessing(MessageSourceAccessor messageSourceAccessor,
                                 PropertyResolver systemProperties) {
        this.messageSourceAccessor = messageSourceAccessor;
        this.systemProperties = systemProperties;
    }

    public N2oJdomTextProcessing() {
    }

    @Override
    public String process(String text) {
        if (text == null) {
            return null;
        }
        String resolve = StringUtils.resolveProperties(text, systemProperties::getProperty);
        if (messageSourceAccessor != null)
            resolve = StringUtils.resolveProperties(resolve, messageSourceAccessor::getMessage);
        return resolve;
    }
}
