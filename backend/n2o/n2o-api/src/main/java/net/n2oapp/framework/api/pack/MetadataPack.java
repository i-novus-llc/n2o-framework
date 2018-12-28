package net.n2oapp.framework.api.pack;

/**
 * Набор сервисов для сборки определенных коллекций метаданных
 */
public interface MetadataPack<B> {

    /**
     * Добавить набор сервисов в конструктор
     *
     * @param b Конструктор сборки метаданных
     */
    void build(B b);

}
