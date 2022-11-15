package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.action.MergeMode;
import net.n2oapp.framework.api.metadata.action.N2oSetValueAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись установление значений в модели
 */
@Component
public class SetValueElementIOV1 extends AbstractActionElementIOV1<N2oSetValueAction> {

    @Override
    public void io(Element e, N2oSetValueAction sv, IOProcessor p) {
        super.io(e, sv, p);
        p.attribute(e, "target-field-id", sv::getTargetFieldId, sv::setTargetFieldId);
        p.attribute(e, "to", sv::getTo, sv::setTo);
        p.attribute(e, "source-widget", sv::getSourceWidget, sv::setSourceWidget);
        p.attribute(e, "source-model", sv::getSourceModel, sv::setSourceModel);
        p.attribute(e, "target-widget", sv::getTargetWidget, sv::setTargetWidget);
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
