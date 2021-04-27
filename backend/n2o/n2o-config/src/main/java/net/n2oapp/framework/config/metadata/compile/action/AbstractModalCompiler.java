package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.modal.AbstractModal;
import net.n2oapp.framework.api.metadata.meta.action.modal.ModalPayload;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

import java.util.Map;

import static net.n2oapp.framework.config.register.route.RouteUtil.convertPathToId;

/**
 * Компиляция абстрактного действия открытия окна
 */
public abstract class AbstractModalCompiler<D extends AbstractModal, S extends N2oAbstractPageAction> extends AbstractOpenPageCompiler<D, S> {

    public void compileModal(S source, D compiled, CompileContext<?, ?> context, CompileProcessor p) {
        compiled.setObjectId(source.getObjectId());
        compiled.setOperationId(source.getOperationId());
        compiled.setPageId(source.getPageId());

        compileAction(compiled, source, p);
        PageContext pageContext = initPageContext(compiled, source, context, p);
        compilePayload(source, compiled, pageContext, p);
    }

    protected abstract void compilePayload(S source, D compiled, PageContext pageContext, CompileProcessor p);

    @Override
    protected void initPageRoute(D compiled, String route, Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping) {
        ModalPayload payload = (ModalPayload) compiled.getPayload();
        String modalPageId = convertPathToId(route);
        payload.setName(modalPageId);
        payload.setPageId(modalPageId);
        payload.setPageUrl(route);
        payload.setPathMapping(pathMapping);
        payload.setQueryMapping(queryMapping);
    }
}
