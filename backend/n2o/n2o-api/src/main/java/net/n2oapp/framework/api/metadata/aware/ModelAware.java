package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;

/**
 * Знание о модели на клиенте
 */
public interface ModelAware extends Compiled {
    ReduxModel getModel();
}
