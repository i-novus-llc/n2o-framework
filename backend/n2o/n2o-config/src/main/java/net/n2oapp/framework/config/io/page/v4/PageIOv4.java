package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import org.jdom2.Namespace;

/**
 * Страница версии 4.0
 */
public interface PageIOv4 extends NamespaceUriAware, BaseElementClassAware<N2oPage> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/page-4.0");


    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oPage> getBaseElementClass() {
        return N2oPage.class;
    }

}
