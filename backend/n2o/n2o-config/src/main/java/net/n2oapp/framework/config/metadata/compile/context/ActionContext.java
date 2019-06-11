package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;

import java.util.List;

/**
 * Контекст сборки объекта под конкретную операцию
 */
@Getter
@Setter
public class ActionContext extends ObjectContext {
    private String operationId;
    private List<Validation> validations;
    private String failAlertWidgetId;
    private String successAlertWidgetId;
    private String messagesForm;
    private RedirectSaga redirect;
    private boolean messageOnSuccess = true;
    private boolean messageOnFail = true;

    /**
     * Создать контекст
     * @param objectId Идентификатор объекта
     * @param operationId Идентификатор операции
     */
    public ActionContext(String objectId, String operationId, String route) {
        super(objectId, route);
        this.operationId = operationId;
    }

}
