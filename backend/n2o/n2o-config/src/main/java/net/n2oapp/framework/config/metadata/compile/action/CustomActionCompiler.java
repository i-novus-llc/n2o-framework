package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomActionPayload;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Компиляция кастомного действия
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

        customAction.setPayload(initPayload(source, p));
        customAction.getMeta().setSuccess(initSuccessMeta(customAction, source, context, p));
        customAction.getMeta().setFail(initFailMeta(customAction, source, context));

        return customAction;
    }

    private CustomActionPayload initPayload(N2oCustomAction source, CompileProcessor p) {
        CustomActionPayload payload = new CustomActionPayload();
        if (source.getPayload() != null)
            payload.setAttributes(
                    source.getPayload().keySet().stream()
                            .collect(Collectors.toMap(Function.identity(), k -> p.resolveJS(source.getPayload().get(k))))
            );
        return payload;
    }
}
