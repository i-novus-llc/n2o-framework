package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

/**
 * Ссылка на модель виджета
 */
@Getter
public class ModelLink extends BindLink {
    private ReduxModel model;
    private String widgetId;
    private String fieldId;
    @Setter
    private SubModelQuery subModelQuery;
    @Setter
    private String param;
    /**
     * Обновление только после появления данных у целевого виджета
     */
    @Setter
    @JsonProperty
    private Boolean observe;

    public ModelLink() {
    }

    public ModelLink(Object value) {
        setValue(value);
    }

    public ModelLink(ModelLink link) {
        super(createBindLink(link.model, link.widgetId, link.fieldId));
        this.model = link.model;
        this.widgetId = link.widgetId;
        this.fieldId = link.fieldId;
        setValue(link.getValue());
        setSubModelQuery(link.subModelQuery);
        setParam(link.param);
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
        if (fieldId != null) return fieldId;
        if (getFieldValue() != null && getFieldValue().contains(".map(function(t){return t."))
            return getFieldValue().substring(0, getFieldValue().indexOf("."));

        return fieldId != null ? fieldId : getFieldValue();
    }

    /**
     * Получить ссылку на модель виджета*
     */
    public ModelLink getWidgetLink() {
        if (getModel() == null || getWidgetId() == null)
            return null;
        ModelLink widgetLink = new ModelLink(getModel(), getWidgetId());
        if (getFieldId() == null || getFieldId().equals("id")) {
            widgetLink.setSubModelQuery(getSubModelQuery());
        }
        return widgetLink;
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
            return thisSubModelQueryLink.startsWith(thatSubModelQueryLink + ".");
        } else if (thisSubModelQueryLink.length() < thatSubModelQueryLink.length()) {
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
