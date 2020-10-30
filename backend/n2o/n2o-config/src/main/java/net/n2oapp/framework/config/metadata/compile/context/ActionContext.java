package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;

import java.util.List;
import java.util.Map;

/**
 * Контекст сборки объекта под конкретную операцию
 */
@Getter
@Setter
public class ActionContext extends ObjectContext {
    private String operationId;
    private List<Validation> validations;
    private String parentWidgetId;
    private String failAlertWidgetId;
    private String successAlertWidgetId;
    private String messagesForm;
    private RedirectSaga redirect;
    private RefreshSaga refresh;
    private boolean messageOnSuccess = true;
    private boolean messageOnFail = true;
    /**
     * Маппинг path, query, header, form параметров url к in параметрам операции
     */
    private Map<String, String> operationMapping;

    /**
     * Создать контекст
     * @param objectId    Идентификатор объекта
     * @param operationId Идентификатор операции
     */
    public ActionContext(String objectId, String operationId, String route) {
        super(objectId, route);
        this.operationId = operationId;
    }

}
