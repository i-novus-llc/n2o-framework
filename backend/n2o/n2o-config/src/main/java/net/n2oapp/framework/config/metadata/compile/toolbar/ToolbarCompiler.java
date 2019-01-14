package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.ButtonCondition;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция тулбара
 */
@Component
public class ToolbarCompiler implements BaseSourceCompiler<Toolbar, N2oToolbar, CompileContext<?, ?>>, SourceClassAware, MetadataEnvironmentAware {

    protected ButtonGeneratorFactory buttonGeneratorFactory;

    @Override
    public Toolbar compile(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p) {
        Toolbar toolbar = new Toolbar();
        List<Group> groups = new ArrayList<>();
        IndexScope index = p.getScope(IndexScope.class);
        initGenerate(source, context, p);
        int i = 0;
        if (source.getItems() == null)
            return toolbar;
        String defaultPlace = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            defaultPlace = property("n2o.api.widget.toolbar.place");
        else
            defaultPlace = property("n2o.api.page.toolbar.place");
        String place = p.cast(source.getPlace(), p.resolve(defaultPlace, String.class));
        int gi = 0;
        while (i < source.getItems().length) {
            Group gr = new Group(place + gi);
            List<Button> buttons = new ArrayList<>();
            ToolbarItem item = source.getItems()[i];
            if (item instanceof N2oGroup) {
                N2oGroup group = (N2oGroup) item;
                if (group.getGenerate() != null) {
                    for (String generate : group.getGenerate()) {
                        buttonGeneratorFactory.generate(generate, source, context, p)
                                .forEach(j -> buttons.add(getButton(source, j, index, context, p)));
                    }
                } else {
                    for (GroupItem it : group.getItems()) {
                        buttons.add(getButton(source, it, index, context, p));
                    }
                }
                i++;
            } else {
                while (i < source.getItems().length && !(source.getItems()[i] instanceof N2oGroup)) {
                    buttons.add(getButton(source, source.getItems()[i], index, context, p));
                    i++;
                }
            }
            gr.setButtons(buttons);
            groups.add(gr);
            gi++;
        }
        toolbar.put(place, groups);
        return toolbar;
    }

