package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModalPayload;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.convertPathToId;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.register.route.RouteUtil.parent;

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
        showModal.getOptions().setType(p.resolve(property("n2o.api.action.show_modal.type"), String.class));
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
        ShowModalPayload payload = compiled.getOptions().getPayload();
        String modalPageId = convertPathToId(route);
        payload.setName(modalPageId);
        payload.setPageId(modalPageId);
        payload.setPageUrl(route);
        payload.setPathMapping(pathMapping);
        payload.setQueryMapping(queryMapping);
    }

    private void compilePayload(ShowModal showModal, N2oShowModal source, CompileContext<?, ?> context, CompileProcessor p) {
        ShowModalPayload payload = showModal.getOptions().getPayload();
        payload.setSize(source.getModalSize());
        payload.setCloseButton(true);
    }
}
