package net.n2oapp.framework.api.metadata.global.dao.object.field;

import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperType;

/**
 * Исходная модель простого поля объекта.
 */
public class ObjectScalarField extends AbstractParameter {

    private String domain;
    private String defaultValue;
    private String normalize;
    private MapperType mapperType;


    public ObjectScalarField() {
    }

    public ObjectScalarField(String id, String name, String mapping, Boolean required, String domain) {
        this.setId(id);
        this.setName(name);
        this.setMapping(mapping);
        this.setRequired(required);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getNormalize() {
        return normalize;
    }

    public void setNormalize(String normalize) {
        this.normalize = normalize;
    }

    public MapperType getMapperType() {
        return mapperType;
    }

    public void setMapperType(MapperType mapperType) {
        this.mapperType = mapperType;
    }
}
