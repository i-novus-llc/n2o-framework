package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Постпроцессинг асинхронного действия
 */
@Getter
@Setter
public class AsyncMetaSaga extends MetaSaga {
    @JsonProperty
    private MetaSaga success;
    @JsonProperty
    private MetaSaga fail;
}
