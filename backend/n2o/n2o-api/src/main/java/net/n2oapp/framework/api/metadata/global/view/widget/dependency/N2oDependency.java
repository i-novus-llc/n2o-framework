package net.n2oapp.framework.api.metadata.global.view.widget.dependency;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Зависимости
 */
@Getter
@Setter
public class N2oDependency implements Source {
    private ReduxModelEnum model;
    private String value;
    private String datasource;
}
