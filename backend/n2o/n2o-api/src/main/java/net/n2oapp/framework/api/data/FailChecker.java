package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.exception.N2oException;

/**
 * Проверка успешности выполнения по результату или исключению провайдера данных
 */
public interface FailChecker {
    /**
     * Преобразовать ответ провайдера данных в исключение N2O
     * @param e Исключение, брошенное при выполнении, или null
     * @throws N2oException Если операция завершилась неудачно
     */
    void checkFail(Exception e) throws N2oException;

    /**
     * Преобразовать ответ провайдера данных в исключение N2O
     * @param result Результат выполнения, или null
     * @throws N2oException Если операция завершилась неудачно
     */
    void checkFail(Object result) throws N2oException;
}
