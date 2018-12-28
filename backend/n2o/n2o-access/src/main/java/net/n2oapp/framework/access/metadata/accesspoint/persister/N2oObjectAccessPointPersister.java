package net.n2oapp.framework.access.metadata.accesspoint.persister;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElementString;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setEmptyElement;

@Component
public class N2oObjectAccessPointPersister extends N2oAccessPointPersister<N2oObjectAccessPoint> {

    @Override
    public Element persist(N2oObjectAccessPoint accessPoint, Namespace namespaceElement) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "object-id", accessPoint.getObjectId());
        if (accessPoint.getAction() != null) {
            setAttribute(rootElement, "actions", accessPoint.getAction());
        }
        if (accessPoint.getAccessFilters() != null) {
            for (N2oPreFilter accessFilter : accessPoint.getAccessFilters()) {
                Element filterElement = setEmptyElement(rootElement, "filter");
                persistFilter(filterElement, accessFilter);
            }
        }
        return rootElement;
    }

    private void persistFilter(Element element, N2oPreFilter filter) {
        setAttribute(element, "field-id", filter.getFieldId());
        setAttribute(element, "type", filter.getType());
        if (filter.getValue() != null) {
            setAttribute(element, "value", filter.getValue());
        }
        if (filter.getValues() != null) {
            for (String value : filter.getValues()) {
                setElementString(element, "value", value);
            }
        }
    }

    @Override
    public Class<N2oObjectAccessPoint> getElementClass() {
        return N2oObjectAccessPoint.class;
    }

    @Override
    public String getElementName() {
        return "object-access";
    }
}
