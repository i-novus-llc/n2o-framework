package net.n2oapp.framework.api.metadata.meta.badge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Клиентская модель значка
 */
@Getter
@Builder
public class Badge implements Compiled {
    @JsonProperty
    private final String fieldId;
    @JsonProperty
    private final String colorFieldId;
    @JsonProperty
    private final String imageFieldId;
    @JsonProperty
    private final Position imagePosition;
    @JsonProperty
    private final ShapeType imageShape;
    @JsonProperty
    private final Position position;
    @JsonProperty
    private final ShapeType shape;
}
