package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;

/**
 * Стандартный виджет для автотестирования
 */
public interface StandardWidget extends Widget {

    /**
     * @return Панель кнопок виджета для автотестирования
     */
    WidgetToolbar toolbar();

    /**
     * @return Предупреждения для автотестирования
     */
    Alerts alerts();

    /**
     * Панель кнопок виджета для автотестирования
     */
    interface WidgetToolbar {
        /**
         * Возвращает левую верхнюю панель кнопок
         * @return Панель кнопок для автотестирования
         */
        Toolbar topLeft();

        /**
         * Возвращает правую верхнюю панель кнопок
         * @return Панель кнопок для автотестирования
         */
        Toolbar topRight();

        /**
         * Нижнюю левую верхнюю панель кнопок
         * @return Панель кнопок для автотестирования
         */
        Toolbar bottomLeft();

        /**
         * Нижнюю правую верхнюю панель кнопок
         * @return Панель кнопок для автотестирования
         */
        Toolbar bottomRight();
    }
}
