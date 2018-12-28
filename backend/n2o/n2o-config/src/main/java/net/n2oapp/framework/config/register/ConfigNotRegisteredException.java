package net.n2oapp.framework.config.register;

/**
 * User: iryabov
 * Date: 13.12.13
 * Time: 13:07
 */
public class ConfigNotRegisteredException extends RuntimeException {
    public ConfigNotRegisteredException() {
    }

    public ConfigNotRegisteredException(String message) {
        super(message);
    }

    public ConfigNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNotRegisteredException(Throwable cause) {
        super(cause);
    }

    public ConfigNotRegisteredException(String message, Throwable cause, boolean enableSuppression,
                                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
