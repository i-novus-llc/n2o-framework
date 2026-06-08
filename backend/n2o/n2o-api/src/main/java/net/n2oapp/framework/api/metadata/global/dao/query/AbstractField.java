package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;

import java.util.Map;

/**
 * Абстрактное поле выборки
 */
@Getter
@Setter
public abstract class AbstractField implements Source, IdAware, ExtensionAttributesAware {
    private String id;
    private String absoluteId;
    private String mapping;
    private String normalize;
    private String selectExpression;
    private Boolean isSelected;
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    protected AbstractField(AbstractField field) {
        this.id = field.getId();
        this.mapping = field.getMapping();
        this.normalize = field.getNormalize();
        this.selectExpression = field.getSelectExpression();
        this.isSelected = field.getIsSelected();
        this.absoluteId = field.getAbsoluteId();
        this.extAttributes = field.getExtAttributes();
    }

    protected AbstractField() {
    }

    @Override
    public String toString() {
        return id;
    }
}
