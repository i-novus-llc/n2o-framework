package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

/**
 * Ссылка на модель виджета
 */
@Getter
public class ModelLink extends BindLink {
    /**
     * Тип модели
     */
    private ReduxModelEnum model;
    /**
     * Клиентский идентификатор источника данных
     */
    private String datasource;
    /**
     * Идентификатор поля
     */
    private String fieldId;
    /**
     * Информация о том, как получить вложенную модель этого поля
     */
    @Setter
    private SubModelQuery subModelQuery;
    /**
     * Параметр запроса, содержащий значение
     */
    @Setter
    private String param;
    /**
     * Может ли значение параметра (param) или ссылки (value) измениться на текущей странице?
     */
    @Setter
    @JsonProperty
    private boolean observe = false;
    /**
     * Является ли значение параметра обязательным
     */
    @Setter
    @JsonProperty
    private boolean required = false;

    public ModelLink() {
    }

    public ModelLink(Object value) {
        setValue(value);
    }

    public ModelLink(ModelLink link) {
        super(createBindLink(link.model, link.datasource, link.fieldId));
        this.model = link.model;
        this.datasource = link.datasource;
        this.fieldId = link.fieldId;
        setValue(link.getValue());
        setSubModelQuery(link.getSubModelQuery());
        setParam(link.getParam());
        setObserve(link.isObserve());
    }

    public ModelLink(ReduxModelEnum model, String datasource) {
        super(createBindLink(model, datasource, null));
        this.model = model;
        this.datasource = datasource;
    }

    public ModelLink(ReduxModelEnum model, String datasource, String fieldId) {
        super(createBindLink(model, datasource, fieldId));
        this.model = model;
        this.datasource = datasource;
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        String result = null;
        String fieldValue = getFieldValue();
        if (fieldId != null) result = fieldId;
        if (fieldValue != null) {
            if (result != null) {
                result += "." + fieldValue;
            } else {
                result = fieldValue;
            }
        }
        return result;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
        if (isConst())
            setObserve(false);
    }

    /**
     * Получить поле модели, установленное ссылкой в значении
     *
     * @return Поле модели или null
     */
    public String getFieldValue() {
        if (StringUtils.isJs(getValue())) {
            String js = getValue().toString().substring(1, getValue().toString().length() - 1);
            if (js.contains(".map(function(t){return t."))
                return js.substring(0, js.indexOf("."));
            else
                return js;
        }
        return null;
    }

    @JsonIgnore
    public ModelLink getWidgetLink() {
        if (getModel() == null || getDatasource() == null)
            return null;
        ModelLink widgetLink = new ModelLink(getModel(), getDatasource());
        if (getFieldId() == null || getFieldId().equals("id")) {
            widgetLink.setSubModelQuery(getSubModelQuery());
        }
        return widgetLink;
    }

    @JsonIgnore
    public ModelLink getSubModelLink() {
        if (subModelQuery == null) return null;
        return new ModelLink(getModel(), getDatasource(), subModelQuery.getFullName());
    }


    /**
     * Эквивалентны ли ссылки на модели.
     *
     * @param o Ссылка
     * @return true - эквивалентны, false - нет
     */
    @Override
    public boolean equalsLink(Object o) {
        if (o == null || o.getClass() != this.getClass())
            return false;
        ModelLink that = (ModelLink) o;
        if (getModel() == null || getDatasource() == null || that.getModel() == null || that.getDatasource() == null)
            return false;

        String thisSubModelQueryLink;
        String thatSubModelQueryLink;

        String thisFieldId = this.getFieldId();
        if (this.getSubModelQuery() != null)
            thisFieldId = this.getSubModelQuery().getFullName();
        thisSubModelQueryLink = createBindLink(this.getModel(), this.getDatasource(), thisFieldId);

        String thatFieldId = that.getFieldId();
        if (that.getSubModelQuery() != null)
            thatFieldId = that.getSubModelQuery().getFullName();
        thatSubModelQueryLink = createBindLink(that.getModel(), that.getDatasource(), thatFieldId);

        if (thisSubModelQueryLink.length() > thatSubModelQueryLink.length())
            return compareLinks(this, that, thisSubModelQueryLink, thatSubModelQueryLink);
        else if (thisSubModelQueryLink.length() < thatSubModelQueryLink.length())
            return compareLinks(that, this, thatSubModelQueryLink, thisSubModelQueryLink);
        else
            return thisSubModelQueryLink.equals(thatSubModelQueryLink);
    }

    private boolean compareLinks(ModelLink first, ModelLink second,
                                 String firstSubModelQueryLink, String secondSubModelQueryLink) {
        if ((first.getValue() == null && second.getValue() == null) ||
                (first.getValue() != null && second.getValue() != null))
            return firstSubModelQueryLink.startsWith(secondSubModelQueryLink + ".");
        return first.getBindLink().equals(second.getBindLink());
    }

    private static String createBindLink(ReduxModelEnum model, String widgetId, String fieldId) {
        if (model == null)
            return null;
        return fieldId == null
                ? String.format("models.%s['%s']", model.getId(), widgetId)
                : String.format("models.%s['%s'].%s", model.getId(), widgetId, fieldId);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "ModelLink{" +
                super.toString() +
                ", model=" + model +
                ", datasource='" + datasource + '\'' +
                ", fieldId='" + fieldId + '\'' +
                '}';
    }
}
