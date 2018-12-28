package net.n2oapp.framework.config.reader.invocation;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;

/**
 * Абстрактная реализация считывания invocation
 */
public abstract class AbstractInvocationReaderV2<T extends N2oInvocation> extends AbstractFactoredReader<T> {

    public AbstractInvocationReaderV2() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0");
    }

}
