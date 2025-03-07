package net.n2oapp.framework.config;

import net.n2oapp.framework.api.metadata.reader.CurrentElementHolder;
import org.jdom2.Element;

import static net.n2oapp.framework.config.util.MetadataUtil.XML_BY_METADATA;

public class ElementWrongLocation extends IncorrectXmlException {

    private static final String ERROR_TEMPLATE = "Ошибка чтения %s.%s. Элемент <%s> не поддерживается внутри <%s>";

    public ElementWrongLocation(Object type) {
        super(type, String.format(ERROR_TEMPLATE,
                CurrentElementHolder.getSourceInfo().getId(),
                XML_BY_METADATA.get(CurrentElementHolder.getSourceInfo().getBaseSourceClass()),
                CurrentElementHolder.getElement().getName(),
                CurrentElementHolder.getElement().getParentElement().getName()));
    }

    public ElementWrongLocation(Element element) {
        super(element.getName(), String.format(ERROR_TEMPLATE,
                CurrentElementHolder.getSourceInfo().getId(),
                XML_BY_METADATA.get(CurrentElementHolder.getSourceInfo().getBaseSourceClass()),
                element.getName(),
                element.getParentElement().getName()));
    }
}
