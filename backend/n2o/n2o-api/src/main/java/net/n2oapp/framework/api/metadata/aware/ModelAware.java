package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

/**
 * Знание о модели на клиенте
 */
public interface ModelAware extends Compiled {
    ReduxModelEnum getModel();

    void setModel(ReduxModelEnum model);
}
