package net.n2oapp.framework.config.reader.tools;

import net.n2oapp.framework.api.metadata.control.N2oControlActionLink;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Абстрактный reader действий в контроллах
 */
public abstract class ControlActionReaderV1<T extends N2oControlActionLink> implements TypedElementReader<T> {
    private NamespaceReaderFactory readerFactory;
    public final static Namespace DEFAULT_EVENT_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-event-1.0");

    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    @Override
    public T read(Element element) {
        if (element == null) return null;
        T t = readControlAction(element, element.getNamespace());
        read(t, element, element.getNamespace());
        return t;
    }

    protected abstract T readControlAction(Element element, Namespace namespace);

    protected T read(T t, Element element, Namespace namespace) {
        t.setId(ReaderJdomUtil.getAttributeString(element, "id"));
        t.setLabel(ReaderJdomUtil.getAttributeString(element, "label"));
        t.setKey(ReaderJdomUtil.getAttributeString(element, "key"));
        if (element.getChildren() != null && !element.getChildren().isEmpty()) {
            element.getChildren().forEach(elem -> {
                if (((Element) elem).getName().equals("dependencies")) {
                    t.setVisibilityCondition(ReaderJdomUtil.getChild((Element) elem, "visibility-condition", ConditionReaderV1.getInstance()));
                    t.setEnablingCondition(ReaderJdomUtil.getChild((Element) elem, "enabling-condition", ConditionReaderV1.getInstance()));
                } else {
                    Element eventElement = (Element) elem;
                    if (eventElement != null) {
                        N2oAction event = (N2oAction) readerFactory
                                .produce(eventElement, element.getNamespace(), DEFAULT_EVENT_NAMESPACE_URI).read(eventElement);
                        t.setEvent(event);
                    }
                }
            });

        }
        return t;
    }

}
