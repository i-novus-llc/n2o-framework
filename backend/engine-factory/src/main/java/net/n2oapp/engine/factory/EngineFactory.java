package net.n2oapp.engine.factory;

/**
 * Фабрика движков
 * @param <T> Тип движка
 * @param <G> Движок
 */
public interface EngineFactory<T, G> {
    /**
     * Произвести движок
     * @param type Тип движка
     * @throws EngineNotFoundException Если по переданному типу движок не был найден
     * @throws EngineNotUniqueException Если по переданному типу было найдено несколько движков
     * @return Движок
     */
    G produce(T type);

}
