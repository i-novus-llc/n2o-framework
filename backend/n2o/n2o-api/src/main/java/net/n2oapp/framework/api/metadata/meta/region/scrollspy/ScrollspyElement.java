package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактная реализация клиентской модели элемента scrollspy-региона
 */
@Getter
@Setter
public abstract class ScrollspyElement implements Compiled, IdAware {

    @JsonProperty
    private String id;
    @JsonProperty
    private String title;
}
