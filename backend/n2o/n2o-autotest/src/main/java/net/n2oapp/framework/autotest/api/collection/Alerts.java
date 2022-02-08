package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.snippet.Alert;

/**
 * Сообщения alerts для автотестирования
 */
public interface Alerts extends ComponentsCollection {

    Alert alert(int index);

//    interface Alert extends Field, net.n2oapp.framework.autotest.api.component.snippet.Alert {
//        void shouldHaveText(String text);
//
//        void shouldHaveColor(Colors colors);
//
//        void shouldHavePlacement(Placement placement);
//
//        void shouldHaveStacktrace();
//
//        enum Placement {
//            TOP,
//            BOTTOM
//        }
//    }
}
