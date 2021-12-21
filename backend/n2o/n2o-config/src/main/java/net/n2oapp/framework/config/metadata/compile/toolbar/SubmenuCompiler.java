package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция кнопки с выпадающим меню
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
    public Submenu compile(N2oSubmenu source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        Submenu submenu = new Submenu();
        IndexScope idx = p.getScope(IndexScope.class);
        compileBase(submenu, source, idx, context, p);
        submenu.setId(source.getId() == null ? "subMenu" + idx.get() : source.getId());
        source.setId(submenu.getId());
        submenu.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.submenu.src"), String.class)));
        submenu.setShowToggleIcon(p.cast(source.getShowToggleIcon(), true));
        submenu.setVisible(source.getVisible());

        initMenuItems(source, submenu, idx, context, p);
        initGenerate(source, submenu, idx, context, p);

        return submenu;
    }

    private void initMenuItems(N2oSubmenu source, Submenu button, IndexScope idx,
                               CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getMenuItems() != null) {
            button.setSubMenu(Stream.of(source.getMenuItems())
                    .map(mi -> {
                        PerformButton menuItem = p.compile(mi, context, p, idx);
                        menuItem.setColor(null);
                        return menuItem;
                    })
                    .collect(Collectors.toList()));
        }
    }

    private void initGenerate(N2oSubmenu sub, Submenu button, IndexScope idx,
                              CompileContext<?, ?> context, CompileProcessor p) {
        if (sub.getGenerate() != null) {
            if (button.getSubMenu() == null) {
                button.setSubMenu(new ArrayList<>());
            }
            for (String generate : sub.getGenerate()) {
                N2oToolbar source = p.getScope(N2oToolbar.class);
                for (ToolbarItem toolbarItem : buttonGeneratorFactory.generate(generate.trim(), source, context, p)) {
                    PerformButton menuItem = p.compile(toolbarItem, context, p, idx);
                    menuItem.setColor(null);
                    button.getSubMenu().add(menuItem);
                }
            }
        }
    }
}
