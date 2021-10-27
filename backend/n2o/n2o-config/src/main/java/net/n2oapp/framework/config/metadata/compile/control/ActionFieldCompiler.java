package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oActionField;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.control.ActionField;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;

public abstract class ActionFieldCompiler<D extends ActionField, S extends N2oField> extends FieldCompiler<D, S> {

    protected Action compileAction(N2oActionField source, ActionField field, CompileContext<?, ?> context, CompileProcessor p) {
        ComponentScope scope = null;
        N2oAction action = null;
        if (source.getAction() != null) {
            scope = new ComponentScope(source);
            action = source.getAction();
        } else if (source.getActionId() != null) {
            scope = p.getScope(ComponentScope.class);
            action = getAction(scope, source.getActionId());
        }
        if (action != null) {
            String objectId = action.getObjectId();
            CompiledObject compiledObject = getCompiledObject(p, objectId);
            action.setId(p.cast(action.getId(), field.getId()));
            Action result = p.compile(action, context, compiledObject, scope);
            if (result instanceof LinkAction) {
                LinkAction linkAction = ((LinkAction) result);
                field.setUrl(linkAction.getUrl());
                field.setTarget(linkAction.getTarget());
                field.setPathMapping(linkAction.getPathMapping());
                field.setQueryMapping(linkAction.getQueryMapping());
            } else {
                field.setAction(result);
            }
            return result;
        }
        return null;
    }

    protected N2oAction getAction(ComponentScope scope, String actionId) {
        if (scope != null) {
            N2oForm form = scope.unwrap(N2oForm.class);
            if (form != null && form.getActions() != null) {
                for (ActionsBar act : form.getActions()) {
                    if (actionId.equals(act.getId()))
                        return act.getAction();
                }
            }
        }
        return null;
    }

    protected CompiledObject getCompiledObject(CompileProcessor p, String objectId) {
        if (objectId != null) {
            WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);
            if (widgetObjectScope != null && widgetObjectScope.containsKey(objectId))
                return widgetObjectScope.getObject(objectId);
        }
        return p.getScope(CompiledObject.class);
    }
}
