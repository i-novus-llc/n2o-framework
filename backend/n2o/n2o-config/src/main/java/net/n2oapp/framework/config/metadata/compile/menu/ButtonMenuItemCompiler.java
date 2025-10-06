package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.menu.N2oButtonMenuItem;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeUtil;
import net.n2oapp.framework.api.metadata.meta.menu.ButtonMenuItem;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.compileAction;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initActions;
import static net.n2oapp.framework.config.metadata.compile.datasource.DatasourceCompileStaticProcessor.initObject;

/**
 * Компиляция элемента меню {@code <button>}
 */
@Component
public class ButtonMenuItemCompiler extends AbstractMenuItemCompiler<N2oButtonMenuItem, ButtonMenuItem> {

    private static final String PROPERTY_PREFIX = "n2o.api.menu.button";

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oButtonMenuItem.class;
    }

    @Override
    public ButtonMenuItem compile(N2oButtonMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        ButtonMenuItem compiled = new ButtonMenuItem();
        initMenuItem(source, compiled, p);
        compiled.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.menu.button.src"), String.class)));
        compiled.setBadge(BadgeUtil.compileSimpleBadge(source, PROPERTY_PREFIX, p));
        compiled.setColor(p.resolveJS(source.getColor()));
        if (source.getDescription() != null) {
            compiled.setHint(p.resolveJS(source.getDescription().trim()));
            compiled.setHintPosition(source.getTooltipPosition());
        }
        source.setActions(initActions(source, p));
        CompiledObject compiledObject = initObject(p, source);
        Action action = compileAction(source, context, p, compiledObject);
        compiled.setAction(action);

        return compiled;
    }
}