package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oOutputText;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

@Component
public class N2oOutputTextXmlReaderV1 extends N2oStandardControlReaderV1<N2oOutputText> {
    @Override
    public N2oOutputText read(Element element, Namespace namespace) {
        N2oOutputText outputText = new N2oOutputText();
        getControlFieldDefinition(element, outputText);
        outputText.setType(getAttributeEnum(element, "type", IconType.class));
        outputText.setIcon(getAttributeString(element, "icon"));
        outputText.setPosition(getAttributeEnum(element, "position", Position.class));
        return outputText;
    }

    @Override
    public Class<N2oOutputText> getElementClass() {
        return N2oOutputText.class;
    }

    @Override
    public String getElementName() {
        return "output-text";
    }
}
