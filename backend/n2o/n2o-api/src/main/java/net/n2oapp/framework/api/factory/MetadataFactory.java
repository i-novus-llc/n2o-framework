package net.n2oapp.framework.api.factory;

/**
 * Фабрика движков сборки метаданных
 */
public interface MetadataFactory<G> {

    MetadataFactory<G> add(G... g);
}
