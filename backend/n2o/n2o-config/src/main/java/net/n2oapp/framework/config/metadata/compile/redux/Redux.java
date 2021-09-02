package net.n2oapp.framework.config.metadata.compile.redux;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.*;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
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
     * @param datasource Идентификатор источника данных
     * @param model      Модель
     * @param field      Поле
     * @return Redux действие
     */
    @Deprecated
    public static BindLink createBindLink(String datasource, ReduxModel model, String field) {
        //todo если поле genders*.id то нужно его превращать через js в массив и сетить в value
        return new ModelLink(model, datasource, field);
    }

    /**
     * Создать ссылку на Redux модель виджета
     *
     * @param datasource Идентификатор источника данных
     * @param model      Модель
     * @return Redux действие
     */
    @Deprecated
    public static BindLink createBindLink(String datasource, ReduxModel model) {
        return new ModelLink(model, datasource);
    }

    /**
     * Создать ссылку на сортировку в состоянии виджета
     *
     * @param datasource Идентификатор источника данных
     * @param fieldId    Поле сортировки
     * @return Ссылка на состояние виджета
     */
    public static BindLink createSortLink(String datasource, String fieldId) {
        return new BindLink(String.format("widgets['%s'].sorting.%s", datasource, fieldId));
    }

    /**
     * Создать ссылку на активный элемент региона
     *
     * @param regionId Идентификатор региона
     * @return Ссылка на активный элемент региона
     */
    public static BindLink createActiveRegionEntityLink(String regionId) {
        return new BindLink("regions." + regionId + ".activeEntity");
    }

    /**
     * Создать ссылку на основе Redux действия
     *
     * @param reduxAction Идентификатор виджета
     * @return Redux действие
     */
    public static BindLink createBindLink(ReduxAction reduxAction) {
        if (reduxAction.getType().equals("n2o/widgets/CHANGE_SELECTED_ID")) {
            ReduxModel reduxModel = ReduxModel.RESOLVE;
            //todo нужна типизация по widgetId и field
            String widgetId = ((SelectedWidgetPayload) reduxAction.getPayload()).getWidgetId();
            return createBindLink(widgetId, reduxModel, "id");
        } else {
            if (reduxAction.getType().equals("n2o/models/UPDATE")) {
                UpdateModelPayload payload = (UpdateModelPayload) reduxAction.getPayload();
                return createBindLink(payload.getKey(), ReduxModel.valueOf(payload.getPrefix().toUpperCase()), payload.getField());
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
        SelectedWidgetPayload payload = new SelectedWidgetPayload(widgetId, value);
        return new ReduxAction("n2o/widgets/CHANGE_SELECTED_ID", payload);
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
        UpdateModelPayload payload = new UpdateModelPayload(model.getId(), widgetId, field, value);
        return new ReduxAction("n2o/models/UPDATE", payload);
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
        UpdateMapModelPayload payload = new UpdateMapModelPayload(model.getId(), widgetId, field, value, map);
        return new ReduxAction("n2o/models/UPDATE_MAP", payload);
    }

    /**
     * Вызвать сортировку виджета
     *
     * @param widgetId  Идентификатор виджета
     * @param field     Поле сортировки
     * @param direction Направление сортировки
     * @return Redux действие
     */
    public static ReduxAction dispatchSortWidget(String widgetId, String field, Object direction) {
        SortWidgetPayload payload = new SortWidgetPayload(widgetId, field, direction);
        return new ReduxAction("n2o/widgets/SORT_BY", payload);
    }

    /**
     * Установить активный элемент региона
     *
     * @param regionId          Идентификатор региона
     * @param regionActiveParam Параметр активного элемента
     * @return Redux действие
     */
    public static ReduxAction dispatchSetActiveRegionEntity(String regionId, String regionActiveParam) {
        SetActiveRegionEntityPayload setActiveRegionEntityPayload = new SetActiveRegionEntityPayload(regionId, ":" + regionActiveParam);
        return new ReduxAction("n2o/regions/SET_ACTIVE_REGION_ENTITY", setActiveRegionEntityPayload);
    }

    /**
     * Создание modelLink для префильтра
     *
     * @param preFilter
     * @return
     */
    public static ModelLink linkParam(N2oPreFilter preFilter, CompileProcessor p) {
        Object value;
        if (preFilter.getValues() == null) {
            value = ScriptProcessor.resolveExpression(preFilter.getValue());
        } else {
            value = ScriptProcessor.resolveArrayExpression(preFilter.getValues());
        }
        if (StringUtils.isJs(value)) {
            PageScope pageScope = p.getScope(PageScope.class);
            String widgetId = CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId());
            ModelLink link = new ModelLink(preFilter.getRefModel(),
                    pageScope == null || pageScope.getWidgetIdDatasourceMap() == null ?
                            widgetId : pageScope.getWidgetIdDatasourceMap().get(widgetId));
            link.setValue(value);
            return link;
        } else {
            return new ModelLink(value);
        }
    }

    public static ModelLink linkParam(N2oParam param, CompileProcessor p) {
        Object value = ScriptProcessor.resolveExpression(param.getValue());
        if (value == null || StringUtils.isJs(value)) {
            PageScope pageScope = p.getScope(PageScope.class);
            String widgetId = CompileUtil.generateWidgetId(param.getRefPageId(), param.getRefWidgetId());
            ModelLink link = new ModelLink(param.getRefModel(),
                    pageScope == null || pageScope.getWidgetIdDatasourceMap() == null ?
                            widgetId : pageScope.getWidgetIdDatasourceMap().get(widgetId));
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
