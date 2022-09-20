package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.UrlAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Клиентская модель ячейки с изображением
 */
@Getter
@Setter
public class ImageCell extends ActionCell implements UrlAware {
    @JsonProperty
    private String width;
    @JsonProperty
    private String url;
    @JsonProperty
    private ShapeType shape;
    @JsonProperty
    private String title;
    @JsonProperty
    private String data;
    @JsonProperty
    private String description;
    @JsonProperty
    private N2oImageCell.Position textPosition;
    @JsonProperty
    private ImageStatusElement[] statuses;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
}
