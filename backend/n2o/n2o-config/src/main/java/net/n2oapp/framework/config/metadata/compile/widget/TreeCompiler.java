package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.Tree;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

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
        N2oDatasource datasource = initInlineDatasource(tree, source, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(tree, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModel.resolve, p.getScope(PageScope.class));
        MetaActions widgetActions = initMetaActions(source, p);
        compileToolbarAndAction(tree, source, context, p, widgetScope, widgetActions, object, null);

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
    protected N2oDatasource initInlineDatasource(Tree compiled, N2oTree source, CompileProcessor p) {
        N2oDatasource datasource = super.initInlineDatasource(compiled, source, p);
        datasource.setSize(p.resolve(property("n2o.api.widget.tree.size"), Integer.class));
        return datasource;
    }
}
