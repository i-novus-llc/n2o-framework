package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактная реализация клиентской модели события
 */
@Getter
@Setter
public abstract class Event implements Compiled, IdAware {

    @JsonProperty
    private String id;
}
