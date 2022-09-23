package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Component
public class ToolbarCellCompiler extends AbstractCellCompiler<ToolbarCell, N2oToolbarCell> {

    @Override
    public ToolbarCell compile(N2oToolbarCell source, CompileContext<?, ?> context, CompileProcessor p) {
        ToolbarCell cell = new ToolbarCell();
        build(cell, source, context, p, property("n2o.api.cell.toolbar.src"));
        compileButtons(cell, source, context, p);
        return cell;
    }

    private void compileButtons(ToolbarCell cell, N2oToolbarCell source, CompileContext<?, ?> context, CompileProcessor p) {
        Toolbar toolbar = p.compile(new N2oToolbar(source.getGenerate(), source.getItems()), context, new ComponentScope(source));
        List<Group> groups = new ArrayList<>();
        toolbar.values().stream().filter(Objects::nonNull).forEach(g ->
            g.forEach(group -> {
                group.setId(null);
                groups.add(group);
            }));
        List<AbstractButton> buttons = new ArrayList<>();
        groups.stream().filter(g -> g.getButtons() != null).forEach(g -> buttons.addAll(g.getButtons()));

        cell.setToolbar(groups);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oToolbarCell.class;
    }
}