package net.n2oapp.framework.api.metadata.global.view.widget.dependency;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.N2oFileAware;

/**
 * Зависимости
 */
@Getter
@Setter
public class N2oDependency implements Source, N2oFileAware {
    private ReduxModelEnum model;
    private String value;
    private String datasource;
    private String file;
}