package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

/**
 * Клиентская модель зависимости submit
 */
@Getter
@Setter
@NoArgsConstructor
public class CopyDependency extends Dependency {
    @JsonProperty
    private ReduxModelEnum model;
    @JsonProperty
    private String field;
    @JsonProperty
    private Boolean submit;
    @JsonProperty
    private Boolean applyOnInit;
}
