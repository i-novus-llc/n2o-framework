package net.n2oapp.framework.api.metadata.global.view.widget.dependency;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Зависимости
 */
@Getter
@Setter
public class N2oDependency implements Source {
    private ReduxModel model;
    private String value;
    private String datasource;
}
