package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.event.N2oAbstractEvent;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.datasource.DatasourceIOv1;
import net.n2oapp.framework.config.io.event.AbstractEventIO;
import net.n2oapp.framework.config.io.region.v3.RegionIOv3;
import net.n2oapp.framework.config.io.toolbar.v2.ToolbarIOv2;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * Чтение\запись базовой страницы версии 4.0
 */
public abstract class BasePageElementIOv4<T extends N2oBasePage> extends AbstractPageElementIOv4<T> {
    private static final Namespace regionDefaultNamespace = RegionIOv3.NAMESPACE;
    private static final Namespace actionDefaultNamespace = ActionIOv2.NAMESPACE;

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.children(e, "actions", "action", m::getActions, m::setActions, ActionBar::new, this::action);
        p.childAttributeEnum(e, "actions", "generate", m::getActionGenerate, m::setActionGenerate, GenerateType.class);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIOv2());
        p.anyChildren(e, "datasources", m::getDatasources, m::setDatasources, p.anyOf(N2oAbstractDatasource.class), DatasourceIOv1.NAMESPACE);
        p.anyChildren(e, "events", m::getEvents, m::setEvents, p.anyOf(N2oAbstractEvent.class), AbstractEventIO.NAMESPACE);
    }

    private void action(Element e, ActionBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.attribute(e, "name", a::getLabel, a::setLabel);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.attribute(e, "icon", a::getIcon, a::setIcon);
        p.attribute(e, "visible", a::getVisible, a::setVisible);
        p.attribute(e, "enabled", a::getEnabled, a::setEnabled);
        p.anyChildren(e, null, a::getN2oActions, a::setN2oActions, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    public Namespace getRegionDefaultNamespace() {
        return regionDefaultNamespace;
    }
}
