package net.n2oapp.framework.api.metadata.application;

/**
 * Состояние боковой панели
 */
public enum SidebarState {
    /**
     * Скрыта полностью
     */
    none,
    /**
     * отображаются только иконки
     */
    micro,
    /**
     * отображаются иконки + название в узком контейнере
     */
    mini,
    /**
     * широкая боковая панель
     */
    maxi
}
