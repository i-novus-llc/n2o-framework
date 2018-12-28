package net.n2oapp.framework.config.io.action;


import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oCopyAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись копирования модели
 */
@Component
public class CopyActionElementIOV1 extends AbstractActionElementIOV1<N2oCopyAction> {

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
        p.attributeEnum(e,"source-model", m::getSourceModel, m::setSourceModel, ReduxModel.class);
        p.attributeEnum(e,"target-model", m::getTargetModel, m::setTargetModel, ReduxModel.class);
    }
}
