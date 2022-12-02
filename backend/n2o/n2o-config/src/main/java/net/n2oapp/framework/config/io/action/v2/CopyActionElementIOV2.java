package net.n2oapp.framework.config.io.action.v2;


import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.PageRef;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.CopyMode;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись копирования модели
 */
@Component
public class CopyActionElementIOV2 extends AbstractActionElementIOV2<N2oCopyAction> {

    @Override
    public String getElementName() {
        return "copy";
    }

    @Override
    public Class<N2oCopyAction> getElementClass() {
        return N2oCopyAction.class;
    }

    @Override
    public void io(Element e, N2oCopyAction m, IOProcessor p) {
        p.attributeEnum(e, "source-model", m::getSourceModel, m::setSourceModel, ReduxModel.class);
        p.attribute(e, "source-datasource", m::getSourceDatasourceId, m::setSourceDatasourceId);
        p.attribute(e, "source-field-id", m::getSourceFieldId, m::setSourceFieldId);
        p.attributeEnum(e, "target-model", m::getTargetModel, m::setTargetModel, ReduxModel.class);
        p.attribute(e, "target-datasource", m::getTargetDatasourceId, m::setTargetDatasourceId);
        p.attribute(e, "target-field-id", m::getTargetFieldId, m::setTargetFieldId);
        p.attributeEnum(e, "target-page", m::getTargetPage, m::setTargetPage, PageRef.class);
        p.attributeBoolean(e, "close-on-success", m::getCloseOnSuccess, m::setCloseOnSuccess);
        p.attributeEnum(e, "mode", m::getMode, m::setMode, CopyMode.class);
    }
}
