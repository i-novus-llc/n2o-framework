package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.config.util.StylesResolver;

/**
 * Сборка компонента
 */
public abstract class ComponentCompiler<D extends Component, S extends N2oComponent, C extends CompileContext<?, ?>>
        implements BaseSourceCompiler<D, S, C> {

    protected void compileComponent(D compiled, S source, C context, CompileProcessor p) {
        if (getSrcProperty() == null) {
            compiled.setSrc(source.getSrc());
        } else {
            compiled.setSrc(p.cast(source.getSrc(), p.resolve(Placeholders.property(getSrcProperty()), String.class)));
        }
        if (compiled.getSrc() == null)
            throw new N2oException("component src is required");
        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAttributes(source));
    }

    /**
     * Свойство содержащее React компонент по умолчанию
     */
    protected String getSrcProperty() {
        return null;
    }
}
