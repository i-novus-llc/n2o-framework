package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.ExtensionAttributeMapperFactory;
import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomActionPayload;
import net.n2oapp.framework.config.metadata.compile.N2oExtensionAttributeMapperFactory;
import net.n2oapp.framework.config.util.CompileUtil;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Компиляция настраиваемого действия
 */
@Component
public class CustomActionCompiler extends AbstractMetaActionCompiler<CustomAction, N2oCustomAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCustomAction.class;
    }

    @Override
    public CustomAction compile(N2oCustomAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        CustomAction customAction = new CustomAction();
        compileAction(customAction, source, p);
        customAction.setType(source.getType());

        customAction.setPayload(initPayload(source, context, p));
        customAction.getMeta().setSuccess(initSuccessMeta(customAction, source, context, p));
        customAction.getMeta().setFail(initFailMeta(customAction, source, context));

        return customAction;
    }

    private CustomActionPayload initPayload(N2oCustomAction source, CompileContext<?, ?> context, CompileProcessor p) {
        CustomActionPayload payload = new CustomActionPayload();
        ExtensionAttributeMapperFactory extensionAttributeMapperFactory = new N2oExtensionAttributeMapperFactory();
        if (source.getPayload() != null) {
            Map.of(new N2oNamespace(Namespace.NO_NAMESPACE), source.getPayload()).forEach((k, v) -> {
                Map<String, String> resolved = v.keySet().stream()
                        .collect(Collectors.toMap(Function.identity(), key -> p.resolveJS(v.get(key))));
                Map<String, Object> res = extensionAttributeMapperFactory.mapAttributes(resolved, k.getUri(), p);
                res = CompileUtil.resolveNestedAttributes(res, Function.identity());
                if (!res.isEmpty()) {
                    payload.setAttributes(res);
                }
            });
        }

        return payload;
    }
}
