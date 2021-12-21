package net.n2oapp.framework.api.metadata.meta.saga;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.io.Serializable;
import java.util.List;

/**
 * Информация об обновлении компонентов после действия
 */
@Getter
@Setter
public class RefreshSaga implements Compiled {
    @JsonProperty
    private List<String> datasources;
}
