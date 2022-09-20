package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель ячейки с рейтингом
 */
@Getter
@Setter
public class RatingCell extends ActionCell {
    @JsonProperty
    private Boolean showTooltip;
    @JsonProperty
    private Boolean half;
    @JsonProperty
    private Integer max;
    @JsonProperty
    private Boolean readonly;
}
