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
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция тулбара
 */
@Component
public class ToolbarCompiler implements BaseSourceCompiler<Toolbar, N2oToolbar, CompileContext<?, ?>>, SourceClassAware, MetadataEnvironmentAware {

    protected ButtonGeneratorFactory buttonGeneratorFactory;

    @Override
    public Toolbar compile(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p) {
        Toolbar toolbar = new Toolbar();
        initGenerate(source, context, p);
        if (source.getItems() == null) {
            return toolbar;
        }


        ToolbarPlaceScope toolbarPlaceScope = p.getScope(ToolbarPlaceScope.class);
        String defaultPlace = toolbarPlaceScope != null ? toolbarPlaceScope.getPlace() :
                p.resolve(property("n2o.api.widget.toolbar.place"), String.class);
        String place = p.cast(source.getPlace(), defaultPlace);

        List<Group> groups = new ArrayList<>();
        initGroups(source, context, p, groups, place);
        toolbar.put(place, groups);
        return toolbar;
    }

    private void initGroups(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p, List<Group> groups, String place) {
        int gi = 0;
        int i = 0;
        Boolean buttonGrouping = isGrouping(p);
        while (i < source.getItems().length) {
            Group gr = new Group(place + gi++);
            List<AbstractButton> buttons = new ArrayList<>();
            ToolbarItem item = source.getItems()[i];
            if (item instanceof N2oGroup) {
                N2oGroup group = (N2oGroup) item;
                if (group.getGenerate() != null) {
                    for (String generate : group.getGenerate()) {
                        buttonGeneratorFactory.generate(generate.trim(), source, context, p)
                                .forEach(j -> buttons.add(getButton(source, j, context, p)));
                    }
                } else {
                    for (GroupItem it : group.getItems()) {
                        buttons.add(getButton(source, it, context, p));
                    }
                }
                i++;
            } else {
                while (i < source.getItems().length && !(source.getItems()[i] instanceof N2oGroup)) {
                    buttons.add(getButton(source, source.getItems()[i], context, p));
                    i++;
                    if (!buttonGrouping) break;
                }
            }
            gr.setButtons(buttons);
            groups.add(gr);
        }
    }

    protected void initGenerate(N2oToolbar source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getGenerate() != null) {
            for (String generate : source.getGenerate()) {
                buttonGeneratorFactory.generate(generate.trim(), source, context, p)
                        .forEach(i -> source.setItems(push(source, (N2oButton) i)));
            }
        }
    }

    private AbstractButton getButton(N2oToolbar source, ToolbarItem item, CompileContext<?, ?> context, CompileProcessor p) {
        IndexScope scope = p.getScope(IndexScope.class);
        scope = scope == null ? new IndexScope() : scope;
        return p.compile(item, context, source, scope);
    }

    private ToolbarItem[] push(N2oToolbar source, N2oButton button) {
        ToolbarItem[] items;
        if (source.getItems() == null) {
            items = new ToolbarItem[1];
            items[0] = button;
        } else {
            int length = source.getItems().length;
            items = new ToolbarItem[length + 1];
            System.arraycopy(source.getItems(), 0, items, 0, length);
            items[length] = button;
        }
        return items;
    }

    private Boolean isGrouping(CompileProcessor p) {
        Object buttonGrouping = p.resolve(property("n2o.api.toolbar.grouping"));
        return buttonGrouping instanceof Boolean ? (Boolean) buttonGrouping : true;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        buttonGeneratorFactory = environment.getButtonGeneratorFactory();
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oToolbar.class;
    }
}

