package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActionsEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteGenerator extends AbstractButtonGenerator {
    @Override
    public String getCode() {
        return "delete";
    }

    @Override
    public List<ToolbarItem> generate(N2oToolbar toolbar, CompileContext context, CompileProcessor p) {
        return build(DefaultActionsEnum.DELETE, p);
    }
}
