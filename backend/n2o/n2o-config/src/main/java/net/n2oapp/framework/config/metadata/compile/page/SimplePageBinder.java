package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SimplePageBinder extends PageBinder<SimplePage> {
    @Override
    public SimplePage bind(SimplePage page, BindProcessor p) {
        super.bindPage(page, p, Collections.singletonMap(page.getWidget().getId(), page.getWidget()));
        return page;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return SimplePage.class;
    }
}
