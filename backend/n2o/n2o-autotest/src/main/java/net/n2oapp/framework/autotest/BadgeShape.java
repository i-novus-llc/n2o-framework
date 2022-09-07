package net.n2oapp.framework.autotest;

public enum BadgeShape {
    CIRCLE("circle"),
    ROUNDED("rounded"),
    SQUARE("square");

    private final String shape;

    BadgeShape(String shape) {
        this.shape = shape;
    }
}
