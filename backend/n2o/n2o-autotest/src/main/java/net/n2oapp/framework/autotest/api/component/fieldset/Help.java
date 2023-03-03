package net.n2oapp.framework.autotest.api.component.fieldset;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Element;

/**
 * Сообщение с подсказкой
 */
public interface Help extends Element {

    /**
     * Проверка существования иконки для вызова подсказки
     */
    default void shouldHaveHelp() {
        element().$(".fa-question-circle").shouldHave(Condition.exist);
    }

    /**
     * Клик для вызова подсказки
     */
    default void clickHelp() {
        element().$(".n2o-popover-btn").click();
    }
}
