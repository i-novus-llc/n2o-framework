package net.n2oapp.framework.config.io.dataprovider;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.dataprovider.DataProvider;
import org.jdom.Namespace;

/**
 * Стандартный провайдер данных версии 1
 */
public interface DataProviderIOv1 extends NamespaceUriAware, BaseElementClassAware<DataProvider> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/data-provider-1.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<DataProvider> getBaseElementClass() {
        return DataProvider.class;
    }
}
