package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.util.Collection;

/**
 * Реестр типов метаданных
 */
public interface SourceTypeRegister {

    void add(MetaType metaType);

    void addAll(Collection<MetaType> metaType);

    MetaType get(String sourceType);

    MetaType get(Class<? extends SourceMetadata> sourceClass);

    void clearAll();
}
