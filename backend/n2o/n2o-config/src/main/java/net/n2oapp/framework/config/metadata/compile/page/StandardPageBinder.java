package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil.getCompiledWidgets;

/**
 * Связывание данных на обычной странице
 */
@Component
public class StandardPageBinder extends PageBinder<StandardPage> {
    @Override
    public StandardPage bind(StandardPage page, BindProcessor p) {
        if (page.getToolbar() != null) {
            for (List<Group> grp : page.getToolbar().values()) {
                grp.forEach(g -> {
                    if (g.getButtons() != null) g.getButtons().forEach(p::bind);
                });
            }
        }

        return bindPage(page, p, getCompiledWidgets(page));
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return StandardPage.class;
    }
}
