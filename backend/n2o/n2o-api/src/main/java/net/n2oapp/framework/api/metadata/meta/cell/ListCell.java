package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель ячейки со списком
 */
@Getter
@Setter
public class ListCell extends AbstractCell {

    @JsonProperty
    private String color;
    @JsonProperty("amountToGroup")
    private Integer size;
    @JsonProperty
    private Boolean inline;
    @JsonProperty
    private String separator;
    @JsonProperty
    private Cell content;
}
