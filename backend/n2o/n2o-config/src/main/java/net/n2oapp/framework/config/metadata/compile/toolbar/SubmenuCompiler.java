package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.MenuItem;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция N2oSubmenu
 */
@Component
public class SubmenuCompiler extends BaseButtonCompiler<N2oSubmenu, Submenu> implements MetadataEnvironmentAware {

    protected ButtonGeneratorFactory buttonGeneratorFactory;

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        buttonGeneratorFactory = environment.getButtonGeneratorFactory();
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubmenu.class;
    }

    @Override
    public Submenu compile(N2oSubmenu sub, CompileContext<?, ?> context, CompileProcessor p) {
        Submenu button = new Submenu();
        button.setSrc(p.cast(sub.getSrc(), p.resolve(property("n2o.api.action.submenu.src"), String.class)));
        IndexScope idx = p.getScope(IndexScope.class);

        button.setId(sub.getId() == null ? "subMenu" + idx.get() : sub.getId());
        button.setLabel(sub.getLabel());
        button.setClassName(sub.getClassName());
        button.setStyle(StylesResolver.resolveStyles(sub.getStyle()));
        if (sub.getColor() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                N2oCell component = componentScope.unwrap(N2oCell.class);
                if (component != null) {
                    button.setColor(p.resolve(property("n2o.api.cell.toolbar.button-color"), String.class));
                }
            }
        } else {
            button.setColor(sub.getColor());
        }
        if (sub.getDescription() != null)
            button.setHint(sub.getDescription().trim());
        button.setIcon(sub.getIcon());
        button.setVisible(sub.getVisible());
        if (sub.getMenuItems() != null) {
            button.setSubMenu(Stream.of(sub.getMenuItems()).map(mi -> {
                MenuItem menuItem = new MenuItem();
                menuItem.setSrc(p.resolve(property("n2o.api.action.button.src"), String.class));
                initItem(menuItem, mi, idx, context, p);
                return menuItem;
            }).collect(Collectors.toList()));
        }
        if (sub.getGenerate() != null) {
            if (button.getSubMenu() == null) {
                button.setSubMenu(new ArrayList<>());
            }
            for (String generate : sub.getGenerate()) {
                N2oToolbar source = p.getScope(N2oToolbar.class);
                for (ToolbarItem toolbarItem : buttonGeneratorFactory.generate(generate.trim(), source, context, p)) {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setSrc(p.resolve(property("n2o.api.action.button.src"), String.class));
                    button.getSubMenu().add(menuItem);
                    initItem(menuItem, (N2oButton) toolbarItem, idx, context, p);
                }
            }
        }

        return button;
    }
}
