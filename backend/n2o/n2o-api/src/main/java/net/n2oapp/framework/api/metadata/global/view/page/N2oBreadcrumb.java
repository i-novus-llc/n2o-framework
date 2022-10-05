package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель навигационной цепочки
 */
@Getter
@Setter
public class N2oBreadcrumb implements Source {
    private String label;
    private String path;
}
