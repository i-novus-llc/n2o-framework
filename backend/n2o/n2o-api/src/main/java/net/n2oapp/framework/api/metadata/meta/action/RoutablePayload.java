package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Действие восстановление состояния пагинации виджета по урл
 */
@Setter
@Getter
public class RoutablePayload extends PerformActionPayload {

    @JsonProperty
    private String id;

    private Map<String, String> paging;

    @JsonAnyGetter
    public Map<String, String> getPaging() {
        return this.paging;
    }

    @JsonAnySetter
    public void setPaging(Map<String, String> paging) {
        this.paging = paging;
    }

    public enum Paging {
        page, size
    }
}
