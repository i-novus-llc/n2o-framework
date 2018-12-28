package net.n2oapp.framework.api.metadata.dataprovider;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;

import java.io.Serializable;

/**
 * Модель провайдера данных
 */
public interface DataProvider extends N2oInvocation, Serializable, NamespaceUriAware {

}
