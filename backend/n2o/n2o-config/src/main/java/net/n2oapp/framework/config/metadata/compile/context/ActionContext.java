package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.saga.LoadingSaga;
import net.n2oapp.framework.api.metadata.meta.saga.PollingSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Контекст сборки объекта под конкретную операцию
 */
@Getter
@Setter
public class ActionContext extends ObjectContext {
    private String operationId;
    private List<Validation> validations;
    private String parentPageId;
    private String parentClientWidgetId;
    private String parentSourceDatasourceId;
    /**
     * Форма, в которой находятся валидации
     */
    private String messagesForm;
    private String clearDatasource;
    private String pollingEndCondition;
    private PollingSaga polling;
    private LoadingSaga loading;
    private RedirectSaga redirect;
    private RefreshSaga refresh;
    private boolean messageOnSuccess = true;
    private boolean messageOnFail = true;
    private boolean useFailOut;
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
        this.refresh = refresh;
    }

    @Override
    public boolean isIdentical(CompileContext<CompiledObject, N2oObject> obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        ActionContext that = (ActionContext) obj;

        return super.isIdentical(that) &&
                Objects.equals(operationId, that.operationId) &&
                Objects.equals(validations, that.validations) &&
                Objects.equals(parentPageId, that.parentPageId) &&
                Objects.equals(parentClientWidgetId, that.parentClientWidgetId) &&
                Objects.equals(parentSourceDatasourceId, that.parentSourceDatasourceId) &&
                Objects.equals(messagesForm, that.messagesForm) &&
                Objects.equals(clearDatasource, that.clearDatasource) &&
                Objects.equals(redirect, that.redirect) &&
                Objects.equals(refresh, that.refresh) &&
                messageOnSuccess == that.messageOnSuccess &&
                messageOnFail == that.messageOnFail &&
                useFailOut == that.useFailOut &&
                messagePosition == that.messagePosition &&
                messagePlacement == that.messagePlacement &&
                Objects.equals(operationMapping, that.operationMapping);
    }
}
