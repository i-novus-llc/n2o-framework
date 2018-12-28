package net.n2oapp.framework.api.metadata;


import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Базовая модель скомпилированной метаданной
 */
public interface CompiledMetadata
        extends Compiled, IdAware {

    /**
     * Базовый класс компилированной метаданной
     */
    @Deprecated
    default Class<? extends CompiledMetadata> getCompiledBaseClass() {
        return getClass();
    }

    /**
     * Класс исходной метаданной
     */
    @Deprecated
    default Class<? extends SourceMetadata> getSourceClass() {
        return null;
    }


}
