package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import org.jdom2.Element;

/**
 * Чтение\запись базовых свойств источников данных
 */
public abstract class AbstractDatasourceIO<T extends N2oAbstractDatasource> implements TypedElementIO<T>, NamespaceIO<T>, DatasourceIOv1 {

    public void io(Element e, T ds, IOProcessor p) {
        p.attribute(e, "id", ds::getId, ds::setId);
    }

    protected void fetch(Element e, N2oDatasource.FetchDependency t, IOProcessor p) {
        p.attribute(e, "on", t::getOn, t::setOn);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
    }
}
