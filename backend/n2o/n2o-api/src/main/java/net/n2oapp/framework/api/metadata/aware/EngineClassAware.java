package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание о классе движка производимого фабрикой
 * @param <G> Тип движка
 */
public interface EngineClassAware<G> {

    /**
     * Получить класс движка
     *
     * @return Класс движка
     */
    Class<G> getEngineClass();
}
