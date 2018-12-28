package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElementInteger;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElementString;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 04.06.13
 * Time: 10:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class WidgetXmlPersister<E extends N2oWidget> extends AbstractN2oMetadataPersister<E> {

    public WidgetXmlPersister() {
        super("http://n2oapp.net/framework/config/schema/n2o-widget-3.0", "wgt");
    }

    public WidgetXmlPersister(String namespaceUri, String namespacePrefix) {
        super(namespaceUri, namespacePrefix );
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }


    @Override
    public Element persist(E entity, Namespace parentNamespace) {
        Namespace namespace = Namespace.getNamespace(namespacePrefix, namespaceUri);
        return getWidget(entity, namespace);
    }

    public abstract Element getWidget(E n2o, Namespace namespace);


    protected void persistWidget(Element element, N2oWidget n2o, Namespace namespace) {
        setElementString(element, "name", n2o.getName());
        setElementString(element, "query-id", n2o.getQueryId());
		setElementString(element, "object-id", n2o.getObjectId());
        setElementInteger(element, "size", n2o.getSize());
        setAttribute(element, "src", n2o.getSrc());
        setAttribute(element, "ref-id", n2o.getRefId());
        setAttribute(element, "customize", n2o.getCustomize());
        setAttribute(element, "css-class", n2o.getCssClass());
        setAttribute(element, "style", n2o.getStyle());
        setElementString(element, "default-values-query-id", n2o.getDefaultValuesQueryId());
    }

    @Override
    public void setPersisterFactory(NamespacePersisterFactory<E, NamespacePersister<E>> persisterFactory) {
        super.setPersisterFactory(persisterFactory);
    }
}
