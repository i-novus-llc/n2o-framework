package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.field.Field;

/**
 * Сообщения alerts для автотестирования
 */
public interface Alerts extends ComponentsCollection {

    Alert alert(int index);

    interface Alert extends Field {
        void shouldHaveText(String text);

        void shouldHaveColor(Colors colors);

        void shouldHavePosition(Position position);

        void shouldHavePlacement(Placement placement);

        enum Position {
            FIXED,
            RELATIVE;

            public String name(String prefix) {
                return prefix + name().toLowerCase();
            }
        }

        enum Placement {
            TOP,
            BOTTOM
        }
    }
}
