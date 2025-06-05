package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.action.N2oConfirmAction;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActionsEnum;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.Collections;
import java.util.List;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

public abstract class AbstractButtonGenerator implements ButtonGenerator {

    protected List<ToolbarItem> build(DefaultActionsEnum action, CompileProcessor p) {
        N2oButton button = new N2oButton();
        button.setId(action.getId());
        button.setLabel(p.getMessage(action.getLabel()));
        button.setIcon(action.getIcon());
        if (action.isContext()) {
            button.setModel(ReduxModelEnum.RESOLVE);
        } else {
            button.setModel(ReduxModelEnum.FILTER);
        }
        switch (action) {
            case DELETE: {
                N2oConfirmAction confirmAction = new N2oConfirmAction();
                N2oInvokeAction invokeAction = new N2oInvokeAction();
                invokeAction.setOperationId(action.getId());
                WidgetScope widgetScope = p.getScope(WidgetScope.class);
                String widgetId = (widgetScope != null && widgetScope.getWidgetId() != null) ? widgetScope.getWidgetId() : "";
                invokeAction.setRoute(normalize("/" + widgetId + "/delete"));
                button.setActions(new N2oAbstractAction[]{confirmAction, invokeAction});
            }
            break;
            case CREATE: {
                N2oShowModal modal = new N2oShowModal();
                CompiledObject object = p.getScope(CompiledObject.class);
                modal.setPageId(object.getId());
                modal.setObjectId(object.getId());
                modal.setPageName(p.getMessage(action.getPageName(), object.getName()));
                modal.setSubmitOperationId(action.getId());
                N2oStandardDatasource datasource = new N2oStandardDatasource();
                datasource.setDefaultValuesMode(DefaultValuesModeEnum.DEFAULTS);
                modal.setDatasources(new N2oStandardDatasource[] { datasource });
                modal.setCloseAfterSubmit(true);
                WidgetScope widgetScope = p.getScope(WidgetScope.class);
                String widgetId = (widgetScope != null && widgetScope.getWidgetId() != null) ? widgetScope.getWidgetId() : "";
                modal.setRoute(normalize("/" + widgetId + "/create"));
                button.setActions(new N2oShowModal[]{modal});
            }
            break;
            case UPDATE: {
                N2oShowModal modal = new N2oShowModal();
                CompiledObject object = p.getScope(CompiledObject.class);
                modal.setPageId(object.getId());
                modal.setObjectId(object.getId());
                modal.setPageName(p.getMessage(action.getPageName(), object.getName()));
                WidgetScope widgetScope = p.getScope(WidgetScope.class);
                String widgetId = (widgetScope != null && widgetScope.getWidgetId() != null) ? widgetScope.getWidgetId() : "";
                String paramName = widgetId + "_" + QuerySimpleField.PK;
                modal.setRoute(normalize("/" + widgetId + "/:" + paramName + "/update"));
                N2oPathParam pathParam = new N2oPathParam();
                pathParam.setName(paramName);
                pathParam.setValue(Placeholders.ref(QuerySimpleField.PK));
                modal.addPathParams(new N2oPathParam[]{pathParam});
                N2oStandardDatasource datasource = new N2oStandardDatasource();
                datasource.setDefaultValuesMode(DefaultValuesModeEnum.QUERY);
                N2oPreFilter masterDetailFilter = new N2oPreFilter();
                masterDetailFilter.setType(FilterTypeEnum.EQ);
                masterDetailFilter.setFieldId(QuerySimpleField.PK);
                masterDetailFilter.setParam(paramName);
                datasource.setFilters(new N2oPreFilter[]{masterDetailFilter});
                modal.setDatasources(new N2oStandardDatasource[]{datasource});
                modal.setSubmitOperationId(action.getId());
                modal.setCloseAfterSubmit(true);
                button.setActions(new N2oShowModal[]{modal});
            }
            break;
            default:
                throw new UnsupportedOperationException();
        }
        return Collections.singletonList(button);
    }
}
