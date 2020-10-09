package net.n2oapp.framework.config.metadata.compile.dialog;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.page.Dialog;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.DialogContext;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция диалога подтверждения действий
 */
@Component
public class DialogCompiler implements BaseSourceCompiler<Dialog, N2oDialog, DialogContext> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDialog.class;
    }

    @Override
    public Dialog compile(N2oDialog source, DialogContext context, CompileProcessor p) {
        Dialog dialog = new Dialog();
        dialog.setTitle(source.getTitle());
        dialog.setDescription(source.getDescription());
        dialog.setSize(source.getSize());
        CompiledObject object = p.getCompiled(new ObjectContext(context.getObjectId()));
        if (source.getToolbar() != null) {
            ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.dialog.toolbar.place"), String.class));
            WidgetScope widgetScope = new WidgetScope();
            widgetScope.setClientWidgetId(context.getParentWidgetId());
            ParentRouteScope pageRouteScope = new ParentRouteScope(context.getRoute((N2oCompileProcessor) p),
                    context.getPathRouteMapping(), context.getQueryRouteMapping());
            Toolbar toolbar = p.compile(source.getToolbar(), context, new IndexScope(), widgetScope, object,
                    pageRouteScope, toolbarPlaceScope);
            dialog.setToolbar(toolbar);
        }
        return dialog;
    }
}