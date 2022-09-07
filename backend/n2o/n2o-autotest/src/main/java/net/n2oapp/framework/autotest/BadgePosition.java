package net.n2oapp.framework.autotest;

public enum BadgePosition {
    LEFT("left"),
    RIGHT("right");

    private final String position;

    BadgePosition(String position) {
        this.position = position;
    }

    public String name(String prefix) {
        return prefix + position;
    }
}
