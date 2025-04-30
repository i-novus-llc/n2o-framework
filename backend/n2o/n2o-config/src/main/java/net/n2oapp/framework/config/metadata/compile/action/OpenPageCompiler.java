package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        initDefaults(source, p);
        LinkActionImpl openPage = new LinkActionImpl();
        source.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.action.link.src"), String.class)));
        openPage.setType(p.resolve(property("n2o.api.action.link.type"), String.class));
        openPage.setObjectId(source.getObjectId());
        openPage.setTarget(castDefault(source.getTarget(), TargetEnum.application));
        openPage.setOperationId(source.getOperationId());
        openPage.setPageId(source.getPageId());
        compileAction(openPage, source, p);
        initPageContext(openPage, source, context, p);

        return openPage;
    }

    @Override
    protected void initPageRoute(LinkAction compiled, String route,
                                 Map<String, ModelLink> pathMapping,
                                 Map<String, ModelLink> queryMapping) {
        if (TargetEnum.application.equals(compiled.getTarget()))
            compiled.setUrl(route);
        else
            compiled.setUrl("#" + route);
        compiled.setPathMapping(pathMapping);
        compiled.setQueryMapping(queryMapping);
    }
}
