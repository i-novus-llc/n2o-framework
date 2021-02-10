package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;
import org.springframework.stereotype.Component;

import java.util.Set;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;

/**
 * Компиляция N2oButton
 */
@Component
public class PerformButtonCompiler extends BaseButtonCompiler<N2oButton, PerformButton> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButton.class;
    }

    @Override
    public PerformButton compile(N2oButton source, CompileContext<?, ?> context, CompileProcessor p) {
        PerformButton button = new PerformButton();
        IndexScope idx = p.getScope(IndexScope.class);
        initItem(button, source, idx, context, p);
        button.setId(p.cast(source.getId(), source.getActionId(), "menuItem" + idx.get()));
        source.setId(button.getId());
        button.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.button.src"), String.class)));
        button.setRounded(p.cast(source.getRounded(), false));
        button.setProperties(p.mapAttributes(source));
        initValidate(source, context, p, button);

        CompiledObject.Operation operation = null;
        CompiledObject compiledObject = null;
        WidgetObjectScope widgetObjectScope = p.getScope(WidgetObjectScope.class);

        if (widgetObjectScope != null && widgetObjectScope.containsKey(source.getWidgetId()))
            compiledObject = widgetObjectScope.getObject(source.getWidgetId());
        if (compiledObject == null)
            compiledObject = p.getScope(CompiledObject.class);

        Action action = compileAction(button, source, context, p, compiledObject);

        if (action != null) {
            button.setAction(action);
            if (action instanceof InvokeAction) {
                operation = compiledObject != null && compiledObject.getOperations() != null
                        && compiledObject.getOperations().containsKey(((InvokeAction) action).getOperationId()) ?
                        compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;
            }
            //todo если это invoke-action, то из action в объекте должны доставаться поля action.getName(), confirmationText
        }

        initLinkAction(button);
        initConfirm(button, source, context, p, operation);
        button.setValidate(source.getValidate());
        return button;
    }

    private void initLinkAction(PerformButton button) {
        if (button.getAction() instanceof LinkAction) {
            LinkAction linkAction = ((LinkAction) button.getAction());
            button.setUrl(linkAction.getUrl());
            button.setTarget(linkAction.getTarget());
            if (linkAction.getPathMapping() != null)
                button.setPathMapping(new StrictMap<>(linkAction.getPathMapping()));
            if (linkAction.getQueryMapping() != null)
                button.setQueryMapping(new StrictMap<>(linkAction.getQueryMapping()));
        }
    }

    private void initValidate(N2oButton but, CompileContext<?, ?> context, CompileProcessor p, PerformButton button) {
        if (but.getValidate() != null && but.getValidate()) {
            button.setValidatedWidgetId(initWidgetId(but, context, p));
        }
    }

    private void initConfirm(PerformButton button, N2oButton source, CompileContext<?, ?> context, CompileProcessor p, CompiledObject.Operation operation) {
        if ((source.getConfirm() == null || !source.getConfirm()) &&
                (source.getConfirm() != null || operation == null || operation.getConfirm() == null || !operation.getConfirm()))
            return;
        Confirm confirm = new Confirm();
        confirm.setMode(p.cast(source.getConfirmType(), ConfirmType.modal));
        confirm.setText(p.cast(source.getConfirmText(), (operation != null ? operation.getConfirmationText() : null), p.getMessage("n2o.confirm.text")));
        confirm.setTitle(p.cast(source.getConfirmTitle(), (operation != null ? operation.getFormSubmitLabel() : null), p.getMessage("n2o.confirm.title")));
        confirm.setOkLabel(p.cast(source.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")));
        confirm.setCancelLabel(p.cast(source.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")));
        if (StringUtils.hasLink(confirm.getText())) {
            Set<String> links = StringUtils.collectLinks(confirm.getText());
            String text = js("'" + confirm.getText() + "'");
            for (String link : links) {
                text = text.replace(ref(link), "' + this." + link + " + '");
            }
            confirm.setText(text);
        }
        if (StringUtils.isJs(confirm.getText())) {
            String widgetId = initWidgetId(source, context, p);
            ReduxModel reduxModel = source.getModel();
            confirm.setModelLink(new ModelLink(reduxModel == null ? ReduxModel.RESOLVE : reduxModel, widgetId).getBindLink());
        }
        button.setConfirm(confirm);
    }

    private Action compileAction(PerformButton button, N2oButton source, CompileContext<?, ?> context, CompileProcessor p,
                                 CompiledObject compiledObject) {
        Action action = null;
        if (source.getActionId() != null) {
            MetaActions metaActions = p.getScope(MetaActions.class);
            action = metaActions.get(source.getActionId());
        } else {
            N2oAction butAction = source.getAction();
            if (butAction != null) {
                butAction.setId(p.cast(butAction.getId(), button.getId()));
                action = p.compile(butAction, context, compiledObject, new ComponentScope(source));
            }
        }
        return action;
    }
}
