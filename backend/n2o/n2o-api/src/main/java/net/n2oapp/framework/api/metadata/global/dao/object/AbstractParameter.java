package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактный параметр объекта
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractParameter implements IdAware, Source {
    private String id;
    private String mapping;
    private String enabled;
    private Boolean required;
    private String namespaceUri;

    public AbstractParameter(AbstractParameter parameter) {
        this.id = parameter.getId();
        this.mapping = parameter.getMapping();
        this.enabled = parameter.getEnabled();
        this.required = parameter.getRequired();
    }
}
