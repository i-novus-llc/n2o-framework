package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Исходная модель "Тулбар на строках таблицы"
 */
@Getter
@Setter
public class N2oRowOverlay implements Source {
    private String className;
    private N2oToolbar toolbar;
}
