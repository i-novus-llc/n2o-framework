package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StandardPage extends Page {

    @JsonProperty
    private Map<String, Widget> widgets;

    @JsonProperty
    private Map<String, List<Region>> regions;

    @JsonProperty
    private Toolbar toolbar;

    @JsonProperty
    private Map<String, Action> actions;

    @JsonProperty
    private RegionWidth width;
}
