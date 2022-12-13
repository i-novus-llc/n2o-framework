package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.VisualAttribute;

/**
 * Исходная модель навигационной цепочки
 */
@Getter
@Setter
public class N2oBreadcrumb implements Source {
    @VisualAttribute
    private String label;
    @VisualAttribute
    private String path;
}
