package net.n2oapp.framework.autotest;

public enum BadgePositionEnum {
    LEFT("left"),
    RIGHT("right");

    private final String position;

    BadgePositionEnum(String position) {
        this.position = position;
    }

    public String name(String prefix) {
        return prefix + position;
    }
}
