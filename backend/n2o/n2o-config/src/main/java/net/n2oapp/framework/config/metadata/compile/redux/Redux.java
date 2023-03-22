package net.n2oapp.framework.config.metadata.compile.redux;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.*;
import net.n2oapp.framework.api.script.ScriptProcessor;

import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

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
            ReduxModel reduxModel = ReduxModel.resolve;
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

    public static ModelLink linkParam(N2oParam param, CompileProcessor p) {
        Object value = ScriptProcessor.resolveExpression(param.getValue());
        if (value == null || StringUtils.isJs(value)) {
            ModelLink link = new ModelLink(param.getModel(), getClientDatasourceId(param.getDatasourceId(), p));
            link.setValue(value);
            return link;
        } else {
            return new ModelLink(value);
        }
    }
}
