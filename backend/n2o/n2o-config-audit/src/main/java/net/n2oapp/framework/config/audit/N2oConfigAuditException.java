package net.n2oapp.framework.config.audit;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Исключение, возникающее при работе аудита конфигураций
 */
public class N2oConfigAuditException extends N2oException {

    public N2oConfigAuditException(Throwable cause) {
        super(cause);
    }

    public N2oConfigAuditException(String message) {
        super(message);
    }

    public N2oConfigAuditException(String message, Throwable cause) {
        super(message, cause);
    }
}
