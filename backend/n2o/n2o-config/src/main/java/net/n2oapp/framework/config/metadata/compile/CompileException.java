package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.exception.N2oException;

public class CompileException extends N2oException {
    public CompileException() {
    }

    public CompileException(Throwable cause) {
        super(cause);
    }

    public CompileException(String message) {
        super(message);
    }

    public CompileException(String message, Throwable cause) {
        super(message, cause);
    }
}
