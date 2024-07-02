package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.util.StylesResolver;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Сборка компонента
 */
public abstract class ComponentCompiler<D extends Component, S extends SourceComponent & ExtensionAttributesAware, C extends CompileContext<?, ?>>
        implements BaseSourceCompiler<D, S, C> {

    protected void compileComponent(D compiled, S source, CompileProcessor p) {
        compiled.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property(getSrcProperty()), String.class)));

        if (compiled.getSrc() == null) {
            throw new N2oException("Required src for N2O component");
        }

        compiled.setClassName(source.getCssClass());
        compiled.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compiled.setProperties(p.mapAndResolveAttributes(source));
    }

    /**
     * Свойство содержащее React компонент по умолчанию
     */
    protected String getSrcProperty() {
        return null;
    }
}
