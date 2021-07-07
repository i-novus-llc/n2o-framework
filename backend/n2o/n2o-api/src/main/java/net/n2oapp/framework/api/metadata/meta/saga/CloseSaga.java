package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Информация после действия закрытия
 */
@Getter
@Setter
public class CloseSaga implements Compiled {
    @JsonProperty
    private RefreshSaga refresh;
}
