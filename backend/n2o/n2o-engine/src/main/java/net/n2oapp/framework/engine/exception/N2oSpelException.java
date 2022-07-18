package net.n2oapp.framework.engine.exception;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;

@Getter
@Setter
public class N2oSpelException extends N2oException {

    private static final String DEFAULT_MESSAGE = "Spel expression conversion error with %s from metadata %s";
    private static final String DEFAULT_FIELD_MESSAGE = "Spel expression conversion error with %s of field %s from metadata %s";

    private String file;
    private String mapping;
    private String fieldId;

    public N2oSpelException(N2oSpelException e, String file) {
        super(e.getFieldId() != null
                ? String.format(DEFAULT_FIELD_MESSAGE, e.getMapping(), e.getFieldId(), file)
                : String.format(DEFAULT_MESSAGE, e.getMapping(), file), e.getCause());
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
}
