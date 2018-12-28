package net.n2oapp.engine.factory;

import java.util.List;

/**
 * Фабрика списка движков
 * @param <T> Тип движка
 * @param <G> Движок
 */
@FunctionalInterface
public interface MultiEngineFactory<T, G> extends EngineFactory<T, G> {

    /**
     * Отобрать список движков по типу
     * @param type Тип движка
     * @return Список движков
     */
    List<G> produceList(T type);

    @Override
    default G produce(T type) {
        List<G> engines = produceList(type);
        if (engines == null || engines.isEmpty())
            throw new EngineNotFoundException(type);
        if (engines.size() > 1)
            throw new EngineNotUniqueException(type);
        return engines.get(0);
    }
}
