package net.n2oapp.framework.config.reader;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

/**
 * User: operhod
 * Date: 20.01.14
 * Time: 13:04
 */
public class RestErrorMappingReader {

    public static RestErrorMapping read(Element errorMapping, Namespace namespace) {
        if (errorMapping == null) return null;
        return new RestErrorMapping(
                ReaderJdomUtil.getAttributeString(errorMapping.getChild("message", namespace), "param-name"),
                ReaderJdomUtil.getAttributeString(errorMapping.getChild("detailed-message", namespace), "param-name"),
                ReaderJdomUtil.getAttributeString(errorMapping.getChild("stack-trace", namespace), "param-name")
        );
    }


}
