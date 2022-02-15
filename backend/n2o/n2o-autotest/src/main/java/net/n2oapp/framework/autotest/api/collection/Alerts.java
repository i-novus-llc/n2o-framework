package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.snippet.Alert;

/**
 * Сообщения alerts для автотестирования
 */
public interface Alerts extends ComponentsCollection {

    Alert alert(int index);
}
