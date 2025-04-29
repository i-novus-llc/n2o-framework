package net.n2oapp.framework.autotest;

/**
 * Цветовые коды
 */
public enum ColorsEnum {
    PRIMARY,
    SECONDARY,
    SUCCESS,
    DANGER,
    WARNING,
    INFO,
    LIGHT,
    MUTED,
    WHITE;

    public String name(String prefix) {
        return prefix + name().toLowerCase();
    }
}
