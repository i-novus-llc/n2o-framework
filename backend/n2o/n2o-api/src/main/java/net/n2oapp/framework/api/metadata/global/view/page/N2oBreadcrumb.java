package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель навигационной цепочки
 */
@Getter
@Setter
public class N2oBreadcrumb {
    private String label;
    private String path;
}
