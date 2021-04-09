package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.UrlAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Ячейка с изображением
 */
@Getter
@Setter
public class N2oImageCell extends N2oActionCell implements UrlAware {
    @JsonProperty
    private Integer width;
    @JsonProperty
    private String url;
    @JsonProperty
    private ImageShape shape;
    @JsonProperty
    private String title;
    @JsonProperty
    private String data;
    @JsonProperty
    private String description;
    @JsonProperty
    private Position textPosition;
    @JsonProperty
    private ImageStatusElement[] statuses;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;

    public enum Position {
        top, left, right, bottom
    }
}