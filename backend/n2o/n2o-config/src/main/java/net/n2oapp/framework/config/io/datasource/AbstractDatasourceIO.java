package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v2.ComponentIO;
import org.jdom2.Element;
import org.jdom2.Namespace;

public abstract class AbstractDatasourceIO<T extends N2oAbstractDatasource> extends ComponentIO<T> {

    static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/datasource-1.0");

    @Override
    public void io(Element e, T ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "id", ds::getId, ds::setId);
    }

    @Override
    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }
}
