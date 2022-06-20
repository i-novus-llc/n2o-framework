package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись базовых свойств источников данных
 */
public abstract class AbstractDatasourceIO<T extends N2oAbstractDatasource> implements TypedElementIO<T> {

    public void io(Element e, T ds, IOProcessor p) {
        p.attribute(e, "id", ds::getId, ds::setId);
    }

    public static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/datasource-1.0");

    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }
}
