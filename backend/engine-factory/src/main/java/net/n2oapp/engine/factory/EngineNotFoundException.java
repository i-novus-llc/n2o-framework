package net.n2oapp.engine.factory;

/**
 * Исключение, выбрасываемое фабрикой движков, когда по типу не был найден движок.
 */
public class EngineNotFoundException extends RuntimeException {
    private final Object type;

    /**
     * Создать исключение по типу
     *
     * @param type Тип, по которому не был найден движок
     */
    public EngineNotFoundException(Object type) {
        super("Engine for '" + type + "' not found");
        this.type = type;
    }

    /**
     * Создать исключение по типу
     *
     * @param type    Тип, по которому не был найден движок
     * @param message Сообщение ошибки
     */
    public EngineNotFoundException(Object type, String message) {
        super(message);
        this.type = type;
    }

    /**
     * Создать исключение по типу
     *
     * @param type Тип, по которому не был найден движок
     */
    public EngineNotFoundException(Class factoryClass, Object type) {
        super(String.format("Factory %s has not found engine by %s", factoryClass.getSimpleName(), type));
        this.type = type;
    }

    /**
     * @return Тип, по которому не был найден движок
     */
    public Object getType() {
        return type;
    }
}
