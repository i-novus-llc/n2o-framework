package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.menu.N2oDividerMenuItem;
import net.n2oapp.framework.api.metadata.meta.menu.DividerMenuItem;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция элемента меню {@code <divider>}
 */
@Component
public class DividerMenuItemCompiler extends AbstractMenuItemCompiler<N2oDividerMenuItem, DividerMenuItem> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDividerMenuItem.class;
    }

    @Override
    public DividerMenuItem compile(N2oDividerMenuItem source, CompileContext<?, ?>  context, CompileProcessor p) {
        DividerMenuItem compiled = new DividerMenuItem();
        initMenuItem(source, compiled, p);
        compiled.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.menu.divider.src"), String.class)));
        return compiled;
    }
}