package net.n2oapp.engine.factory;

public class EngineNotUniqueException extends RuntimeException {
    private Object type;

    /**
     * Создать исключение по типу
     * @param type Тип, по которому не был найден движок
     */
    public EngineNotUniqueException(Object type) {
        super(String.format("Found more then one engine for [%s]", type));
        this.type = type;
    }

    public EngineNotUniqueException(Class factoryClass, Object type) {
        super(String.format("Factory %s has found more then one engine by %s", factoryClass.getSimpleName(), type));
        this.type = type;
    }

    public Object getType() {
        return type;
    }
}
