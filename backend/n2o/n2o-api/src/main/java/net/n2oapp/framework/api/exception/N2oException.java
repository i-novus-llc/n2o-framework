package net.n2oapp.framework.api.exception;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDialog;

/**
 * Базовое исключение N2O
 */
@Getter
@Setter
public class N2oException extends RuntimeException {
    /**
     * Обернуть любое исключение в {@link N2oException}
     *
     * @param e Исключение
     * @return Исключение N2O
     */
    public static N2oException wrap(Throwable e) {
        if (e instanceof N2oException)
            return (N2oException) e;
        return new N2oException(e);
    }

    /**
     * Текст сообщения для конечного пользователя
     */
    private String userMessage;
    /**
     * Статус http запроса
     */
    private int httpStatus = 500;
    /**
     * Уровень сообщения
     */
    private SeverityType severity = SeverityType.danger;
    /**
     * Поле на форме, к которому привязано сообщение
     */
    private String field;
    /**
     * Данные сообщения
     */
    private Object data;

    /**
     * Диалог подтверждения действия
     */
    private N2oDialog dialog;

    public N2oException() {
        super();
    }

    public N2oException(Throwable cause) {
        super(cause);
    }

    public N2oException(String message) {
        super(message);
    }

    public N2oException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Получить локализованное сообщение для пользователя
     */
    @Override
    public String getLocalizedMessage() {
        return StringUtils.resolveLinks(super.getLocalizedMessage(), data);
    }

    public N2oException addData(Object... data) {
        if (data == null) return this;
        if (data.length == 0) return this;
        if (data.length == 1) {
            this.data = data[0];
        } else {
            this.data = data;
        }
        return this;
    }
}
