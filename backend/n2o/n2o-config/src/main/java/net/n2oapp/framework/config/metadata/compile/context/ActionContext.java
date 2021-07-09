package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;

import java.util.HashMap;
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
    private String parentPageId;
    private String parentWidgetId;
    private String failAlertWidgetId;
    private String successAlertWidgetId;
    private String messagesForm;
    private RedirectSaga redirect;
    private RefreshSaga refresh;
    private boolean messageOnSuccess = true;
    private boolean messageOnFail = true;
    private MessagePosition messagePosition;
    private MessagePlacement messagePlacement;
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

    public void setRedirect(RedirectSaga redirect) {
        if (redirect == null)
            return;
        this.redirect = new RedirectSaga();
        this.redirect.setPath(redirect.getPath());
        this.redirect.setPathMapping(new HashMap<>(redirect.getPathMapping()));
        this.redirect.setQueryMapping(new HashMap<>(redirect.getQueryMapping()));
        this.redirect.setTarget(redirect.getTarget());
        this.redirect.setServer(redirect.isServer());
    }

    public void setRefresh(RefreshSaga refresh) {
        if (refresh == null)
            return;
        this.refresh = new RefreshSaga();
        if (refresh.getOptions() != null && refresh.getOptions().getWidgetId() != null) {
            RefreshSaga.Options options = new RefreshSaga.Options();
            options.setWidgetId(refresh.getOptions().getWidgetId());
            this.refresh.setOptions(options);
        }
        this.refresh.setType(refresh.getType());
    }
}
