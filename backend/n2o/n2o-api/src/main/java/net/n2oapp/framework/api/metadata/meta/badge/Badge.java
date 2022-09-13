package net.n2oapp.framework.api.metadata.meta.badge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Клиентская модель значка
 */
@Getter
@Setter
public class Badge implements Compiled {
    @JsonProperty
    private String fieldId;
    @JsonProperty
    private String colorFieldId;
    @JsonProperty
    private String imageFieldId;
    @JsonProperty
    private String text;
    @JsonProperty
    private String color;
    @JsonProperty
    private String image;
    @JsonProperty
    private Position imagePosition;
    @JsonProperty
    private ShapeType imageShape;
    @JsonProperty
    private Position position;
    @JsonProperty
    private ShapeType shape;
}
