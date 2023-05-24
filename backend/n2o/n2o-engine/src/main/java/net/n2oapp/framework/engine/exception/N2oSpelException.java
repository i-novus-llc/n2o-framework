package net.n2oapp.framework.engine.exception;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;

@Getter
@Setter
public class N2oSpelException extends N2oException {

    private static final String DEFAULT_MESSAGE = "Spel expression conversion error with %s from metadata %s";
    private static final String DEFAULT_FIELD_MESSAGE = "Spel expression conversion error with %s of field '%s' from metadata %s";
    private static final String DEFAULT_OPERATION_MESSAGE = "Spel expression conversion error with %s of field '%s' in operation '%s' from metadata %s";

    private String file;
    private String mapping;
    private String fieldId;
    private String operationId;

    public N2oSpelException(N2oSpelException e, String file) {
        super(message(e, file), e.getCause());
    }

    public N2oSpelException(String mapping, Throwable cause) {
        super(cause.getMessage(), cause);
        this.mapping = mapping;
    }

    public N2oSpelException(String fieldId, String mapping, Throwable cause) {
        super(cause.getMessage(), cause);
        this.fieldId = fieldId;
        this.mapping = mapping;
    }

    private static String message(N2oSpelException e, String file) {
        if (e.getFieldId() != null)
            if (e.getOperationId() == null) {
                return defaultFieldMessage(e, file);
            } else {
                return defaultOperationMessage(e, file);
            }
        else {
            return defaultMessage(e, file);
        }
    }

    private static String defaultMessage(N2oSpelException e, String file) {
        return String.format(DEFAULT_MESSAGE, e.getMapping(), file) + ". Cause: " + e.getMessage();
    }

    private static String defaultFieldMessage(N2oSpelException e, String file) {
        return String.format(DEFAULT_FIELD_MESSAGE, e.getMapping(), e.getFieldId(), file) + ". Cause: " + e.getMessage();
    }

    private static String defaultOperationMessage(N2oSpelException e, String file) {
        return String.format(DEFAULT_OPERATION_MESSAGE, e.getMapping(), e.getFieldId(), e.getOperationId(), file) + ". Cause: " + e.getMessage();
    }
}
