package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;

/**
 * Исходная модель компонента
 */
public interface SourceComponent extends Source, NamespaceUriAware, SrcAware, CssClassAware {

}
