package net.n2oapp.framework.engine.validation.engine;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;

@Getter
@Setter
public class FailInfo {
    public SeverityTypeEnum severity;
    public N2oValidation.ServerMomentEnum moment;
    public String fieldId;
    public String message;
    public String messageTitle;
    public String validationId;
    public String validationClass;
    public String messageForm;
}
