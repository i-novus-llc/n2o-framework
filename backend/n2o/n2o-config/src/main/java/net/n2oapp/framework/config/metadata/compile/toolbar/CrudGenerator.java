package net.n2oapp.framework.config.metadata.compile.toolbar;

import lombok.Setter;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Генерация crud кнопок
 */
@Component
public class CrudGenerator implements ButtonGenerator {

    @Setter
    private ButtonGeneratorFactory buttonGeneratorFactory;

    @Override
    public String getCode() {
        return "crud";
    }

    @Override
    public List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        if (p.getScope(CompiledObject.class) != null) {
            List<ToolbarItem> items = new ArrayList<>();
            for (DefaultActions action : DefaultActions.values()) {
                List<ToolbarItem> toolbarItems = buttonGeneratorFactory.generate(action.name(), toolbar, context, p);
                if (toolbarItems != null)
                    items.addAll(toolbarItems);
            }
            return items;
        } else {
            return null;
        }
    }
}
