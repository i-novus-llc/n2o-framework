package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

/**
 * Абстрактная реализация компиляции действия
 */
public abstract class AbstractActionCompiler<D extends Action, S extends N2oAction>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    public void compileAction(D compiled, S source, CompileProcessor p) {
        compiled.setProperties(p.mapAttributes(source));
    }

    protected void initDefaults(S source, CompileProcessor p) {
        source.setId(initId(source, p));
    }

    protected String initId(S source, CompileProcessor p) {
        if (source.getId() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                IdAware component = componentScope.unwrap(IdAware.class);
                if (component != null) {
                    return component.getId();
                } else {
                    WidgetScope widgetScope = p.getScope(WidgetScope.class);
                    if (widgetScope != null) {
                        return widgetScope.getClientWidgetId() + "_row";
                    }
                }
            }
        }
        return source.getId();
    }

    protected ReduxModelEnum getModelFromComponentScope(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                return modelAware.getModel();
            }
        }
        return ReduxModelEnum.RESOLVE;
    }

    /**
     * Получение текущей страницы
     */
    protected String getPageId(CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null)
            return pageScope.getPageId();
        else
            return null;
    }
}