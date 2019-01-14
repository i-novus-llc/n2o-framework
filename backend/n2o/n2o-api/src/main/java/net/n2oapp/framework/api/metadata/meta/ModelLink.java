package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import net.n2oapp.framework.api.StringUtils;
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
    private String param;
    private String queryId;
    private SubModelQuery subModelQuery;

    public ModelLink(Object value) {
        setValue(value);
    }

    public ModelLink(Object value, SubModelQuery subModelQuery) {
        setValue(value);
        this.subModelQuery = subModelQuery;
    }

    public ModelLink(ReduxModel model, String widgetId) {
        super(String.format("models.%s['%s']", model.getId(), widgetId));
        this.model = model;
        this.widgetId = widgetId;
    }

    public ModelLink(ReduxModel model, String widgetId, SubModelQuery subModelQuery) {
        super(String.format("models.%s['%s']", model.getId(), widgetId));
        this.model = model;
        this.widgetId = widgetId;
        this.subModelQuery = subModelQuery;
    }

    public ModelLink(ReduxModel model, String widgetId, String fieldId) {
        //todo если поле genders*.id то нужно его превращать через js в массив и сетить в value
        super(fieldId == null ? String.format("models.%s['%s']", model.getId(), widgetId) : String.format("models.%s['%s'].%s",
                model.getId(), widgetId, fieldId));
        this.model = model;
        this.widgetId = widgetId;
        this.fieldId = fieldId;
    }

    public ModelLink(ReduxModel model, String widgetId, String fieldId, String param) {
        this(model, widgetId, fieldId);
        this.param = param;
    }

    public ModelLink(ReduxModel model, String widgetId, String fieldId, String param, String queryId) {
        this(model, widgetId, fieldId, param);
        this.queryId = queryId;
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

    public boolean isConstant() {
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
