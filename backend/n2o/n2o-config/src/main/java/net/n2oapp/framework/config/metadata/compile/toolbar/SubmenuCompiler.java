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
    public Submenu compile(N2oSubmenu source, CompileContext<?, ?> context, CompileProcessor p) {
        Submenu submenu = new Submenu();
        IndexScope idx = p.getScope(IndexScope.class);
        initItem(submenu, source, idx, context, p);
        submenu.setId(source.getId() == null ? "subMenu" + idx.get() : source.getId());
        source.setId(submenu.getId());
        submenu.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.submenu.src"), String.class)));
        submenu.setShowToggleIcon(p.cast(source.getShowToggleIcon(), true));
        initMenuItems(source, context, p, submenu, idx);
        initGenerate(source, context, p, submenu, idx);

        return submenu;
    }

    private void initMenuItems(N2oSubmenu sub, CompileContext<?, ?> context, CompileProcessor p,
                               Submenu button, IndexScope idx) {
        if (sub.getMenuItems() != null) {
            button.setSubMenu(Stream.of(sub.getMenuItems())
                    .map(mi -> (PerformButton) p.compile(mi, context, p, idx))
                    .collect(Collectors.toList()));
        }
    }

    private void initGenerate(N2oSubmenu sub, CompileContext<?, ?> context, CompileProcessor p,
                              Submenu button, IndexScope idx) {
        if (sub.getGenerate() != null) {
            if (button.getSubMenu() == null) {
                button.setSubMenu(new ArrayList<>());
            }
            for (String generate : sub.getGenerate()) {
                N2oToolbar source = p.getScope(N2oToolbar.class);
                for (ToolbarItem toolbarItem : buttonGeneratorFactory.generate(generate.trim(), source, context, p)) {
                    PerformButton toolbarButton = p.compile(toolbarItem, context, p, idx);
                    button.getSubMenu().add(toolbarButton);
                }
            }
        }
    }
}
