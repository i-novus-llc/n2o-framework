package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
    public PerformButton compile(N2oButton but, CompileContext<?, ?> context, CompileProcessor p) {
        PerformButton button = new PerformButton();
        button.setSrc(p.cast(but.getSrc(), p.resolve(property("n2o.api.action.button.src"), String.class)));

        button.setProperties(p.mapAttributes(but));
        if (but.getColor() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                N2oCell component = componentScope.unwrap(N2oCell.class);
                if (component != null) {
                    button.setColor(p.resolve(property("n2o.api.cell.toolbar.button-color"), String.class));
                }
            }
        } else {
            button.setColor(but.getColor());
        }
        IndexScope idx = p.getScope(IndexScope.class);
        initItem(button, but, idx, context, p);
        if (but.getValidate() != null && but.getValidate()) {
            button.setValidatedWidgetId(initWidgetId(but, context, p));
        }
        if (button.getAction() != null && button.getAction() instanceof LinkAction) {
            LinkAction linkAction = ((LinkAction) button.getAction());
            button.setUrl(linkAction.getUrl());
            button.setTarget(linkAction.getTarget());
            if (linkAction.getPathMapping() != null)
                button.setPathMapping(new StrictMap<>(linkAction.getPathMapping()));
            if (linkAction.getQueryMapping() != null)
                button.setQueryMapping(new StrictMap<>(linkAction.getQueryMapping()));
        }
        return button;
    }

}
