package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;

import java.util.Objects;

/**
 * Ссылка на модель виджета
 */
@Getter
public class ModelLink extends BindLink {
    private ReduxModel model;
    private String widgetId;
    private String fieldId;
    @Setter
    private String queryId;

    public ModelLink(Object value) {
        setValue(value);
    }

    public ModelLink(ReduxModel model, String widgetId) {
        super(String.format("models.%s['%s']", model.getId(), widgetId));
        this.model = model;
        this.widgetId = widgetId;
    }

    public ModelLink(ReduxModel model, String widgetId, String fieldId) {
        super(fieldId == null ? String.format("models.%s['%s']", model.getId(), widgetId) : String.format("models.%s['%s'].%s",
                model.getId(), widgetId, fieldId));
        this.model = model;
        this.widgetId = widgetId;
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId != null ? fieldId : getFieldValue();
    }

    /**
     * Проверяет является ли BindLink ссылкой на другой объект в redux или это константное значение
     * @return   true, если является ссылкой
     */
    public boolean isConst(){
        if (getBindLink() == null && !StringUtils.isJs(getValue()))
            return false;
        return true;
    }

    /**
     * Эквивалентны ли ссылки на модели без учёта значений и полей.
     * @param o Ссылка
     * @return true - эквивалентны, false - нет
     */
    public boolean equalsLink(Object o) {
        if (super.equalsLink(o))
            return true;
        if (!(o instanceof ModelLink))
            return false;
        ModelLink modelLink = (ModelLink) o;
        return Objects.equals(getWidgetId(), modelLink.getWidgetId())
                && Objects.equals(getModel(), modelLink.getModel());
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
