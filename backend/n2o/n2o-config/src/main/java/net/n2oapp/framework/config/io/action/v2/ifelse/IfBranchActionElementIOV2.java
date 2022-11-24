package net.n2oapp.framework.config.io.action.v2.ifelse;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oIfBranchAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись элемента if оператора if-else
 */
@Component
public class IfBranchActionElementIOV2 extends ConditionBranchElementIOV2<N2oIfBranchAction> {
    @Override
    public Class<N2oIfBranchAction> getElementClass() {
        return N2oIfBranchAction.class;
    }

    @Override
    public String getElementName() {
        return "if";
    }

    @Override
    public void io(Element e, N2oIfBranchAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "test", a::getTest, a::setTest);
        p.attribute(e, "datasource", a::getDatasourceId, a::setDatasourceId);
        p.attributeEnum(e, "model", a::getModel, a::setModel, ReduxModel.class);
    }
}
