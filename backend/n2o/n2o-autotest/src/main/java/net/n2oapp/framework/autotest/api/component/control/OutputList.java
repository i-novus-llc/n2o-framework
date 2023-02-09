package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.api.metadata.meta.control.OutputList.Direction;

/**
 * Компонент вывода многострочного текста для автотестирования
 */
public interface OutputList extends Control {

    void shouldHaveValues(String separator, String... values);

    void shouldHaveLinkValues(String separator, String... values);

    void shouldHaveDirection(Direction direction);

    void shouldHaveLink(String itemValue, String link);
}
