package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import org.jdom2.Namespace;

/**
 * Интерфейс  виджета версии 4
 */
public interface WidgetIOv4 extends NamespaceUriAware, BaseElementClassAware<N2oWidget> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-widget-4.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default void setNamespaceUri(String namespaceUri) {
    }

    @Override
    default Class<N2oWidget> getBaseElementClass() {
        return N2oWidget.class;
    }
}
