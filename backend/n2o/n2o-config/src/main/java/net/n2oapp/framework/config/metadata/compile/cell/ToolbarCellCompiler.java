package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.toolbar.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class ToolbarCellCompiler extends AbstractCellCompiler<ToolbarCell, N2oToolbarCell> {

    @Override
    public ToolbarCell compile(N2oToolbarCell source, CompileContext<?, ?> context, CompileProcessor p) {
        ToolbarCell cell = new ToolbarCell();
        build(cell, source, context, p, property("n2o.default.cell.toolbar.src"));
        compileButtons(cell, source, context, p);
        return cell;
    }

    private void compileButtons(ToolbarCell cell, N2oToolbarCell source, CompileContext<?, ?> context, CompileProcessor p) {
        Toolbar toolbar = p.compile(new N2oToolbar(source.getGenerate(), source.getItems()), context);
        List<Button> buttons = toolbar.getGroup(0).getButtons();
        MetaActions metaActions = p.getScope(MetaActions.class);
        if (metaActions != null) {
            for (Button button : buttons)
                initActions(button, metaActions);
        }
        cell.setButtons(buttons);
    }

    private void initActions(MenuItem menuItem, MetaActions actions) {
        if (menuItem instanceof Button && ((Button) menuItem).getSubMenu() != null) {
            for (MenuItem item : ((Button) menuItem).getSubMenu()) {
                initActions(item, actions);
            }
        }
        if (menuItem.getActionId() != null && actions.containsKey(menuItem.getActionId()))
            menuItem.setAction(actions.get(menuItem.getActionId()));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oToolbarCell.class;
    }
}