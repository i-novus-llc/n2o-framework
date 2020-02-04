package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.event.action.ShowModalMode;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModalPayload;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.convertPathToId;

/**
 * Компиляция show-modal
 */
@Component
public class ShowModalCompiler extends AbstractOpenPageCompiler<ShowModal, N2oShowModal> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oShowModal.class;
    }

    @Override
    public ShowModal compile(N2oShowModal source, CompileContext<?, ?> context, CompileProcessor p) {
        ShowModal showModal = new ShowModal();
        showModal.setType(p.resolve(property("n2o.api.action.show_modal.type"), String.class));
        showModal.setObjectId(source.getObjectId());
        showModal.setOperationId(source.getOperationId());
        showModal.setPageId(source.getPageId());
        compileAction(showModal, source, p);
        initPageContext(showModal, source, context, p);
        compilePayload(showModal, source, context, p);
        return showModal;
    }

    @Override
    protected PageContext constructContext(String pageId, String route) {
        ModalPageContext modalPageContext = new ModalPageContext(pageId, route);
        modalPageContext.setClientPageId(convertPathToId(route));
        return modalPageContext;
    }

    @Override
    protected void initPageRoute(ShowModal compiled,
                                 String route,
                                 Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping) {
        ShowModalPayload payload = compiled.getPayload();
        String modalPageId = convertPathToId(route);
        payload.setName(modalPageId);
        payload.setPageId(modalPageId);
        payload.setPageUrl(route);
        payload.setPathMapping(pathMapping);
        payload.setQueryMapping(queryMapping);
    }

    private void compilePayload(ShowModal showModal, N2oShowModal source, CompileContext<?, ?> context, CompileProcessor p) {
        ShowModalPayload payload = showModal.getPayload();
        payload.setSize(source.getModalSize());
        payload.setCloseButton(true);
        payload.setMode(p.cast(source.getType(), ShowModalMode.modal));
    }
}
