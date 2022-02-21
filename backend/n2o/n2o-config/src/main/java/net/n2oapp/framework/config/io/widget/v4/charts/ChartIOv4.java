package net.n2oapp.framework.config.io.widget.v4.charts;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAbstractChart;
import org.jdom2.Namespace;

/**
 * Интерфейс графиков версии 4
 */
public interface ChartIOv4 extends NamespaceUriAware, BaseElementClassAware<N2oAbstractChart> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-chart-4.0");

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
    default Class<N2oAbstractChart> getBaseElementClass() {
        return N2oAbstractChart.class;
    }
}
