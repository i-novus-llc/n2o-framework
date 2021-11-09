package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.MergeMode;
import net.n2oapp.framework.api.metadata.event.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись установление значений в модели
 */
@Component
public class SetValueElementIOV2 extends AbstractActionElementIOV2<N2oSetValueAction> {

    @Override
    public void io(Element e, N2oSetValueAction sv, IOProcessor p) {
        super.io(e, sv, p);
        p.attribute(e, "target-field-id", sv::getTargetFieldId, sv::setTargetFieldId);
        p.attribute(e, "to", sv::getTo, sv::setTo);
        p.attribute(e, "source-datasource", sv::getSourceDatasource, sv::setSourceDatasource);
        p.attribute(e, "source-model", sv::getSourceModel, sv::setSourceModel);
        p.attribute(e, "target-datasource", sv::getTargetDatasource, sv::setTargetDatasource);
        p.attribute(e, "target-model", sv::getTargetModel, sv::setTargetModel);
        p.attributeEnum(e, "merge-mode", sv::getMergeMode, sv::setMergeMode, MergeMode.class);
        p.text(e, sv::getExpression, sv::setExpression);
    }

    @Override
    public String getElementName() {
        return "set-value";
    }

    @Override
    public Class<N2oSetValueAction> getElementClass() {
        return N2oSetValueAction.class;
    }
}
