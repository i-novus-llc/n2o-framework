package net.n2oapp.framework.api.exception;

/**
 * Маркер обозначающий что экспешен возращает краткую и детальную ошибку на клиент
 * @deprecated
 * @see N2oUserException
 */
@Deprecated
public interface UserMessageAware {

    /**
     * @return краткая ошибка
     */
    String getSummary();

    /**
     * @return детальная ошибка
     */
    String getDetailedMessage();
}
