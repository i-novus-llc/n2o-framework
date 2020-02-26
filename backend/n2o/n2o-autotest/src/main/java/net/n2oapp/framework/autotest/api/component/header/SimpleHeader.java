package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.collection.Menu;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент header для автотестирования
 */
public interface SimpleHeader extends Component {


    void shouldBrandName(String brandName);

    Menu menu();

    Menu extra();

}
