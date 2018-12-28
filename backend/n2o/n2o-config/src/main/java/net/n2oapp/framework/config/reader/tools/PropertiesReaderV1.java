package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.framework.api.data.DomainProcessor;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * User: operhod
 * Date: 30.01.14
 * Time: 13:42
 */
public class PropertiesReaderV1 {
    private static final PropertiesReaderV1 instance = new PropertiesReaderV1();

    private PropertiesReaderV1() {
    }

    public static PropertiesReaderV1 getInstance() {
        return instance;
    }

    public Map<String, Object> readFromPropertiesElement(Element element, Namespace namespace) {
        Map<String, Object> res = null;
        if (element != null) {
            List<Element> properties = element.getChildren("property", namespace);
            res = new LinkedHashMap<>();
            for (Element property : properties) {
                String key = getAttributeString(property, "key");
                String value = getAttributeString(property, "value");
                String domain = getAttributeString(property, "domain");
                Object object = DomainProcessor.getInstance().doDomainConversion(domain, value);
                res.put(key, object);
            }
        }
        return res;
    }

    public Map<String, Object> read(Element element, Namespace namespace) {
        if (element == null)
            return null;
        Element propertiesElement = element.getChild("properties", namespace);
        return readFromPropertiesElement(propertiesElement, namespace);
    }


}
