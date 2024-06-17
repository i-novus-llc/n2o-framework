package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактное поле выборки
 */
@Getter
@Setter
public abstract class AbstractField implements Source, IdAware {
    private String id;
    private String absoluteId;
    private String mapping;
    private String normalize;
    private String selectExpression;
    private Boolean isSelected;

    protected AbstractField(AbstractField field) {
        this.id = field.getId();
        this.mapping = field.getMapping();
        this.normalize = field.getNormalize();
        this.selectExpression = field.getSelectExpression();
        this.isSelected = field.getIsSelected();
        this.absoluteId = field.getAbsoluteId();
    }

    protected AbstractField() {
    }

    @Override
    public String toString() {
        return id;
    }
}
