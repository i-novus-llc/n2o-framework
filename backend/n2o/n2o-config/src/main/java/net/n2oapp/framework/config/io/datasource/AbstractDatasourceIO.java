package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись базовых свойств источников данных
 */
@Component
public abstract class AbstractDatasourceIO<T extends N2oAbstractDatasource> extends ComponentIO<T> {

    static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/datasource-1.0");

    public void io(Element e, T ds, IOProcessor p) {
        p.attribute(e, "id", ds::getId, ds::setId);
    }

    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }
}
