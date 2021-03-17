package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактный параметр объекта
 */
@Getter
@Setter
public abstract class AbstractParameter implements IdAware, Source {
    private String id;
    private String name;
    private String mapping;
    private String enabled;
    private Boolean required;

    public AbstractParameter() {
    }

    public AbstractParameter(AbstractParameter parameter) {
        this.id = parameter.getId();
        this.name = parameter.getName();
        this.mapping = parameter.getMapping();
        this.enabled = parameter.getEnabled();
        this.required = parameter.getRequired();
    }
}
