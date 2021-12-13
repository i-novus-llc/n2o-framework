package net.n2oapp.framework.config.io.fieldset.v4;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import org.jdom2.Namespace;

/**
 * Интерфейс  филдсета версии 4
 */
public interface FieldsetIOv4 extends NamespaceUriAware, BaseElementClassAware<N2oFieldSet> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/fieldset-4.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oFieldSet> getBaseElementClass() {
        return N2oFieldSet.class;
    }
}
