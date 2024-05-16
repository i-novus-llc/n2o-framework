package net.n2oapp.framework.ui.controller.action;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.rest.ControllerType;
import net.n2oapp.framework.api.rest.ControllerTypeAware;
import net.n2oapp.framework.api.rest.ValidationDataResponse;
import net.n2oapp.framework.api.ui.ValidationRequestInfo;
import net.n2oapp.framework.api.ui.ValidationResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * Контроллер получения выборки данных
 */
@Controller
public class ValidationController implements ControllerTypeAware {

    private static final Logger logger = LoggerFactory.getLogger(ValidationController.class);
    private InvocationProcessor serviceProvider;
    private DomainProcessor domainProcessor;

    public ValidationController(InvocationProcessor serviceProvider,
                                DomainProcessor domainProcessor) {
        this.serviceProvider = serviceProvider;
        this.domainProcessor = domainProcessor;
    }

    public ValidationDataResponse execute(ValidationRequestInfo requestInfo, ValidationResponseInfo responseInfo) {
        ValidationDataResponse response = new ValidationDataResponse();
        requestInfo.getValidation().validate(requestInfo.getData(), serviceProvider, message -> {
            response.setId(requestInfo.getValidation().getId());
            response.setField(requestInfo.getValidation().getFieldId());
            response.setSeverity(requestInfo.getValidation().getSeverity().name());
            response.setStatus(500);
            response.setText(StringUtils.resolveLinks(requestInfo.getValidation().getMessage(), requestInfo.getData()));
            responseInfo.setSuccess(false);
            logger.error(String.format("For field %s validation fail:%s", response.getField(), response.getText()));
        }, domainProcessor);
        return response;
    }

    @Override
    public ControllerType getControllerType() {
        return ControllerType.validation;
    }
}
