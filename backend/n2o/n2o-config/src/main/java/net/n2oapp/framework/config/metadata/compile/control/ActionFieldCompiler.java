package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.N2oActionField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.control.ActionField;
import net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;

import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.compileLink;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initActions;

public abstract class ActionFieldCompiler<D extends ActionField, S extends N2oActionField> extends FieldCompiler<D, S> {

    @Override
    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setActions(initActions(source, p));
    }

    protected Action compileAction(N2oActionField source, ActionField field, CompileContext<?, ?> context, CompileProcessor p) {
        Action action = ActionCompileStaticProcessor.compileAction(source, context, p, null);
        field.setAction(action);
        compileLink(field);
        return action;
    }

    protected CompiledObject getCompiledObject(CompileProcessor p, String objectId) {
        if (objectId != null) {
            return p.getCompiled(new ObjectContext(objectId));
        }
        return p.getScope(CompiledObject.class);
    }
}
