package net.n2oapp.framework.engine.validation.engine;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;

@Getter
@Setter
public class FailInfo {
    public SeverityType severity;
    public N2oValidation.ServerMoment moment;
    public String fieldId;
    public String message;
    public String messageTitle;
    public String validationId;
    public String validationClass;
    public String messageForm;
    public N2oDialog dialog;
}
