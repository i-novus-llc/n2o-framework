package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oSubmenu;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.generateButtons;

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
        compileBase(submenu, source, context, p);
        submenu.setId(source.getId() == null ? "subMenu" + idx.get() : source.getId());
        source.setId(submenu.getId());
        submenu.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.action.submenu.src"), String.class)));
        submenu.setShowToggleIcon(castDefault(source.getShowToggleIcon(),
                () -> p.resolve(property("n2o.api.submenu.show_toggle_icon"), Boolean.class)));

        compileCondition(source, submenu, p, p.getScope(ComponentScope.class));

        initMenuItems(source, submenu, idx, context, p);
        initGenerate(source, submenu, context, p);

        return submenu;
    }

    private void initMenuItems(N2oSubmenu source, Submenu button, IndexScope idx,
                               CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getMenuItems() != null) {
            button.setSubMenu(Stream.of(source.getMenuItems())
                    .map(mi -> {
                        if (mi.getDatasourceId() == null)
                            mi.setDatasourceId(source.getDatasourceId());
                        if (mi.getModel() == null)
                            mi.setModel(source.getModel());
                        mi.setGeneratedForSubMenu(true);
                        PerformButton menuItem = p.compile(mi, context, p, idx);
                        menuItem.setColor(null);

                        return menuItem;
                    })
                    .collect(Collectors.toList()));
        }
    }

    private void initGenerate(N2oSubmenu sub, Submenu button,
                              CompileContext<?, ?> context, CompileProcessor p) {
        if (!ArrayUtils.isEmpty(sub.getGenerate())) {
            if (button.getSubMenu() == null)
                button.setSubMenu(new ArrayList<>());

            N2oToolbar toolbar = p.getScope(N2oToolbar.class);
            toolbar.setIsGeneratedForSubMenu(true);

            List<AbstractButton> generatedButtons = generateButtons(sub, toolbar, buttonGeneratorFactory, context, p);
            button.getSubMenu().addAll(
                    generatedButtons.stream()
                            .map(b -> (PerformButton) b)
                            .peek(b -> b.setColor(null))
                            .collect(Collectors.toList())
            );
            if (Arrays.asList(sub.getGenerate()).contains("tableSettings")) {
                button.setIcon(p.resolve(property("n2o.api.generate.button.tableSettings.icon"), String.class));
            }
        }
    }
}
