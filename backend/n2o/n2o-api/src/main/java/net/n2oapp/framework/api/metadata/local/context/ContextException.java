package net.n2oapp.framework.api.metadata.local.context;

/**
 * User: iryabov
 * Date: 13.12.13
 * Time: 12:44
 */
public class ContextException extends Exception {
    public ContextException() {
    }

    public ContextException(String message) {
        super(message);
    }

    public ContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextException(Throwable cause) {
        super(cause);
    }

    public ContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
