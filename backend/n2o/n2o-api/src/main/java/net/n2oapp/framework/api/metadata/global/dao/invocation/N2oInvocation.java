package net.n2oapp.framework.api.metadata.global.dao.invocation;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;

/**
 * Исходная модель операции
 */
public interface N2oInvocation extends Source, NamespaceUriAware {

    String getResultMapping();

    void setResultMapping(String resultMapping);

    String getResultNormalize();

    void setResultNormalize(String resultMapping);
}
