package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Метаданная, имеющая знание о URI схемы
 */
public interface SourceComponent extends Source, NamespaceUriAware {
}