    protected void initItem(MenuItem button, AbstractMenuItem source, IndexScope idx,
                            CompileContext<?, ?> context, CompileProcessor p) {
        button.setId(castDefault(source.getId(), source.getActionId(), "menuItem" + idx.get()));
        source.setId(button.getId());
        if (source.getType() != null && source.getType().equals(LabelType.icon)) {
            button.setIcon(source.getIcon());
        } else if (source.getType() != null && source.getType().equals(LabelType.text)) {
            button.setLabel(source.getLabel());
        } else {
            button.setIcon(source.getIcon());
            button.setLabel(source.getLabel());
        }

        if (source.getActionId() == null) {
            N2oAction butAction = source.getAction();
            if (butAction != null) {
                butAction.setId(p.cast(butAction.getId(), button.getId()));
                Action action = p.compile(butAction, context, new ComponentScope(source));
                button.setActionId(action.getId());

                if (action instanceof InvokeAction && source instanceof N2oButton) {
                    CompiledObject compiledObject = p.getScope(CompiledObject.class);
                    CompiledObject.Operation operation = compiledObject != null && compiledObject.getOperations() != null ?
                            compiledObject.getOperations().get(((InvokeAction) action).getOperationId()) : null;

                    if((source.getConfirm() != null && source.getConfirm()) ||
                            (source.getConfirm() == null && operation != null && operation.getConfirm() != null && operation.getConfirm()) ) {
                        N2oButton srcBtn = (N2oButton)source;

                        Confirm confirm = new Confirm();
                        confirm.setText(p.cast(srcBtn.getConfirmText(), (operation != null ? operation.getConfirmationText(): null), p.getMessage("n2o.confirm.text")));
                        confirm.setTitle(p.cast(srcBtn.getConfirmTitle(),  (operation != null ? operation.getFormSubmitLabel(): null), p.getMessage("n2o.confirm.title")));
                        confirm.setOkLabel(p.cast(srcBtn.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")));
                        confirm.setCancelLabel(p.cast(srcBtn.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")));
                        button.setConfirm(confirm);
                    }
                }
                //todo если это invoke-action, то из action в объекте должны доставаться поля action.getName(), confirmationText
            }
        } else {
            button.setActionId(source.getActionId());
        }
        button.setClassName(source.getClassName());
        if (p.cast(source.getDescription(), source.getLabel()) != null)
            button.setHint(p.cast(source.getDescription(), source.getLabel()).trim());
        button.setVisible(p.resolveJS(source.getVisible(), Boolean.class));
        button.setEnabled(p.resolveJS(source.getEnabled(), Boolean.class));
        if (source.getModel() == null)
            source.setModel(ReduxModel.RESOLVE);
        compileDependencies(button, source, context, p);
        button.setValidate(source.getValidate());
    }

    /**
     * Компиляция зависимостей между полем и кнопкой
     *
     * @param button клиентская модель кнопки
     * @param source исходная модель поля
     */
    protected void compileDependencies(MenuItem button, AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getVisibilityConditions() != null) {
            String widgetId = initWidgetId(source, context, p);
            List<ButtonCondition> conditions = new ArrayList<>();
            for (N2oButtonCondition n2oCondition : source.getVisibilityConditions()) {
                ButtonCondition condition = new ButtonCondition();
                condition.setExpression(n2oCondition.getExpression().trim());
                condition.setModelLink(new ModelLink(source.getModel(), widgetId).getBindLink());
                conditions.add(condition);
            }
            button.getConditions().put(ValidationType.visible, conditions);
        }
        List<ButtonCondition> conditions = new ArrayList<>();
        if (source.getEnablingConditions() != null) {
            String widgetId = initWidgetId(source, context, p);
            for (N2oButtonCondition n2oCondition : source.getEnablingConditions()) {
                ButtonCondition condition = new ButtonCondition();
                condition.setExpression(n2oCondition.getExpression().trim());
                condition.setModelLink(new ModelLink(source.getModel(), widgetId).getBindLink());
                conditions.add(condition);
            }
        }
        if (source.getModel() == null || source.getModel().equals(ReduxModel.RESOLVE)) {
            String widgetId = initWidgetId(source, context, p);
            ButtonCondition condition = new ButtonCondition();
            condition.setExpression("!_.isEmpty(this)");
            condition.setModelLink(new ModelLink(ReduxModel.RESOLVE, widgetId).getBindLink());
            conditions.add(condition);
        }
        if (!conditions.isEmpty()) {
            button.getConditions().put(ValidationType.enabled, conditions);
        }
    }

    protected String initWidgetId(AbstractMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (source.getWidgetId() != null) {
            return pageScope == null ? source.getWidgetId() : pageScope.getGlobalWidgetId(source.getWidgetId());//todo обсудить
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null) {
            return widgetScope.getClientWidgetId();
        } else if (context instanceof PageContext && ((PageContext) context).getResultWidgetId() != null) {
            return pageScope.getGlobalWidgetId(((PageContext) context).getResultWidgetId());
        } else {
            throw new N2oException("Unknown widgetId for invoke action!");
        }
    }

    protected Button getButton(N2oToolbar source, ToolbarItem item, IndexScope idx,
                               CompileContext<?, ?> context, CompileProcessor p) {
        Button button = new Button();
        if (item instanceof N2oButton) {
            N2oButton but = (N2oButton) item;
            button.setProperties(p.mapAttributes(but));
            button.setColor(but.getColor());
            button.setDropdownSrc(but.getDropdownSrc());
            initItem(button, but, idx, context, p);
        } else if (item instanceof N2oSubmenu) {
            N2oSubmenu sub = (N2oSubmenu) item;
            button.setId(sub.getId() == null ? "subMenu" + idx.get() : sub.getId());
            button.setLabel(sub.getLabel());
            button.setClassName(sub.getClassName());
            if (sub.getDescription() != null)
                button.setHint(sub.getDescription().trim());
            button.setIcon(sub.getIcon());
            button.setVisible(sub.getVisible());
            if (sub.getMenuItems() != null) {
                button.setSubMenu(Stream.of(sub.getMenuItems()).map(mi -> {
                    MenuItem menuItem = new MenuItem();
                    initItem(menuItem, mi, idx, context, p);
                    return menuItem;
                }).collect(Collectors.toList()));
            }
            if (sub.getGenerate() != null) {
                if (button.getSubMenu() == null) {
                    button.setSubMenu(new ArrayList<>());
                }
                for (String generate : sub.getGenerate()) {
                    for (ToolbarItem toolbarItem : buttonGeneratorFactory.generate(generate, source, context, p)) {
                        MenuItem menuItem = new MenuItem();
                        button.getSubMenu().add(menuItem);
                        initItem(menuItem, (N2oButton) toolbarItem, idx, context, p);
                    }
                }
            }
        }
        return button;
    }

    private ToolbarItem[] push(N2oToolbar source, N2oButton button) {
        ToolbarItem[] items;
        if (source.getItems() == null) {
            items = new ToolbarItem[1];
            items[0] = button;
        } else {
            int length = source.getItems().length;
            items = new ToolbarItem[length + 1];
            System.arraycopy(source.getItems(), 0, items, 0, length);
            items[length] = button;
        }
        return items;
    }

    protected void initGenerate(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getGenerate() != null) {
            for (String generate : source.getGenerate()) {
                buttonGeneratorFactory.generate(generate, source, context, p)
                        .forEach(i -> source.setItems(push(source, (N2oButton) i)));
            }
        }
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        buttonGeneratorFactory = environment.getButtonGeneratorFactory();
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oToolbar.class;
    }
}

