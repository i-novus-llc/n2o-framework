package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Кнопка меню для автотестирования
 */
public interface MenuItem extends Component {

    void shouldHaveImage();

    void imageShouldHaveShape(ImageShape shape);

    void imageSrcShouldBe(String src);

    void labelShouldHave(String text);

    void click();
}
