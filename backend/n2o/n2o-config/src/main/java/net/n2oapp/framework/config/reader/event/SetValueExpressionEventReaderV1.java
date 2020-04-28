package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oSetValueAction;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getText;

/**
 * Считывает событие set-value-expression
 */
@Component
public class SetValueExpressionEventReaderV1 extends AbstractN2oEventXmlReader<N2oSetValueAction> {

    @Override
    public String getElementName() {
        return "set-value-expression";
    }

    @Override
    public Class<N2oSetValueAction> getElementClass() {
        return N2oSetValueAction.class;
    }

    @Override
    public N2oSetValueAction read(Element element) {
        if (element == null) return null;
        N2oSetValueAction res = new N2oSetValueAction();
        res.setExpression(getText(element));
        return res;
    }
}
