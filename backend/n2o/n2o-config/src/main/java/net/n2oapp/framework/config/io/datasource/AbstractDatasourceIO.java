package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
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

    protected void dependency(Element e, N2oDatasource.Dependency t, IOProcessor p) {
        p.attribute(e, "on", t::getOn, t::setOn);
    }

    protected void fetch(Element e, N2oDatasource.FetchDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
    }

    protected void copy(Element e, N2oDatasource.CopyDependency c, IOProcessor p) {
        dependency(e, c, p);
        p.attribute(e, "target-field-id", c::getTargetFieldId, c::setTargetFieldId);
        p.attributeEnum(e, "target-model", c::getTargetModel, c::setTargetModel, ReduxModel.class);
        p.attributeEnum(e, "source-model", c::getSourceModel, c::setSourceModel, ReduxModel.class);
        p.attribute(e, "source-field-id", c::getSourceFieldId, c::setSourceFieldId);
        p.attributeBoolean(e, "submit", c::getSubmit, c::setSubmit);
        p.attributeBoolean(e, "apply-on-init", c::getApplyOnInit, c::setApplyOnInit);
    }
}
