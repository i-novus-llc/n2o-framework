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
    private String absoluteId;
    private String mapping;
    private String normalize;
    private String selectExpression;
    private String sortingExpression;
    private String sortingMapping;

    private Boolean isSelected;
    private Boolean isSorted;

    public AbstractField(AbstractField field) {
        this.id = field.getId();
        this.mapping = field.getMapping();
        this.normalize = field.getNormalize();
        this.selectExpression = field.getSelectExpression();
        this.isSelected = field.getIsSelected();
        this.sortingExpression = field.getSortingExpression();
        this.sortingMapping = field.getSortingMapping();
        this.isSorted = field.getIsSorted();
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
