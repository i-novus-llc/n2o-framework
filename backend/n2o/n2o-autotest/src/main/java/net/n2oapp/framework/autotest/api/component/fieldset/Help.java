package net.n2oapp.framework.autotest.api.component.fieldset;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Element;

/**
 * Сообщение с подсказкой
 */
public interface Help extends Element {

    default void shouldHaveHelp() {
        element().$(".fa-question-circle").shouldHave(Condition.exist);
    }

    default void clickHelp() {
        element().$(".n2o-popover-btn").click();
    }
}
