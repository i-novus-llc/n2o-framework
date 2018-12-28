package net.n2oapp.framework.api.metadata.global.view.action.control;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 30.01.14
 * Time: 17:50
 * Некоторый активный элеметн управления, знающий ID дествия который он выполняет.
 */
public interface OperationIdAware extends Serializable {
    String getOperationId();
}
