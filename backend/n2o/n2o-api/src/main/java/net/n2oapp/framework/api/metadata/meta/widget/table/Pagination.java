package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Place;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountType;

import java.util.Map;

/**
 * Клиентская модель для паджинации таблицы.
 */
@Getter
@Setter
public class Pagination implements Compiled {
    @JsonProperty
    private Boolean prev;
    @JsonProperty
    private Boolean next;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private String src;
    @JsonProperty
    private ShowCountType showCount;
    @JsonProperty
    private Boolean showLast;
    @JsonProperty
    private String prevLabel;
    @JsonProperty
    private String prevIcon;
    @JsonProperty
    private String nextLabel;
    @JsonProperty
    private String nextIcon;
    @JsonProperty
    private String className;
    @JsonProperty
    private Map<String, String> style;
    @JsonProperty
    private Place place;
}
