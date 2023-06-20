package net.n2oapp.framework.config.metadata.validation.standard.regions;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ScrollspyValidator implements SourceValidator<N2oScrollspyRegion>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oScrollspyRegion.class;
    }

    @Override
    public void validate(N2oScrollspyRegion source, SourceProcessor p) {
        N2oScrollspyRegion.AbstractMenuItem[] items = source.getMenu();
        p.checkIdsUnique(collectItems(items), String.format("Меню '%s' встречается более одного раза в <scrollspy> '%s'", "%s", source.getId()));

    }

    private List<N2oScrollspyRegion.AbstractMenuItem> collectItems(N2oScrollspyRegion.AbstractMenuItem[] items) {
        List<N2oScrollspyRegion.AbstractMenuItem> itemsList = new ArrayList<>();
        Arrays.stream(items).forEach(item -> {
            itemsList.add(item);
            if (item instanceof N2oScrollspyRegion.GroupItem)
                itemsList.addAll(collectItems(((N2oScrollspyRegion.GroupItem) item).getGroup()));
            if (item instanceof N2oScrollspyRegion.SubMenuItem)
                itemsList.addAll(collectItems(((N2oScrollspyRegion.SubMenuItem) item).getSubMenu()));
        });
        return itemsList;
    }
}
