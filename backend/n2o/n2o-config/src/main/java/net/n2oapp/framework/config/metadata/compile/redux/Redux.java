package net.n2oapp.framework.config.metadata.compile.redux;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.util.CompileUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.global.dao.N2oQuery.Field.PK;

/**
 * Взаимодействие c Redux моделями
 */
public abstract class Redux {

    /**
     * Создать ссылку на поле в Redux модели виджета
     *
     * @param widgetId Идентификатор виджета
     * @param model    Модель
     * @param field    Поле
     * @return Redux действие
     */
    @Deprecated
    public static BindLink createBindLink(String widgetId, ReduxModel model, String field) {
        //todo если поле genders*.id то нужно его превращать через js в массив и сетить в value
        return new ModelLink(model, widgetId, field);
    }

    /**
     * Создать ссылку на Redux модель виджета
     *
     * @param widgetId Идентификатор виджета
     * @param model    Модель
     * @return Redux действие
     */
    @Deprecated
    public static BindLink createBindLink(String widgetId, ReduxModel model) {
        return new ModelLink(model, widgetId);
    }

    /**
     * Создать ссылку на сортировку в состоянии виджета
     *
     * @param widgetId Идентификатор виджета
     * @param fieldId  Поле сортировки
     * @return Ссылка на состояние виджета
     */
    public static BindLink createSortLink(String widgetId, String fieldId) {
        return new BindLink(String.format("widgets['%s'].sorting.%s", widgetId, fieldId));
    }

    /**
     * Создать ссылку на основе Redux действия
     *
     * @param reduxAction Идентификатор виджета
     * @return Redux действие
     */
    public static BindLink createBindLink(ReduxAction reduxAction) {
        ReduxModel reduxModel = null;
        if (reduxAction.getType().equals("n2o/widgets/CHANGE_SELECTED_ID")) {
            reduxModel = ReduxModel.RESOLVE;
            //todo нужна типизация по widgetId и field
            String widgetId = reduxAction.getPayload().get("widgetId").toString();
            return createBindLink(widgetId, reduxModel, "id");
        } else {
            if (reduxAction.getType().equals("n2o/models/UPDATE")) {
                reduxModel = ReduxModel.valueOf(reduxAction.getPayload().get("prefix").toString().toUpperCase());
                String widgetId = reduxAction.getPayload().get("key").toString();
                String field = reduxAction.getPayload().get("field") == null ? null : reduxAction.getPayload().get("field").toString();
                return createBindLink(widgetId, reduxModel, field);
            } else {
                throw new UnsupportedOperationException("Redux action type " + reduxAction.getType() + " unsupported");
            }
        }

    }

    /**
     * Вызвать выделение записи в виджете
     *
     * @param widgetId Идентификатор виджета
     * @param value    Значение
     * @return Redux действие
     */
    public static ReduxAction dispatchSelectedWidget(String widgetId, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("widgetId", widgetId);
        params.put("value", value);
        return new ReduxAction("n2o/widgets/CHANGE_SELECTED_ID", params);
    }

    /**
     * Вызвать обновление поля в модели виджета
     *
     * @param widgetId Идентификатор виджета
     * @param model    Модель виджета
     * @param field    Поле виджета
     * @param value    Значение
     * @return Redux действие
     */
    public static ReduxAction dispatchUpdateModel(String widgetId, ReduxModel model, String field, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("prefix", model.getId());
        params.put("key", widgetId);
        params.put("field", field);
        params.put("value", value);
        return new ReduxAction("n2o/models/UPDATE", params);
    }

    /**
     * Вызвать обновление поля со списком в модели виджета
     *
     * @param widgetId Идентификатор виджета
     * @param model    Модель виджета
     * @param field    Поле виджета
     * @param map      Поле для маппинга
     * @param value    Значение
     * @return Redux действие
     */
    public static ReduxAction dispatchUpdateMapModel(String widgetId, ReduxModel model, String field, String map, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("prefix", model.getId());
        params.put("key", widgetId);
        params.put("field", field);
        params.put("value", value);
        params.put("map", map);
        return new ReduxAction("n2o/models/UPDATE_MAP", params);
    }

    /**
     * Вызвать сортировку виджета
     *
     * @param widgetId  Идентификатор виджета
     * @param field     Поле сортировки
     * @param direction Направление сортировки
     * @return Redux действие
     */
    public static ReduxAction dispatchSortWidget(String widgetId, String field, String sortingParam, Object direction) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("widgetId", widgetId);
        params.put("fieldKey", field);
        params.put("sortDirection", direction);
        params.put("sortParam", sortingParam);
        return new ReduxAction("n2o/widgets/SORT_BY", params);
    }

    public static ModelLink linkFilter(N2oPreFilter preFilter) {
        Object value;
        if (preFilter.getValues() == null) {
            value = ScriptProcessor.resolveExpression(preFilter.getValue());
        } else {
            value = ScriptProcessor.resolveArrayExpression(preFilter.getValues());
        }
        if (StringUtils.isJs(value)) {
            ModelLink link = new ModelLink(preFilter.getRefModel(),
                    CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId()));
            link.setValue(value);
            return link;
        } else {
            return new ModelLink(value);
        }
    }

    public static ModelLink linkQuery(String clientWidgetId, String fieldId, String queryId) {
        ModelLink link = new ModelLink(ReduxModel.RESOLVE, clientWidgetId, fieldId);
        if (PK.equals(fieldId) && queryId != null)
            link.setSubModelQuery(new SubModelQuery(queryId));
        return link;
    }
}
