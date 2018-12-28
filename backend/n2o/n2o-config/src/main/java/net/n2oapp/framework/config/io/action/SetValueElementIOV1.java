package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.event.action.SetValueExpressionAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись установление значений в модели
 */
@Component
public class SetValueElementIOV1 extends AbstractActionElementIOV1<SetValueExpressionAction> {



    @Override
    public void io(Element e, SetValueExpressionAction sv, IOProcessor p) {
        super.io(e, sv, p);
        p.attribute(e,"target-field-id", sv::getTargetFieldId, sv::setTargetFieldId);
        p.text(e,sv::getExpression,sv::setExpression);
    }

    @Override
    public String getElementName() {
        return "set-value";
    }

    @Override
    public Class<SetValueExpressionAction> getElementClass() {
        return SetValueExpressionAction.class;
    }
}
