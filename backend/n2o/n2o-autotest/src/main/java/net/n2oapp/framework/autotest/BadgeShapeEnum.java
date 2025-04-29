package net.n2oapp.framework.autotest;

public enum BadgeShapeEnum {
    CIRCLE("circle"),
    ROUNDED("rounded"),
    SQUARE("square");

    private final String shape;

    BadgeShapeEnum(String shape) {
        this.shape = shape;
    }

    public String name(String prefix) {
        return prefix + shape;
    }
}
