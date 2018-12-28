package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User: operehod
 * Date: 03.10.2014
 * Time: 10:20
 */
@Getter
@Setter
@NoArgsConstructor
public class InvocationParameter extends AbstractParameter {

    private String domain;
    private MapperType mapper;
    protected String defaultValue;
    protected String normalize;
    private String mappingCondition;
    private String entityClass;

    public InvocationParameter(InvocationParameter parameter) {
        this.domain = parameter.getDomain();
        this.setId(parameter.getId());
        this.setMapping(parameter.getMapping());
        this.defaultValue = parameter.getDefaultValue();
        this.setRequired(parameter.getRequired());
        this.normalize = parameter.getNormalize();
        this.mapper = parameter.getMapper();
    }
}
