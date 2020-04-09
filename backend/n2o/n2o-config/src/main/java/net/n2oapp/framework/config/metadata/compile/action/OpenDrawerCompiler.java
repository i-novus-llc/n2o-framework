package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenDrawer;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.open_drawer.OpenDrawerPayload;
import net.n2oapp.framework.config.metadata.compile.context.DrawerPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.convertPathToId;

/**
 * Компиляция show-modal
 */
@Component
public class OpenDrawerCompiler extends AbstractOpenPageCompiler<OpenDrawer, N2oOpenDrawer> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOpenDrawer.class;
    }

    @Override
    public OpenDrawer compile(N2oOpenDrawer source, CompileContext<?, ?> context, CompileProcessor p) {
        OpenDrawer showModal = new OpenDrawer();
        showModal.setType(p.resolve(property("n2o.api.action.open_drawer.type"), String.class));
        showModal.setObjectId(source.getObjectId());
        showModal.setOperationId(source.getOperationId());
        showModal.setPageId(source.getPageId());
        showModal.setBackdrop(source.getBackdrop());
        showModal.setWidth(source.getWidth());
        showModal.setHeight(source.getHeight());
        showModal.setPlacement(p.cast(source.getPlacement(), property("n2o.api.action.open_drawer.placement")));
        showModal.setLevel(source.getLevel());
        showModal.setBackdropClosable(p.cast(source.getBackdropClosable(), true));

        compileAction(showModal, source, p);
        initPageContext(showModal, source, context, p);
        compilePayload(showModal, source, context, p);
        return showModal;
    }

    @Override
    protected PageContext constructContext(String pageId, String route) {
        DrawerPageContext modalPageContext = new DrawerPageContext(pageId, route);
        modalPageContext.setClientPageId(convertPathToId(route));
        return modalPageContext;
    }

    @Override
    protected void initPageRoute(OpenDrawer compiled,
                                 String route,
                                 Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping) {
        OpenDrawerPayload payload = compiled.getPayload();
        String modalPageId = convertPathToId(route);
        payload.setName(modalPageId);
        payload.setPageId(modalPageId);
        payload.setPageUrl(route);
        payload.setPathMapping(pathMapping);
        payload.setQueryMapping(queryMapping);
    }

    private void compilePayload(OpenDrawer showModal, N2oOpenDrawer source, CompileContext<?, ?> context, CompileProcessor p) {
        OpenDrawerPayload payload = showModal.getPayload();
        payload.setClosable(p.cast(source.getClosable(), true));
    }
}
