package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;

/**
 * Сборка компонента
 */
public abstract class ComponentCompiler<D extends Component, S extends N2oComponent>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void compileComponent(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
        String src = source instanceof N2oStandardField ?
                p.resolve(Placeholders.property(getSrcProperty()), String.class) :
                p.cast(source.getSrc(), p.resolve(Placeholders.property(getSrcProperty()), String.class));
        if (src == null)
            throw new N2oException("component src is required");
        compiled.setSrc(src);
        compiled.setClassName(source.getCssClass());
        compiled.setProperties(p.mapAttributes(source));
    }

    /**
     * Свойство содержащее React компонент по умолчанию
     */
    protected String getSrcProperty() {
        return null;
    }
}
