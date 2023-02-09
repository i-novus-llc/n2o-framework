package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Кнопка меню для автотестирования
 */
public interface MenuItem extends Component {

    void shouldHaveImage();

    void shouldHaveImageInShape(ShapeType shape);

    void shouldHaveImageBySrc(String src);

    void shouldHaveLabel(String text);

    void click();
}
