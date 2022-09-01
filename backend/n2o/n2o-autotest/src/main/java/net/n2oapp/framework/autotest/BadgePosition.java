package net.n2oapp.framework.autotest;

public enum BadgePosition {
    LEFT("left"),
    RIGHT("right");

    private final String position;

    BadgePosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
