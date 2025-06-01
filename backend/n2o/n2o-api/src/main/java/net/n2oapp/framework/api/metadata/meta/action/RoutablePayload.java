package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

import java.util.Map;

/**
 * Действие восстановление состояния пагинации виджета по урл
 */
@Setter
@Getter
public class RoutablePayload extends PerformActionPayload {

    @JsonProperty
    private String id;

    private Map<String, String> params;

    @JsonAnyGetter
    public Map<String, String> getParams() {
        return this.params;
    }

    @JsonAnySetter
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @RequiredArgsConstructor
    @Getter
    public enum PagingEnum implements N2oEnum {
        PAGE("page"),
        SIZE("size");

        private final String id;
    }
}
