package net.n2oapp.framework.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Количественная информация ответа на запрос получения данных
 */
@Getter
public class Paging {
    @JsonProperty
    private Integer page;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private Integer count;

    public Paging() {
    }

    public Paging(Integer page, Integer size, Integer count) {
        this.page = page;
        this.size = size;
        this.count = count;
    }
}
