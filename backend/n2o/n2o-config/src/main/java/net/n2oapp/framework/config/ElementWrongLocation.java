package net.n2oapp.framework.config;

import net.n2oapp.framework.api.metadata.reader.CurrentElementHolder;

public class ElementWrongLocation extends IncorrectXmlException {

    public ElementWrongLocation(Object type) {
        super(type, String.format("Ошибка чтения %s.%s. Элемент <%s> не поддерживается внутри <%s>",
                CurrentElementHolder.getSourceInfo().getId(),
                xml.get(CurrentElementHolder.getSourceInfo().getBaseSourceClass()),
                CurrentElementHolder.getElement().getName(),
                CurrentElementHolder.getElement().getParentElement().getName()));
    }
}
