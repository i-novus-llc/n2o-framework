package net.n2oapp.framework.config.metadata.compile.toolbar;

import lombok.Getter;

@Getter
public class ToolbarPlaceScope {
    private String place;

    public ToolbarPlaceScope(String place) {
        this.place = place;
    }
}
