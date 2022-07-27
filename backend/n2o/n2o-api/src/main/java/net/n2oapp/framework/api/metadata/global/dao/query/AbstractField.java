package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактное поле выборки
 */
@Getter
@Setter
public abstract class AbstractField implements Source, Compiled, IdAware {
    private String id;
    private String mapping;
    private String normalize;

    public AbstractField(AbstractField field) {
        this.id = field.getId();
        this.mapping = field.getMapping();
        this.normalize = field.getNormalize();
    }

    public AbstractField() {
    }

    public static AbstractField of(AbstractField field) {
        if (field instanceof ListField)
            return new ListField(((ListField) field));
        if (field instanceof ReferenceField)
            return new ReferenceField(((ReferenceField) field));
        return new SimpleField(((SimpleField) field));
    }

    @Override
    public String toString() {
        return id;
    }
}
