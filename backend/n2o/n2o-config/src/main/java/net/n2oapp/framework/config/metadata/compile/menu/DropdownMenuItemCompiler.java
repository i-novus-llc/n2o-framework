package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.menu.N2oAbstractMenuItem;
import net.n2oapp.framework.api.metadata.menu.N2oDropdownMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.BaseMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.DropdownMenuItem;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция элемента меню {@code <dropdown-menu>}
 */
@Component
public class DropdownMenuItemCompiler extends AbstractMenuItemCompiler<N2oDropdownMenuItem, DropdownMenuItem> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDropdownMenuItem.class;
    }

    @Override
    public DropdownMenuItem compile(N2oDropdownMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        DropdownMenuItem compiled = new DropdownMenuItem();
        initMenuItem(source, compiled, p);
        compiled.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.menu.dropdown.src"), String.class)));
        compiled.setTrigger(castDefault(source.getTrigger(),
                () -> p.resolve(property("n2o.api.menu.dropdown.trigger"), TriggerEnum.class)));
        compiled.setPosition(castDefault(source.getPosition(),
                () -> p.resolve(property("n2o.api.menu.dropdown.position"), N2oDropdownMenuItem.PositionTypeEnum.class)));
        ArrayList<BaseMenuItem> items = new ArrayList<>();
        ComponentScope componentScope = new ComponentScope(source);
        for (N2oAbstractMenuItem mi : source.getMenuItems()) {
            mi.setDatasourceId(castDefault(mi.getDatasourceId(), source.getDatasourceId()));
            mi.setModel(castDefault(mi.getModel(), compiled.getModel()));
            items.add(p.compile(mi, context, componentScope));
        }
        compiled.setContent(items);
        return compiled;
    }
}