package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;

public class MetaTypeNotFoundException extends N2oException {

    public MetaTypeNotFoundException(String sourceType) {
        super(String.format("Тип метаданной '%s' не зарегистрирован", sourceType));
    }

    public MetaTypeNotFoundException(Class<? extends Source> sourceClass) {
        super(String.format("Класс метаданной '%s' не зарегистрирован", sourceClass.getSimpleName()));
    }
}