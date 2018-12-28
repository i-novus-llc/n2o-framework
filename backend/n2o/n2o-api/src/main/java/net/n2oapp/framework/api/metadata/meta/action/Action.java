package net.n2oapp.framework.api.metadata.meta.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;

import net.n2oapp.framework.api.metadata.aware.SrcAware;

/**
 * Клиентская модель действия
 */
public interface Action extends Compiled, IdAware, SrcAware, PropertiesAware {

}
