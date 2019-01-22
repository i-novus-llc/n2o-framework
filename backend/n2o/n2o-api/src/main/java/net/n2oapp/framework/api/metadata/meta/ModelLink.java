package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.script.ScriptProcessor;

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
    private SubModelQuery subModelQuery;

    public ModelLink(Object value) {
        setValue(value);
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
        return fieldId != null ? fieldId : getFieldValue();
    }

    /**
     * Проверяет является ли BindLink ссылкой на другой объект в redux или это константное значение
     *
     * @return true, если является ссылкой
     */
    public boolean isLink() {
        if (getBindLink() == null && !StringUtils.isJs(getValue()))
            return false;
        return true;
    }

    /**
     * Эквивалентны ли ссылки на модели без учёта значений и полей.
     *
     * @param o Ссылка
     * @return true - эквивалентны, false - нет
     */
    public boolean equalsLink(Object o) {
        if (o == null || o.getClass() != this.getClass())
            return false;

        ModelLink that = (ModelLink) o;

        ModelLink withSubModelQuery;
        ModelLink withoutSubModelQuery;

        if (this.getSubModelQuery() != null && that.getSubModelQuery() != null) {
            return false;
        } else if (this.getSubModelQuery() != null) {
            withSubModelQuery = this;
            withoutSubModelQuery = that;
        } else if (that.getSubModelQuery() != null) {
            withSubModelQuery = that;
            withoutSubModelQuery = this;
        } else {
            return super.equalsLink(o) || Objects.equals(widgetId, that.getWidgetId())
                    && Objects.equals(model, that.getModel());
        }

        String withSubModelQueryLink;
        String withoutSubModelQueryLink = createBindLink(withSubModelQuery.getModel(), withSubModelQuery.getWidgetId(), withSubModelQuery.getSubModelQuery().getSubModel());
        if (withoutSubModelQuery.getValue() == null) {
            withSubModelQueryLink = createBindLink(withoutSubModelQuery.getModel(), withoutSubModelQuery.getWidgetId(), withoutSubModelQuery.getFieldId());
        } else {
            withSubModelQueryLink = withoutSubModelQuery.getBindLink() + "." + ScriptProcessor.removeJsBraces(withoutSubModelQuery.getValue());
        }
        if (withoutSubModelQueryLink.equals(withSubModelQueryLink))
            return true;
        return withSubModelQueryLink.startsWith(withoutSubModelQueryLink + ".");
    }

    private static String createBindLink(ReduxModel model, String widgetId, String fieldId) {
        return fieldId == null
                ? String.format("models.%s['%s']", model.getId(), widgetId)
                : String.format("models.%s['%s'].%s", model.getId(), widgetId, fieldId);
    }

    public boolean isConst() {
        return !StringUtils.isJs(getValue());
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
