package net.n2oapp.framework.api.exception;

/**
 * Исключение, бросаемое при попытке генерации кнопок без указания 'object-id'
 */
public class GenerateWithoutObjectException extends N2oException {
    private final String type;
    private final String tag;

    public GenerateWithoutObjectException(String type) {
        this(type, null);
    }

    public GenerateWithoutObjectException(String type, String tag) {
        this.type = type;
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Не удалось определить объект для генерации кнопок с помощью атрибута 'generate=\"%s\"' в \"%s\". Укажите 'object-id' в источнике данных или в его выборке",
                type, tag != null ? tag : "неизвестный тег");
    }
}