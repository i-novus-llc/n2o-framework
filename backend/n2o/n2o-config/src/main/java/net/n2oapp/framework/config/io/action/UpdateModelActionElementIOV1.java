package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.UpdateModelAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия выполнения запроса за выборкой данных
 */
@Component
public class UpdateModelActionElementIOV1  implements NamespaceIO<UpdateModelAction> {
    @Override
    public void io(Element e, UpdateModelAction um, IOProcessor p) {
        p.attributeBoolean(e, "validate", um::getValidate, um::setValidate);
        p.attribute(e,"src",um::getSrc, um::setSrc);
        p.attribute(e,"query-id",um::getQueryId, um::setQueryId);
        p.attribute(e,"target-field-id",um::getTargetFieldId, um::setTargetFieldId);
        p.attribute(e,"value-field-id",um::getValueFieldId, um::setValueFieldId);
    }

    @Override
    public String getElementName() {
        return "execute-query";
    }

    @Override
    public Class<UpdateModelAction> getElementClass() {
        return UpdateModelAction.class;
    }


    @Override
    public String getNamespaceUri() {
        return ActionIOv1.NAMESPACE.getURI();
    }
}
