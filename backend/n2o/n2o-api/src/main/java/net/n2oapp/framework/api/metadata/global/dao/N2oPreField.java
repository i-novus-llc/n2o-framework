package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;

import java.io.Serializable;

/**
 * Модель предустановленных полей
 */
@Getter
@Setter
public class N2oPreField implements Serializable {
    private String fieldId;
    private String value;
    private FilterType type;
    private String[] values;
}
