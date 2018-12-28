package net.n2oapp.framework.api.exception;

/**
 * Маркер что ошибка содержит в себе чужой стек-трейс
 */
public interface ForeignStackTraceAware {

    /**
     * @return чужой стек-трейс
     */
    String getForeignStackTrace();
}
