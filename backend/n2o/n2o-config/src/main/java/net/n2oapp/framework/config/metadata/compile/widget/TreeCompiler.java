package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.Tree;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.springframework.stereotype.Component;

/**
 * Компиляция виджета дерево
 */
@Component
public class TreeCompiler extends BaseWidgetCompiler<Tree, N2oTree> {
    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.tree.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTree.class;
    }

    @Override
    public Tree compile(N2oTree source, CompileContext<?, ?> context, CompileProcessor p) {
        Tree tree = new Tree();
        CompiledObject object = getObject(source, p);
        compileWidget(tree, source, context, p, object);
        ParentRouteScope widgetRoute = initWidgetRouteScope(tree, context, p);
        PageRoutesScope pageRoutesScope = p.getScope(PageRoutesScope.class);
        if (pageRoutesScope != null) {
            pageRoutesScope.put(tree.getId(), widgetRoute);
        }
        compileDataProviderAndRoutes(tree, source, context, p, null, widgetRoute, null, null, object);
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setWidgetId(source.getId());
        widgetScope.setQueryId(source.getQueryId());
        widgetScope.setClientWidgetId(tree.getId());
        MetaActions widgetActions = initMetaActions(source);
        compileToolbarAndAction(tree, source, context, p, widgetScope, widgetRoute, widgetActions, object, null);

        tree.setFetchOnInit(source.getFetchOnInit());
        tree.setParentFieldId(p.resolveJS(source.getParentFieldId()));
        tree.setValueFieldId(p.resolveJS(source.getValueFieldId()));
        tree.setChildrenFieldId(p.resolveJS(source.getHasChildrenFieldId()));
        tree.setLabelFieldId(p.resolveJS(source.getLabelFieldId()));
        tree.setIconFieldId(p.resolveJS(source.getIconFieldId()));
        tree.setImageFieldId(p.resolveJS(source.getImageFieldId()));
        tree.setBadgeFieldId(p.resolveJS(source.getBadgeFieldId()));
        tree.setBadgeColorFieldId(p.resolveJS(source.getBadgeColorFieldId()));
        tree.setMultiselect(source.getMultiselect());
        tree.setHasCheckboxes(source.getCheckboxes());
        tree.setAjax(source.getAjax());
        return tree;
    }

    @Override
    protected QueryContext getQueryContext(Tree widget, N2oTree source, CompileContext<?, ?> context, String route,
                                           ValidationList validationList, SubModelsScope subModelsScope,
                                           CopiedFieldScope copiedFieldScope, CompiledObject object) {
        QueryContext queryContext = super.getQueryContext(widget, source, context, route, validationList, subModelsScope, copiedFieldScope, object);
        queryContext.setQuerySize(source.getSize() != null ? source.getSize() : 200);
        return queryContext;
    }
}
