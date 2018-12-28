package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oDynamicSwitch;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author V. Alexeev.
 */
public class DynamicSwitchReader<T extends NamespaceUriAware> implements TypedElementReader<N2oDynamicSwitch> {

    private final NamespaceReaderFactory extensionReaderFactory;
    private final String type;
    private final Class<T> typeClass;


    public DynamicSwitchReader(NamespaceReaderFactory extensionReaderFactory, String type, Class<T> typeClass) {
        this.extensionReaderFactory = extensionReaderFactory;
        this.type = type;
        this.typeClass = typeClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public N2oDynamicSwitch<T> read(Element element) {
        N2oDynamicSwitch<T> sw = new N2oDynamicSwitch<>();
        List<SimpleEntry<String, T>> cases = ReaderJdomUtil.getChildrenAsList(element, "switch", "case", (e) -> {
            String key = ReaderJdomUtil.getAttributeString(e, "value");
            T[] children = ReaderJdomUtil.getChildren(e, null, extensionReaderFactory, type, typeClass);
            if (children == null || children.length == 0) {
                return null;
            }
            return new SimpleEntry(key, children[0]);
        });
        if (cases != null) {
            Map<String, T> casesMap = new HashMap<>();
            cases.stream().filter(Objects::nonNull).forEach(entry -> casesMap.put(entry.getKey(), entry.getValue()));
            sw.setCases(casesMap);
        }
        T defaultValue = ReaderJdomUtil.getChild(element, "switch", "default", (e) -> {
            T[] children = ReaderJdomUtil.getChildren(e, null, extensionReaderFactory, null, typeClass);
            return children == null || children.length == 0 ? null : children[0];
        });
        sw.setDefaultCase(defaultValue);
        sw.setValueFieldId(ReaderJdomUtil.getElementAttributeString(element, "switch", "value-field-id"));
        return sw;
    }

    @Override
    public Class<N2oDynamicSwitch> getElementClass() {
        return N2oDynamicSwitch.class;
    }

    @Override
    public String getElementName() {
        return "switch";
    }
}
