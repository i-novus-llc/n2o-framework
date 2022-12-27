package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Действие установки активного элемента региона
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetActiveRegionEntityPayload extends PerformActionPayload {
    @JsonProperty
    private String regionId;
    @JsonProperty
    private String activeEntity;
}
