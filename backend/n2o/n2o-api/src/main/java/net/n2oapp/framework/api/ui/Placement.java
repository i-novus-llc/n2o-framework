package net.n2oapp.framework.api.ui;

import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum Placement implements IdAware {
    top("top"),
    bottom("bottom"),
    topLeft("top-left"),
    topRight("top-right"),
    bottomLeft("bottom-left"),
    bottomRight("bottom-right");

    private String name;

    Placement(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }
}
