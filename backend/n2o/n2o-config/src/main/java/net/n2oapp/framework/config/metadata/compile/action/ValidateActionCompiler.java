package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oValidateAction;
import net.n2oapp.framework.api.metadata.action.ValidateBreakOnEnum;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.validate.ValidateAction;
import net.n2oapp.framework.api.metadata.meta.action.validate.ValidateActionPayload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.getLocalDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

@Component
public class ValidateActionCompiler extends AbstractActionCompiler<ValidateAction, N2oValidateAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oValidateAction.class;
    }

    @Override
    public ValidateAction compile(N2oValidateAction source, CompileContext<?, ?> context, CompileProcessor p) {
        ValidateAction compiled = new ValidateAction();
        compileAction(compiled, source, p);
        compiled.setType(p.resolve(property("n2o.api.action.validate.type"), String.class));

        ValidateActionPayload payload = compiled.getPayload();
        payload.setId(getClientDatasourceId(getLocalDatasourceId(p), p));
        payload.setModel(ActionCompileStaticProcessor.getModelFromComponentScope(p));
        payload.setBreakOn(castDefault(source.getBreakOn(), () -> p.resolve(property("n2o.api.action.validate.break_on"), ValidateBreakOnEnum.class)));
        if (source.getFields() != null && source.getFields().length > 0) {
            payload.setFields(Arrays.stream(source.getFields())
                    .filter(Objects::nonNull)
                    .map(N2oValidateAction.Field::getId)
                    .filter(Objects::nonNull)
                    .toList());
        }

        return compiled;
    }

}
