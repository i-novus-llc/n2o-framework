package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Layout;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Place;

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
    private Boolean last;
    @JsonProperty
    private Boolean first;
    @JsonProperty
    private Boolean showCount;
    @JsonProperty
    private Boolean showSinglePage;
    @JsonProperty
    private Layout layout;
    @JsonProperty
    private String prevLabel;
    @JsonProperty
    private String prevIcon;
    @JsonProperty
    private String nextLabel;
    @JsonProperty
    private String nextIcon;
    @JsonProperty
    private String firstLabel;
    @JsonProperty
    private String firstIcon;
    @JsonProperty
    private String lastLabel;
    @JsonProperty
    private String lastIcon;
    @JsonProperty
    private Integer maxPages;
    @JsonProperty
    private String className;
    @JsonProperty
    private String style;
    @JsonProperty
    private Place place;
}
