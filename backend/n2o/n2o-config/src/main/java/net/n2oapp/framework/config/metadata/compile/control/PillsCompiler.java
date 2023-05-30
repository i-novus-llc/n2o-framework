package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.list.MultiType;
import net.n2oapp.framework.api.metadata.control.list.N2oPills;
import net.n2oapp.framework.api.metadata.meta.control.Pills;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class PillsCompiler extends ListControlCompiler<Pills, N2oPills> {
    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.pills.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPills.class;
    }

    @Override
    public StandardField<Pills> compile(N2oPills source, CompileContext<?, ?> context, CompileProcessor p) {
        Pills pills = new Pills();
        source.setType(p.cast(source.getType(),
                p.resolve(property("n2o.api.control.pills.type"), MultiType.class)));
        pills.setMulti(source.getType().equals(MultiType.checkboxes));
        StandardField<Pills> result = compileListControl(pills, source, context, p);
        return compileFetchDependencies(result, source, p);
    }
}
