package net.n2oapp.framework.config;

import net.n2oapp.framework.api.metadata.reader.CurrentElementHolder;

public class SchemaNotRegisteredException extends IncorrectXmlException {

    public SchemaNotRegisteredException(Object type) {
        super(type, String.format("Ошибка чтения %s.%s. Схема %s не поддерживается",
                CurrentElementHolder.getSourceInfo().getId(),
                xml.get(CurrentElementHolder.getSourceInfo().getBaseSourceClass()),
                CurrentElementHolder.getElement().getNamespaceURI().substring(CurrentElementHolder.getElement().getNamespaceURI().lastIndexOf('/') + 1)));
    }
}
