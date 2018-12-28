package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.SetValueExpressionAction;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getText;

/**
 * Считывает событие set-value-expression
 */
@Component
public class SetValueExpressionEventReaderV1 extends AbstractN2oEventXmlReader<SetValueExpressionAction> {

    @Override
    public String getElementName() {
        return "set-value-expression";
    }

    @Override
    public Class<SetValueExpressionAction> getElementClass() {
        return SetValueExpressionAction.class;
    }

    @Override
    public SetValueExpressionAction read(Element element) {
        if (element == null) return null;
        SetValueExpressionAction res = new SetValueExpressionAction();
        res.setExpression(getText(element));
        return res;
    }
}
