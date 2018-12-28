package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * User: operhod
 * Date: 15.01.14
 * Time: 10:13
 */
@Getter
@Setter
public abstract class AbstractParameter implements IdAware, Source {
    private String id;
    private String name;
    private String mapping;
    private Boolean required;
    private PluralityType pluralityType;
    private Boolean nullIgnore;
}
