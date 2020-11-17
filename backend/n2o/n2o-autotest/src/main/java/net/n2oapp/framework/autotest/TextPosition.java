package net.n2oapp.framework.autotest;

/**
 * Положение текста
 */
public enum TextPosition {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM;

    public String name(String prefix) {
        return prefix + name().toLowerCase();
    }
}
