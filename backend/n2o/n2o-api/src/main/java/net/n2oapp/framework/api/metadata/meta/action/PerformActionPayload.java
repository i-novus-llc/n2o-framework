package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.Map;

/**
 * Настраиваемая полезная нагрузка действия
 */
@Getter
@Setter
@NoArgsConstructor
public class PerformActionPayload implements ActionPayload {

    StrictMap<String, Object> params;

    public PerformActionPayload(Map<String, Object> params) {
        this.params = new StrictMap<>();
        this.params.putAll(params);
    }

    @JsonAnyGetter
    public Map<String, Object> getJsonParams() {
        return getParams();
    }
}
