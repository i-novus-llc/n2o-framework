package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция open-page
 */
@Component
public class OpenPageCompiler extends AbstractOpenPageCompiler<LinkAction, N2oOpenPage> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOpenPage.class;
    }

    @Override
    public LinkAction compile(N2oOpenPage source, CompileContext<?, ?> context, CompileProcessor p) {
        LinkActionImpl openPage = new LinkActionImpl();
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.link.src"), String.class)));
        openPage.setObjectId(source.getObjectId());
        openPage.setTarget(p.cast(source.getTarget(), Target.application));
        openPage.setOperationId(source.getOperationId());
        openPage.setPageId(source.getPageId());
        compileAction(openPage, source, p);
        initPageContext(openPage, source, context, p);
        return openPage;
    }

    @Override
    protected PageContext constructContext(String pageId, String route) {
        return new PageContext(pageId, route);
    }

    @Override
    protected void initPageRoute(LinkAction compiled, String route,
                                 Map<String, ModelLink> pathMapping,
                                 Map<String, ModelLink> queryMapping) {
        compiled.setUrl(route);
        compiled.setPathMapping(pathMapping);
        compiled.setQueryMapping(queryMapping);
    }
}
