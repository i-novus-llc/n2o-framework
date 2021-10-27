package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.Map;

/**
 * Клиентская модель компонента вывода изображения
 */
@Getter
@Setter
public class ImageField extends ActionField {
    @JsonProperty
    private String data;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private TextPosition textPosition;
    @JsonProperty
    private ImageShape shape;
    @JsonProperty
    private String width;
    @JsonProperty
    private ImageStatusElement[] statuses;
}
