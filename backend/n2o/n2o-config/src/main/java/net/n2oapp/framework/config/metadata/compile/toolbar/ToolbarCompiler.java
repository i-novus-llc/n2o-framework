package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oGroup;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.util.StylesResolver;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.toolbar.ButtonCompileUtil.generateButtons;

/**
 * Компиляция тулбара
 */
@Component
public class ToolbarCompiler implements BaseSourceCompiler<Toolbar, N2oToolbar, CompileContext<?, ?>>, SourceClassAware, MetadataEnvironmentAware {

    protected ButtonGeneratorFactory buttonGeneratorFactory;

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        buttonGeneratorFactory = environment.getButtonGeneratorFactory();
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oToolbar.class;
    }

    @Override
    public Toolbar compile(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p) {
        Toolbar toolbar = new Toolbar();
        List<Group> groups = new ArrayList<>();
        int groupIndex = 0;
        String toolbarPlace = getPlace(source, p);

        fillGroups(source, context, p, groups, groupIndex, toolbarPlace);
        if (ArrayUtils.isNotEmpty(source.getGenerate()))
            groups.add(compileGeneratedButtons(source, context, p, groupIndex, toolbarPlace));

        if (!groups.isEmpty()) {
            toolbar.put(toolbarPlace, groups);
        }

        return toolbar;
    }

    private void fillGroups(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p, List<Group> groups, int groupIndex, String toolbarPlace) {
        if (ArrayUtils.isEmpty(source.getItems()))
            return;

        List<ToolbarItem> toolbarItems = List.of(source.getItems());
        int itemIndex = 0;
        while (itemIndex < toolbarItems.size()) {
            Group group = initGroup(source, toolbarPlace, groupIndex++);
            if (toolbarItems.get(itemIndex) instanceof N2oGroup groupItem) {
                group.setButtons(compileButtonsOfGroup(source, context, p, groupItem));
                itemIndex++;
            } else {
                itemIndex = compileSingleButtons(source, context, p, toolbarItems, group, itemIndex);
            }
            groups.add(group);
        }
    }

    private int compileSingleButtons(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p, List<ToolbarItem> toolbarItems, Group group, int itemIndex) {
        List<AbstractButton> buttons = new ArrayList<>();
        boolean shouldBeGrouped = Boolean.FALSE.equals(shouldBeGrouped(p));

        while (itemIndex < toolbarItems.size() && !(toolbarItems.get(itemIndex) instanceof N2oGroup)) {
            buttons.add(p.compile(toolbarItems.get(itemIndex), context, source));
            itemIndex++;
            if (shouldBeGrouped) break;
        }
        group.setButtons(buttons);

        return itemIndex;
    }

    private ArrayList<AbstractButton> compileButtonsOfGroup(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p, N2oGroup item) {
        ArrayList<AbstractButton> result = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(item.getGenerate())) {
            result.addAll(generateButtons(item, source, buttonGeneratorFactory, context, p));
        }
        if (item.getItems() == null)
            return result;
        for (int i = 0; i < item.getItems().length; i++) {
            result.add(p.compile(item.getItems()[i], context, source));
        }

        return result;
    }

    private Group compileGeneratedButtons(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p, int groupIndex, String toolbarPlace) {
        Group group = initGroup(source, toolbarPlace, groupIndex);
        List<AbstractButton> generatedButtons = generateButtons(source, source, buttonGeneratorFactory, context, p);
        group.setButtons(generatedButtons);

        return group;
    }

    private Group initGroup(N2oToolbar source, String place, Integer groupIndex) {
        Group group = new Group(place + groupIndex);
        group.setClassName(source.getCssClass());
        group.setStyle(StylesResolver.resolveStyles(source.getStyle()));

        return group;
    }

    private static String getPlace(N2oToolbar source, CompileProcessor p) {
        ToolbarPlaceScope toolbarPlaceScope = p.getScope(ToolbarPlaceScope.class);
        String defaultPlace = toolbarPlaceScope != null
                ? toolbarPlaceScope.getPlace()
                : p.resolve(property("n2o.api.widget.toolbar.place"), String.class);

        return castDefault(source.getPlace(), defaultPlace);
    }

    private Boolean shouldBeGrouped(CompileProcessor p) {
        return castDefault(
                p.resolve(property("n2o.api.toolbar.grouping"), Boolean.class), true);
    }
}