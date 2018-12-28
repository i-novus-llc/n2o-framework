package net.n2oapp.framework.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;

import java.util.Collection;

public class InvocationProcessorMock implements InvocationProcessor {
    @Override
    public DataSet invoke(N2oInvocation invocation, DataSet inDataSet, Collection<? extends InvocationParameter> inParameters, Collection<? extends InvocationParameter> outParameters) {
        return null;
    }
}
