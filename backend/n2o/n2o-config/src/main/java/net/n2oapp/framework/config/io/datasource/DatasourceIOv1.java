package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import org.jdom2.Namespace;

/**
 * Интерфейс источника данных версии 1.0
 */
public interface DatasourceIOv1 extends NamespaceUriAware, BaseElementClassAware<N2oAbstractDatasource> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/datasource-1.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oAbstractDatasource> getBaseElementClass() {
        return N2oAbstractDatasource.class;
    }
}
