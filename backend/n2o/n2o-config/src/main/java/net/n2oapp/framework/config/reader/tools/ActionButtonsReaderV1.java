package net.n2oapp.framework.config.reader.tools;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.N2oActionButton;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

/**
 * Считывает кнопки внутри контроллов.
 */
public class ActionButtonsReaderV1 extends ControlActionReaderV1<N2oActionButton> {

    @Override
    public N2oActionButton readControlAction(Element element, Namespace namespace) {
        N2oActionButton res = new N2oActionButton();
        res.setIcon(ReaderJdomUtil.getAttributeString(element, "icon"));
        res.setType(ReaderJdomUtil.getAttributeEnum(element, "type", LabelType.class));
        return res;
    }

    @Override
    public Class<N2oActionButton> getElementClass() {
        return N2oActionButton.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }
}
