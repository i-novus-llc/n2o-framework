package net.n2oapp.framework.config.io.action.v2.ifelse;

import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oElseIfBranchAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись элемента else-if оператора if-else
 */
@Component
public class ElseIfBranchActionElementIOV2 extends ConditionBranchElementIOV2<N2oElseIfBranchAction> {
    @Override
    public Class<N2oElseIfBranchAction> getElementClass() {
        return N2oElseIfBranchAction.class;
    }

    @Override
    public String getElementName() {
        return "else-if";
    }

    @Override
    public void io(Element e, N2oElseIfBranchAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "test", a::getTest, a::setTest);
    }
}
