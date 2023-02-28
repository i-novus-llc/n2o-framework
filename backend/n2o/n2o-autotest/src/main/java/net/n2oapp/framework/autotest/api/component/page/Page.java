package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.api.metadata.application.NavigationLayout;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.application.Footer;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.header.SimpleHeader;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;

/**
 * Страница для автотестирования
 */
public interface Page extends Component {

    /**
     * @return Компонент header для автотестирования
     */
    SimpleHeader header();

    /**
     * @return Компонент боковой панели для автотестирования
     */
    Sidebar sidebar();

    /**
     * @return Компонент footer для автотестирования
     */
    Footer footer();

    /**
     * @return Компонент панель кнопок страницы для автотестирования
     */
    PageToolbar toolbar();

    /**
     * @return Компонент шлебные крошки для автотестирования
     */
    Breadcrumb breadcrumb();

    /**
     * Возвращает компонент диалог, заголовок которого совпадает с ожидаемым
     * @param title заголовок диалога
     * @return Компонент диалог для автотестирования
     */
    Dialog dialog(String title);

    /**
     * Возвращает компонент поповер, заголовок которого совпадает с ожидаемым
     * @param title заголовок диалога
     * @return Компонент поповер для автотестирования
     */
    Popover popover(String title);

    /**
     * Метод не поддерживаемый. Необходимо использовать alerts(Alert.Placement placement)
     */
    @Deprecated
    Alerts alerts();

    /**
     * Возвращает компонент оповещение, положение которого совпадает с ожидаемым
     * @param placement положение оповещения на странице
     * @return Компонент оповещение для автотестирования
     */
    Alerts alerts(Alert.Placement placement);

    /**
     * Проверка совпадения части ссылки страницы
     * @param regex часть ссылки в виде регулярного выражения
     */
    void shouldHaveUrlMatches(String regex);

    /**
     * Проверка заголовка на соответствие
     * @param title ожидаемый заголовок страницы
     */
    void shouldHaveTitle(String title);

    /**
     * Прокрутка страницы в самый верх
     */
    void scrollUp();

    /**
     * Прокрутка страницы в самый низ
     */
    void scrollDown();

    /**
     * Проверка стиля страницы на соответствие
     * @param style ожидаемый стиль страницы
     */
    void shouldHaveStyle(String style);

    /**
     * Проверка макета на соответствие
     * @param layout ожидаемый макет
     */
    void shouldHaveLayout(NavigationLayout layout);

    /**
     * Проверка http-ошибки на странице
     * @param statusCode ожидаемая ошибка
     */
    void shouldHaveError(int statusCode);

    /**
     * Компонент панель кнопок страницы для автотестирования
     */
    interface PageToolbar {
        /**
         * @return Панель кнопок в левой верхней части страницы
         */
        Toolbar topLeft();

        /**
         * @return Панель кнопок в правой верхней части страницы
         */
        Toolbar topRight();

        /**
         * @return Панель кнопок в левой нижней части страницы
         */
        Toolbar bottomLeft();

        /**
         * @return Панель кнопок в правой нижней части страницы
         */
        Toolbar bottomRight();
    }

    /**
     * Компонент шлебные крошки для автотестирования
     */
    interface Breadcrumb extends Component {

        /**
         * Клик хлебной крошке с ссылкой с соответствующим текстом
         * Метод не поддерживаемый, следует класс Crumb и его метод click();
         * @param text текст хлебной крошки
         */
        @Deprecated
        void clickLink(String text);

        /**
         * Проверка заголовка первой хлебной крошки на соответствие
         * Метод не поддерживаемый, следует класс Crumb и его метод shouldHaveLabel(String text);
         * @param text ожидаемый текст
         */
        @Deprecated
        void firstTitleShouldHaveText(String text);

        /**
         * Проверка заголовка последней хлебной крошки на соответствие
         * Метод не поддерживаемый, следует класс Crumb и его метод shouldHaveLabel(String text);
         * @param title ожидаемый текст заголовка
         */
        @Deprecated
        void lastTitleShouldHaveText(String title);

        /**
         * Проверка заголовка хлебной крошки соответствующей номеру
         * Метод не поддерживаемый, следует класс Crumb и его метод shouldHaveLabel(String text);
         * @param title ожидаемый заголовок
         * @param index номер проверяемой крошки
         */
        @Deprecated
        void titleShouldHaveText(String title, Integer index);

        /**
         * Проверка количества хлебных крошек
         * @param size ожидаемое количество
         */
        void shouldHaveSize(int size);

        /**
         * Возвращает хлебную крошку соответствующую номеру расположения
         * @param index номер хлебной крошки
         * @return Компонент хлебная крошка для автотестирования
         */
        Crumb crumb(int index);

        /**
         * Возвращает хлебную крошку соответствующую заголовку
         * @param label заголовок хлебной крошки
         * @return Компонент хлебная крошка для автотестирования
         */
        Crumb crumb(String label);

        interface Crumb {

            /**
             * Клик по хлебной крошке для перехода по ссылке
             */
            void click();

            /**
             * Проверка заголовка на соответствие
             * @param text ожидаемый текст заголовка
             */
            void shouldHaveLabel(String text);

            /**
             * Проверка ссылки на соответствие
             * @param link ожидаемая ссылка
             */
            void shouldHaveLink(String link);

            /**
             * Проверка отсутствия ссылки у крошки
             */
            void shouldNotHaveLink();
        }
    }

    /**
     * Компонент диалог для автотестирования
     */
    interface Dialog {

        /**
         * Проверка видимости
         */
        void shouldBeVisible();

        /**
         * Проверка заголовка диалога
         * @param text ожидаемый заголовок
         */
        void shouldHaveText(String text);

        /**
         * Возвращает стандартную кнопку диалога, соответствующую ожидаемому заголовку
         * @param label ожидаемый заголовок
         * @return Кнопка для автотестирования
         */
        StandardButton button(String label);

        /**
         * Проверка закрытия диалога в течение передаваемого времени
         * @param timeOut длительность проверки
         */
        void shouldBeClosed(long timeOut);

        /**
         * Проверка того, что кнопки имеют обратное положение
         */
        void shouldHaveReversedButtons();
    }

    /**
     * Компонент поповер для автотестирования
     */
    interface Popover {

        /**
         * Проверка видимости
         */
        void shouldBeVisible();

        /**
         * Проверка заголовка на соответствие
         * @param text ожидаемый заголовок
         */
        void shouldHaveText(String text);

        /**
         * Клик по кнопке соответствующей метке
         * @param label метка кнопки
         */
        void click(String label);

        /**
         * Проверка закрытия поповера в течение передаваемого времени
         * @param timeOut длительность проверки
         */
        void shouldBeClosed(long timeOut);
    }

}
