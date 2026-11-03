package net.n2oapp.framework.engine.modules.stack;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.DataProcessing;

public interface DataProcessingErrorCallback {
    void process(DataProcessing module, N2oException exception);
}
