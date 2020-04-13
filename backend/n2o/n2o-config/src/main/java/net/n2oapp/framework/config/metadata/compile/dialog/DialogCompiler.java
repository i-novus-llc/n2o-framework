package net.n2oapp.framework.config.metadata.compile.dialog;

import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.page.Dialog;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.DialogContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

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
        Toolbar toolbar = new Toolbar();
        IndexScope indexScope = new IndexScope();
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setClientWidgetId(context.getParentWidgetId());
        CompiledObject object = new CompiledObject();
        object.setId(context.getObjectId());
        ParentRouteScope pageRouteScope = new ParentRouteScope(context.getRoute((N2oCompileProcessor) p),
                context.getPathRouteMapping(), context.getQueryRouteMapping());
        if (source.getLeftButtons() != null) {
            N2oToolbar sourceToolbar = new N2oToolbar();
            sourceToolbar.setPlace("bottomLeft");
            sourceToolbar.setItems(source.getLeftButtons());
            toolbar.putAll(p.compile(sourceToolbar, context, indexScope, widgetScope, object, pageRouteScope));
        }
        if (source.getRightButtons() != null) {
            N2oToolbar sourceToolbar = new N2oToolbar();
            sourceToolbar.setPlace("bottomRight");
            sourceToolbar.setItems(source.getRightButtons());
            toolbar.putAll(p.compile(sourceToolbar, context, indexScope, widgetScope, object, pageRouteScope));
        }
        dialog.setToolbar(toolbar);
        return dialog;
    }
}