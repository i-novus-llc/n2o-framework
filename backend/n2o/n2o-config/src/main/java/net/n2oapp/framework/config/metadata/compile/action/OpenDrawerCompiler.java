package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oOpenDrawer;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawerPayload;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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

    protected void compilePayload(N2oOpenDrawer source, OpenDrawer drawer, PageContext pageContext, CompileProcessor p) {
        OpenDrawerPayload payload = drawer.getPayload();
        payload.setBackdrop(castDefault(source.getBackdrop(),
                () -> p.resolve(property("n2o.api.action.open_drawer.backdrop"), Boolean.class)));
        payload.setWidth(prepareSizeAttribute(castDefault(source.getWidth(),
                () -> p.resolve(property("n2o.api.action.open_drawer.width"), String.class))));
        payload.setHeight(prepareSizeAttribute(source.getHeight()));
        payload.setPlacement(castDefault(source.getPlacement(),
                () -> p.resolve(property("n2o.api.action.open_drawer.placement"), String.class)));
        payload.setCloseOnBackdrop(castDefault(source.getCloseOnBackdrop(),
                () -> p.resolve(property("n2o.api.action.open_drawer.close_on_backdrop"), Boolean.class), () -> true));
        payload.setClosable(castDefault(source.getClosable(),
                () -> p.resolve(property("n2o.api.action.open_drawer.closable"), Boolean.class), () -> true));
        payload.setPrompt(pageContext.getUnsavedDataPromptOnClose());
        payload.setFixedFooter(castDefault(source.getFixedFooter(),
                () -> p.resolve(property("n2o.api.action.open_drawer.fixed_footer"), Boolean.class)));
        payload.setCloseOnEscape(castDefault(source.getCloseOnEscape(),
                () -> p.resolve(property("n2o.api.action.open_drawer.close_on_escape"), Boolean.class)));
    }
}
