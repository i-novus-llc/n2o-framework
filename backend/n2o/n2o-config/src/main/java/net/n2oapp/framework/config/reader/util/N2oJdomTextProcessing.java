package net.n2oapp.framework.config.reader.util;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.properties.StaticProperties;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * User: operehod
 * Date: 14.10.2014
 * Time: 11:34
 */
@Deprecated
public class N2oJdomTextProcessing implements JdomTextProcessing {
    private MessageSourceAccessor messageSourceAccessor;

    public N2oJdomTextProcessing(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public N2oJdomTextProcessing() {
    }

    @Override
    public String process(String text) {
        if (text == null) {
            return null;
        }
        String resolve = StringUtils.resolveProperties(text, StaticProperties::get);
        if (messageSourceAccessor != null)
            resolve = StringUtils.resolveProperties(resolve, messageSourceAccessor::getMessage);
        return resolve;
    }
}
