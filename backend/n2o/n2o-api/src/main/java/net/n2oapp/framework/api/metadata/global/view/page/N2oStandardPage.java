package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;

import static net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil.collectWidgets;


/**
 * Исходная модель стандартной страницы
 */
@Getter
@Setter
public class N2oStandardPage extends N2oBasePage {
    private SourceComponent[] items;

    @Override
    public List<N2oWidget> getWidgets() {
        return collectWidgets(items);
    }
}
