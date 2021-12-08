package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.util.Objects;

/**
 * Ссылка на модель виджета
 */
@Getter
public class ModelLink extends BindLink {
    /**
     * Тип модели
     */
    private ReduxModel model;
    /**
     * Клиентский идентификатор виджета
     */
    private String widgetId;
    /**
     * Идентификатор поля виджета
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
     * Может лои значение параметра (param) или ссылки (value) измениться на текущей странице?
     */
    @Setter
    @JsonProperty
    private boolean observe = false;

    public ModelLink() {
    }

    public ModelLink(Object value) {
        setValue(value);
    }

    public ModelLink(ModelLink link) {
        super(createBindLink(link.model, link.widgetId, link.fieldId));
        this.model = link.getModel();
        this.widgetId = link.getWidgetId();
        this.fieldId = link.fieldId;
        setValue(link.getValue());
        setSubModelQuery(link.getSubModelQuery());
        setParam(link.getParam());
        setObserve(link.isObserve());
    }

    public ModelLink(ReduxModel model, String widgetId) {
        super(createBindLink(model, widgetId, null));
        this.model = model;
        this.widgetId = widgetId;
    }

    public ModelLink(ReduxModel model, String widgetId, String fieldId) {
        super(createBindLink(model, widgetId, fieldId));
        this.model = model;
        this.widgetId = widgetId;
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        String result = null;
        String fieldValue = getFieldValue();
        if (fieldId != null) result = fieldId;
        if (fieldValue != null)
            if (result != null) result += "." + fieldValue;
            else result = fieldValue;
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

    public ModelLink getWidgetLink() {
        if (getModel() == null || getWidgetId() == null)
            return null;
        ModelLink widgetLink = new ModelLink(getModel(), getWidgetId());
        if (getFieldId() == null || getFieldId().equals("id")) {
            widgetLink.setSubModelQuery(getSubModelQuery());
        }
        return widgetLink;
    }

    public ModelLink getSubModelLink() {
        if (subModelQuery == null) return null;
        return new ModelLink(getModel(), getWidgetId(), subModelQuery.getSubModel());
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
        if (model == null || widgetId == null || that.model == null || that.widgetId == null)
            return false;

        String thisSubModelQueryLink;
        String thatSubModelQueryLink;

        String thisFieldId = this.getFieldId();
        if (this.getSubModelQuery() != null)
            thisFieldId = this.getSubModelQuery().getSubModel();
        thisSubModelQueryLink = createBindLink(this.getModel(), this.getWidgetId(), thisFieldId);

        String thatFieldId = that.getFieldId();
        if (that.getSubModelQuery() != null)
            thatFieldId = that.getSubModelQuery().getSubModel();
        thatSubModelQueryLink = createBindLink(that.getModel(), that.getWidgetId(), thatFieldId);

        if (thisSubModelQueryLink.length() > thatSubModelQueryLink.length()) {
            if (that.getValue() == null && this.getValue() == null
                    && (this.getSubModelQuery() != null || that.getSubModelQuery() != null))
                return thisSubModelQueryLink.startsWith(thatSubModelQueryLink + ".") && thatFieldId != null;
            return thisSubModelQueryLink.startsWith(thatSubModelQueryLink + ".");
        } else if (thisSubModelQueryLink.length() < thatSubModelQueryLink.length()) {
            if (that.getValue() == null && this.getValue() == null
                    && (this.getSubModelQuery() != null || that.getSubModelQuery() != null))
                return thatSubModelQueryLink.startsWith(thisSubModelQueryLink + ".") && thisFieldId != null;
            return thatSubModelQueryLink.startsWith(thisSubModelQueryLink + ".");
        } else
            return thisSubModelQueryLink.equals(thatSubModelQueryLink);
    }

    private static String createBindLink(ReduxModel model, String widgetId, String fieldId) {
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
                ", widgetId='" + widgetId + '\'' +
                ", fieldId='" + fieldId + '\'' +
                '}';
    }
}
