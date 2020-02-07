package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import org.springframework.stereotype.Component;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.Collections;
import java.util.List;

/**
 * Связывание данных на простой странице
 */
@Component
public class SimplePageBinder extends PageBinder<SimplePage> {
    @Override
    public SimplePage bind(SimplePage page, BindProcessor p) {
        if (page.getToolbar() != null) {
            for (List<Group> grp : page.getToolbar().values()) {
                grp.forEach(g -> {if (g.getButtons() != null) g.getButtons().forEach(p::bind);});
            }
        }
        return bindPage(page, p, Collections.singletonMap(page.getWidget().getId(), page.getWidget()));
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return SimplePage.class;
    }
}
