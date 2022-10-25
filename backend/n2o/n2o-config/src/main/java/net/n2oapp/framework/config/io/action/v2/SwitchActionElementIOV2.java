package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oSwitchAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия switch версии 2.0
 */
@Component
public class SwitchActionElementIOV2 extends AbstractActionElementIOV2<N2oSwitchAction> {
    @Override
    public Class<N2oSwitchAction> getElementClass() {
        return N2oSwitchAction.class;
    }

    @Override
    public String getElementName() {
        return "switch";
    }

    @Override
    public void io(Element e, N2oSwitchAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "value-field-id", a::getValueFieldId, a::setValueFieldId);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
        p.anyChildren(e, null, a::getCases, a::setCases, p.oneOf(N2oSwitchAction.AbstractCase.class)
                .add("case", N2oSwitchAction.Case.class, this::caseIo)
                .add("default", N2oSwitchAction.DefaultCase.class, this::abstractCaseIO));
    }

    private void caseIo(Element e, N2oSwitchAction.Case m, IOProcessor p) {
        abstractCaseIO(e, m, p);
        p.attribute(e, "value", m::getValue, m::setValue);
    }

    private void abstractCaseIO(Element e, N2oSwitchAction.AbstractCase m, IOProcessor p) {
        ActionsAwareIO.action(e, m, null, getNamespace(), p);
    }
}
