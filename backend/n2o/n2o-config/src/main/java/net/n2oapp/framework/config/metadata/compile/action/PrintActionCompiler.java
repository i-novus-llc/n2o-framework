package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.PrintType;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oPrintAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.print.PrintAction;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


/**
 * Компиляция действия печати
 */
@Component
public class PrintActionCompiler extends AbstractActionCompiler<PrintAction, N2oPrintAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPrintAction.class;
    }

    @Override
    public PrintAction compile(N2oPrintAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        PrintAction print = new PrintAction();
        source.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.action.link.src"), String.class)));
        compileAction(print, source, p);
        print.setType(p.resolve(property("n2o.api.action.print.type"), String.class));
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = source.getUrl().startsWith(":")
                ? source.getUrl()
                : RouteUtil.absolute(source.getUrl(), routeScope != null ? routeScope.getUrl() : null);
        print.getPayload().setUrl(StringUtils.hasLink(path)
                ? p.resolveJS(path)
                : RouteUtil.normalize(path));
        print.getPayload().setType(castDefault(source.getType(),
                () -> p.resolve(property("n2o.api.action.print.document_type"), PrintType.class)));
        print.getPayload().setKeepIndent(castDefault(source.getKeepIndent(),
                () -> p.resolve(property("n2o.api.action.print.keep_indent"), Boolean.class)));
        print.getPayload().setDocumentTitle(source.getDocumentTitle());
        print.getPayload().setLoader(castDefault(source.getLoader(),
                () -> p.resolve(property("n2o.api.action.print.loader"), Boolean.class)));
        print.getPayload().setLoaderText(source.getLoaderText());
        print.getPayload().setBase64(castDefault(source.getBase64(),
                () -> p.resolve(property("n2o.api.action.print.base64"), Boolean.class)));

        initMappings(print, source, p, routeScope);
        return print;
    }

    private void initMappings(PrintAction compiled, N2oPrintAction source, CompileProcessor p, ParentRouteScope routeScope) {
        Map<String, ModelLink> pathMapping = initParentRoutePathMappings(routeScope, compiled.getPayload().getUrl());
        Map<String, ModelLink> queryMapping = new LinkedHashMap<>();
        initMappings(source.getPathParams(), source.getQueryParams(), pathMapping, queryMapping, p);

        compiled.getPayload().setQueryMapping(queryMapping);
        compiled.getPayload().setPathMapping(pathMapping);
    }
}
