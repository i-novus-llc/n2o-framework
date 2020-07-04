package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.page.Dialog;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DialogBinder implements BaseMetadataBinder<Dialog> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Dialog.class;
    }

    @Override
    public Dialog bind(Dialog dialog, BindProcessor p) {
        if (dialog.getToolbar() != null) {
            for (List<Group> grp : dialog.getToolbar().values()) {
                grp.forEach(g -> {if (g.getButtons() != null) g.getButtons().forEach(p::bind);});
            }
        }
        return dialog;
    }
}
