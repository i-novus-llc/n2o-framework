package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель навигационной цепочки
 */
@Getter
@Setter
public class N2oBreadcrumb implements Source {
    @N2oAttribute("Заголовок")
    private String label;
    @N2oAttribute("Путь")
    private String path;
}
