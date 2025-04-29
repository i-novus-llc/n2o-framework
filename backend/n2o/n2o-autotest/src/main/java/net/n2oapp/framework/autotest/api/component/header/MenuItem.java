package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Кнопка меню для автотестирования
 */
public interface MenuItem extends Component {

    /**
     * Проверка наличия изображения
     */
    void shouldHaveImage();

    /**
     * Проверка формы изображения на соответствие
     * @param shape ожидаемая форма изображения
     */
    void imageShouldHaveShape(ShapeTypeEnum shape);

    /**
     * Проверка источника изображения на соответствие
     * @param src ожидаемый источник изображения
     */
    void imageShouldHaveSrc(String src);

    /**
     * Проверка метки на соответствие
     * @param text ожидаемое значение метки
     */
    void shouldHaveLabel(String text, Duration... duration);

    /**
     * Клик по элементу
     */
    void click();
}
