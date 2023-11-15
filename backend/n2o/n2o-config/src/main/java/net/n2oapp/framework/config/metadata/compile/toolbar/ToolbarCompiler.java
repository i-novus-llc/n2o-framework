package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
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

        ToolbarPlaceScope toolbarPlaceScope = p.getScope(ToolbarPlaceScope.class);
        String defaultPlace = toolbarPlaceScope != null ? toolbarPlaceScope.getPlace() :
                p.resolve(property("n2o.api.widget.toolbar.place"), String.class);
        String place = castDefault(source.getPlace(), defaultPlace);

        groupIndex = initGenerate(source, context, p, groups, groupIndex, place);
        initGroups(source, context, p, groups, place, groupIndex);

        if (!groups.isEmpty())
            toolbar.put(place, groups);

        return toolbar;
    }

    private int initGenerate(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p, List<Group> groups, int groupIndex, String place) {
        if (!ArrayUtils.isEmpty(source.getGenerate())) {
            Group group = initGroup(source, place, groupIndex++);
            List<AbstractButton> generatedButtons = generateButtons(source, source, buttonGeneratorFactory, context, p);
            group.setButtons(generatedButtons);
            groups.add(group);
        }

        return groupIndex;
    }

    private void initGroups(N2oToolbar toolbar, CompileContext<?, ?> context, CompileProcessor p, List<Group> groups, String place, int gi) {
        if (toolbar.getItems() == null)
            return;

        Boolean buttonGrouping = isGrouping(p);
        int i = 0;
        while (i < toolbar.getItems().length) {
            Group gr = initGroup(toolbar, place, gi++);
            List<AbstractButton> buttons = new ArrayList<>();
            ToolbarItem item = toolbar.getItems()[i];
            if (item instanceof N2oGroup) {
                N2oGroup group = (N2oGroup) item;
                if (!ArrayUtils.isEmpty(group.getGenerate())) {
                    buttons.addAll(generateButtons(group, toolbar, buttonGeneratorFactory, context, p));
                } else if (group.getItems() != null) {
                    for (GroupItem it : group.getItems()) {
                        buttons.add(p.compile(it, context, toolbar));
                    }
                }
                i++;
            } else {
                while (i < toolbar.getItems().length && !(toolbar.getItems()[i] instanceof N2oGroup)) {
                    buttons.add(p.compile(toolbar.getItems()[i], context, toolbar));
                    i++;
                    if (!buttonGrouping) break;
                }
            }
            gr.setButtons(buttons);
            groups.add(gr);
        }
    }

    private Group initGroup(N2oToolbar source, String place, int gi) {
        Group group = new Group(place + gi);
        group.setClassName(source.getCssClass());
        group.setStyle(StylesResolver.resolveStyles(source.getStyle()));

        return group;
    }

    private Boolean isGrouping(CompileProcessor p) {
        Object buttonGrouping = p.resolve(property("n2o.api.toolbar.grouping"));

        return buttonGrouping instanceof Boolean ? (Boolean) buttonGrouping : true;
    }
}
