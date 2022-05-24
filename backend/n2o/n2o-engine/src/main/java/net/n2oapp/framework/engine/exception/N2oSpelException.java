package net.n2oapp.framework.engine.exception;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;

@Getter
@Setter
public class N2oSpelException extends N2oException {

    private String file;
    private String mapping;
    private String fieldId;

    public N2oSpelException(String mapping, Throwable cause) {
        super("n2o.summary.spelError", cause);
        this.mapping = mapping;
    }

    public N2oSpelException(String fieldId, String mapping, Throwable cause) {
        super("n2o.summary.fieldSpelError", cause);
        this.fieldId = fieldId;
        this.mapping = mapping;
    }
}
