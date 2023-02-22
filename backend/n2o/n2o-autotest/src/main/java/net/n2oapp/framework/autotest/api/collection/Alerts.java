package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.snippet.Alert;

/**
 * Сообщения alerts для автотестирования
 */
public interface Alerts extends ComponentsCollection {

    /**
     * <p>
     *     Возвращает предупреждение по индексу из списка предупреждений на странице
     * </p>
     *
     * <p>For example: {@code
     *    alerts(Alert.Placement.topLeft).alert(0);
     * }</p>
     *
     * @param index номер требуемого предупреждения
     * @return Компонент предупреждение для автотестирования
     */
    Alert alert(int index);
}
