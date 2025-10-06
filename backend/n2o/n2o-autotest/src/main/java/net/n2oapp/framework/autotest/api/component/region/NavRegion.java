package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент региона навигации (nav) для автотестирования.
 * Предоставляет доступ к элементам навигационного меню и их проверкам.
 */
public interface NavRegion extends Region {

    /**
     * Получить содержимое региона навигации
     *
     * @return Коллекция элементов навигации
     */
    RegionItems content();

    /**
     * Интерфейс для работы с коллекцией элементов навигации
     */
    interface RegionItems {
        /**
         * Получить элемент навигации по индексу с приведением типа
         *
         * @param index Индекс элемента (начиная с 0)
         * @param clazz Класс ожидаемого типа элемента
         * @param <T>   Тип элемента, наследуемый от NavRegionItem
         * @return Элемент навигации заданного типа
         */
        <T extends NavRegionItem> T item(int index, Class<T> clazz);

        /**
         * Проверить количество элементов в коллекции
         *
         * @param size Ожидаемое количество элементов
         */
        void shouldHaveSize(int size);
    }

    /**
     * Базовый интерфейс элемента навигации
     */
    interface NavRegionItem extends Component {
        /**
         * Проверить текст метки элемента
         *
         * @param label Ожидаемый текст метки
         */
        void shouldHaveLabel(String label);

        /**
         * Кликнуть по элементу
         */
        void click();
    }

    /**
     * Элемент-ссылка навигационного меню
     */
    interface AnchorItem extends NavRegionItem {
        /**
         * Проверить URL ссылки
         *
         * @param url Ожидаемый URL
         */
        void shouldHaveUrl(String url);
    }

    /**
     * Группа элементов навигационного меню
     */
    interface GroupItem extends NavRegionItem {
        /**
         * Получить дочерние элементы группы
         *
         * @return Коллекция дочерних элементов
         */
        RegionItems items();

        /**
         * Проверить количество дочерних элементов
         *
         * @param size Ожидаемое количество элементов
         */
        void shouldHaveSize(int size);

        /**
         * Получить дочерний элемент по индексу с приведением типа
         *
         * @param index Индекс элемента (начиная с 0)
         * @param clazz Класс ожидаемого типа элемента
         * @param <T>   Тип элемента, наследуемый от NavRegionItem
         * @return Дочерний элемент заданного типа
         */
        <T extends NavRegionItem> T item(int index, Class<T> clazz);
    }

    /**
     * Выпадающее меню навигации
     */
    interface DropdownItem extends NavRegionItem {
        /**
         * Получить элементы выпадающего меню
         *
         * @return Коллекция элементов меню
         */
        RegionItems items();

        /**
         * Проверить количество элементов в выпадающем меню
         *
         * @param size Ожидаемое количество элементов
         */
        void shouldHaveSize(int size);

        /**
         * Получить элемент меню по индексу с приведением типа
         *
         * @param index Индекс элемента (начиная с 0)
         * @param clazz Класс ожидаемого типа элемента
         * @param <T>   Тип элемента, наследуемый от NavRegionItem
         * @return Элемент меню заданного типа
         */
        <T extends NavRegionItem> T item(int index, Class<T> clazz);
    }
}