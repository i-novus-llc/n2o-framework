package net.n2oapp.framework.access.integration.metadata.transform.action;

import net.n2oapp.framework.access.integration.metadata.transform.BaseAccessTransformer;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Трансформатор доступа invoke
 */
@Component
public class MultiActionAccessTransformer extends BaseAccessTransformer<MultiAction, CompileContext<?, ?>> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return MultiAction.class;
    }

    @Override
    public MultiAction transform(MultiAction compiled, CompileContext context, CompileProcessor p) {
        mapSecurity(compiled);
        return compiled;
    }

    private void mapSecurity(MultiAction compiled) {
        if (compiled.getPayload() != null && !CollectionUtils.isEmpty(compiled.getPayload().getActions())) {
            merge(compiled, compiled.getPayload().getActions());
        }

    }
}
