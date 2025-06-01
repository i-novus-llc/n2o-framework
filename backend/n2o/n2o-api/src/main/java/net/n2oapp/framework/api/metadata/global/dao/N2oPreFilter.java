package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;

import java.util.Objects;

import static net.n2oapp.framework.api.StringUtils.unwrapLink;


/**
 * Исходная модель предустановленного фильтра
 */
@Getter
@Setter
public class N2oPreFilter implements Source {
    /**
     * Поле фильтрации
     */
    private String fieldId;
    /**
     * Значение фильтра value
     */
    private String valueAttr;
    /**
     * Значение фильтра values
     */
    private String valuesAttr;
    /**
     * Тип фильтрации
     */
    private FilterTypeEnum type;
    /**
     * Наименование параметра в адресе маршрута
     */
    private String param;
    /**
     * Попадает ли фильтр в url
     */
    private Boolean routable;
    /**
     * Идентификатор страницы, на которую ссылается фильтр
     */
    private String refPageId;
    /**
     * Идентификатор источника данных на странице, на который ссылается фильтр
     */
    private String datasourceId;
    /**
     * Модель виджета, на который ссылается фильтр
     */
    private ReduxModelEnum model;

    /**
     * Список значений фильтра
     */
    private String[] valueList;

    /**
     * Обязательность фильтра
     */
    private Boolean required;

    /**
     * Сбрасывать значение при изменении в модели
     */
    private Boolean resetOnChange;

    public N2oPreFilter() {
    }

    public N2oPreFilter(String fieldId, FilterTypeEnum type) {
        this.fieldId = fieldId;
        this.type = type;
    }

    public N2oPreFilter(String fieldId, String value, FilterTypeEnum type) {
        this.fieldId = fieldId;
        this.type = type;
        this.valueAttr = value;
    }

    public N2oPreFilter(String fieldId, String[] values, FilterTypeEnum type) {
        this.fieldId = fieldId;
        this.type = type;
        this.valueList = values;
    }

    public String getRef() {
        return unwrapLink(getValue());
    }

    public void setRef(String ref) {
        this.valueAttr = Placeholders.ref(ref);
    }

    public boolean isRef() {
        return getRef() != null;
    }

    @Deprecated
    public String getRefWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setRefWidgetId(String refWidgetId) {
        this.datasourceId = refWidgetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof N2oPreFilter that)) return false;
        return Objects.equals(fieldId, that.fieldId) &&
                Objects.equals(datasourceId, that.datasourceId) &&
                model == that.model &&
                type == that.type;
    }

    public boolean isBoolean() {
        return FilterTypeEnum.IS_NULL.equals(getType()) || FilterTypeEnum.IS_NOT_NULL.equals(getType());
    }

    public boolean isArray() {
        return getValues() != null && getValues().length != 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, datasourceId, model, type);
    }

    public String[] getValues() {
        return (valueList != null && valueList.length > 0) ? valueList : null;
    }

    public String getValue() {
        return valueAttr == null ? valuesAttr : valueAttr;
    }

    @Override
    public String toString() {
        return "N2oPreFilter{" +
                "fieldId='" + fieldId + '\'' +
                ", type=" + type +
                '}';
    }
}
