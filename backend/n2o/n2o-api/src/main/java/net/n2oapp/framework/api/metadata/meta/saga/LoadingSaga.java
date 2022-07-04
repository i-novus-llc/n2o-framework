package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Информация о лоадере после действия
 */
@Getter
@Setter
public class LoadingSaga implements Compiled {
    @JsonProperty
    private String pageId;
    @JsonProperty
    private String position;
    @JsonProperty
    private Boolean active;
}
