package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
/**
 * Абстрактаня реализация компиляции действия
 */
public abstract class AbstractActionCompiler<D extends Action & SrcAware, S extends N2oAction>
        implements BaseSourceCompiler<D, S, CompileContext<?,?>> {

    public void compileAction(D compiled, S source, CompileProcessor p) {
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.src"), String.class)));
        if (source.getId() != null) {
            compiled.setId(source.getId());
        } else {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                IdAware component = componentScope.unwrap(IdAware.class);
                if (component != null) {
                    source.setId(component.getId());
                    compiled.setId(component.getId());
                }
            }
        }
        MetaActions widgetActions = p.getScope(MetaActions.class);
        if (widgetActions != null) {
            widgetActions.addAction(compiled);
        }
    }

    /**
     * Инициализация целевого виджета действия
     */
    protected String initTargetWidget(N2oAction source, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String targetWidgetId = null;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                targetWidgetId = pageScope == null ?
                        widgetIdAware.getWidgetId() : pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());//todo обсудить
            }
        }
        if(targetWidgetId == null) {
            if (widgetScope != null) {
                targetWidgetId = widgetScope.getClientWidgetId();
            } else if (context instanceof PageContext && ((PageContext) context).getResultWidgetId() != null) {
                targetWidgetId = pageScope.getGlobalWidgetId(((PageContext) context).getResultWidgetId());
            } else {
                throw new N2oException("Unknown widgetId for invoke action!");
            }
        }
        return targetWidgetId;
    }
}
