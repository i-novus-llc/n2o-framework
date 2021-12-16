package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawerPayload;
import net.n2oapp.framework.config.metadata.compile.context.DrawerPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.convertPathToId;

/**
 * Компиляция действия открытия drawer окна
 */
@Component
public class OpenDrawerCompiler extends AbstractModalCompiler<OpenDrawer, N2oOpenDrawer> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOpenDrawer.class;
    }

    @Override
    public OpenDrawer compile(N2oOpenDrawer source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        OpenDrawer drawer = new OpenDrawer();
        compileModal(source, drawer, context, p);
        drawer.setType(p.resolve(property("n2o.api.action.open_drawer.type"), String.class));
        return drawer;
    }

    @Override
    protected PageContext constructContext(String pageId, String route) {
        DrawerPageContext drawerPageContext = new DrawerPageContext(pageId, route);
        drawerPageContext.setClientPageId(convertPathToId(route));
        return drawerPageContext;
    }

    protected void compilePayload(N2oOpenDrawer source, OpenDrawer drawer, PageContext pageContext, CompileProcessor p) {
        OpenDrawerPayload payload = drawer.getPayload();
        payload.setBackdrop(p.cast(source.getBackdrop(),
                p.resolve(property("n2o.api.action.open_drawer.backdrop"), Boolean.class)));
        payload.setWidth(p.cast(source.getWidth(),
                p.resolve(property("n2o.api.action.open_drawer.width"), String.class)));
        payload.setHeight(source.getHeight());
        payload.setPlacement(p.cast(source.getPlacement(),
                p.resolve(property("n2o.api.action.open_drawer.placement"), String.class)));
        payload.setLevel(source.getLevel());
        payload.setCloseOnBackdrop(p.cast(source.getCloseOnBackdrop(), true));
        payload.setClosable(p.cast(source.getClosable(), true));
        payload.setPrompt(pageContext.getUnsavedDataPromptOnClose());
        payload.setFixedFooter(p.cast(source.getFixedFooter(), p.resolve(property("n2o.api.action.open_drawer.fixed_footer"), Boolean.class)));
        payload.setCloseOnEscape(p.cast(source.getCloseOnEscape(), p.resolve(property("n2o.api.action.open_drawer.close_on_escape"), Boolean.class)));
    }
}
