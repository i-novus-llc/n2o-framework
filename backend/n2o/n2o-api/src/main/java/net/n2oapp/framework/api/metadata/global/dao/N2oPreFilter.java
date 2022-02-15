package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;

import java.util.Objects;

import static net.n2oapp.criteria.filters.FilterType.Arity.n_ary;
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
    private FilterType type;
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
    private String datasource;
    /**
     * Модель виджета, на который ссылается фильтр
     */
    private ReduxModel model;

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

    public N2oPreFilter(String fieldId, FilterType type) {
        this.fieldId = fieldId;
        this.type = type;
    }

    public N2oPreFilter(String fieldId, String value, FilterType type) {
        this.fieldId = fieldId;
        this.type = type;
        this.valueAttr = value;
    }

    public N2oPreFilter(String fieldId, String[] values, FilterType type) {
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
        return datasource;
    }

    @Deprecated
    public void setRefWidgetId(String refWidgetId) {
        this.datasource = refWidgetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof N2oPreFilter)) return false;
        N2oPreFilter that = (N2oPreFilter) o;
        return Objects.equals(fieldId, that.fieldId) &&
                Objects.equals(datasource, that.datasource) &&
                model == that.model &&
                type == that.type;
    }

    public boolean isBoolean() {
        return FilterType.isNull.equals(getType()) || FilterType.isNotNull.equals(getType());
    }

    public boolean isArray() {
        return (getValues() != null && getValues().length != 0)
                || (type.arity.equals(n_ary) && valueAttr != null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId, datasource, model, type);
    }

    public String[] getValues() {
        if (valueList != null && valueList.length > 0)
            return valueList;
        else if (type != null && type.arity.equals(n_ary) && valueAttr != null)
            return new String[]{valueAttr};
        else
            return null;
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
