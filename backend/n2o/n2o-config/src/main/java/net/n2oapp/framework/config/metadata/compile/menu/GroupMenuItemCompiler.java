package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oAbstractMenuItem;
import net.n2oapp.framework.api.metadata.menu.N2oGroupMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.BaseMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.GroupMenuItem;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция элемента меню {@code <group>}
 */
@Component
public class GroupMenuItemCompiler extends AbstractMenuItemCompiler<N2oGroupMenuItem, GroupMenuItem> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oGroupMenuItem.class;
    }

    @Override
    public GroupMenuItem compile(N2oGroupMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        GroupMenuItem compiled = new GroupMenuItem();
        initMenuItem(source, compiled, p);
        compiled.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.menu.group.src"), String.class)));
        compiled.setCollapsible(castDefault(source.getCollapsible(),
                () -> p.resolve(property("n2o.api.menu.group.collapsible"), Boolean.class)));
        compiled.setDefaultState(castDefault(source.getDefaultState(),
                () -> p.resolve(property("n2o.api.menu.group.default_state"), N2oGroupMenuItem.GroupStateTypeEnum.class)));
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