import { createSelector } from 'reselect';
import get from 'lodash/get';

import { FORM, TABLE } from '../components/widgets/widgetTypes';

/*
  Базовые селекторы
*/

/**
 * Базовый селектор всех виджетов
 * @param state
 */
const widgetsSelector = (state = {}) => {
  return state.widgets || {};
};

/*
  Селекторы генераторы
*/

/**
 * Селектор-генератор для получения виджета по ID
 * @param widgetId
 */
const makeWidgetByIdSelector = widgetId => {
  return createSelector(
    widgetsSelector,
    widgetsState => {
      return widgetsState[widgetId] || {};
    }
  );
};

/**
 * Селектор-генератор для получения свойства виджета - isInit
 * @param widgetId
 */
const makeWidgetIsInitSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => {
      return widgetState.isInit;
    }
  );

/**
 * Селектор-генератор для получения свойства виджета - isVisible
 * @param widgetId
 */
const makeWidgetVisibleSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isVisible
  );

/**
 * Селектор-генератор для получения свойства виджета - isEnabled
 * @param widgetId
 */
const makeWidgetEnabledSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isEnabled
  );

/**
 * Селектор-генератор для получения свойства виджета - isLoading
 * @param widgetId
 */
const makeWidgetLoadingSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isLoading
  );

/**
 * Селектор-генератор для получения свойства виджета - size
 * @param widgetId
 */
const makeWidgetSizeSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.size
  );

/**
 * Селектор-генератор для получения свойства виджета - count
 * @param widgetId
 */
const makeWidgetCountSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.count
  );

/**
 * Селектор-генератор для получения свойства виджета - page
 * @param widgetId
 */
const makeWidgetPageSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.page
  );

const makeWidgetPageIdSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.pageId
  );

/**
 * Селектор-генератор для получения свойства виджета - sorting
 * @param widgetId
 */
const makeWidgetSortingSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.sorting
  );

/**
 * Селектор-генератор для получения свойства виджета - isFilterVisible
 * @param widgetId
 */
const makeWidgetFilterVisibilitySelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => {
      return widgetState.isFilterVisible;
    }
  );

/**
 * Селектор-генератор для получения свойства виджета - sorting
 * @param widgetId
 */
const makeWidgetValidationSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.validation
  );

export const getWidgetFieldValidation = (state, widgetId, fieldId) =>
  get(state, ['widgets', widgetId, 'validation', fieldId]);

const makeSelectedIdSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.selectedId
  );

const makeIsActiveSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.isActive
  );

const makeTypeSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.type
  );

const makeWidgetDataProviderSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.dataProvider
  );

const isAnyTableFocusedSelector = createSelector(
  widgetsSelector,
  widgetsState => {
    return Object.values(widgetsState).some(
      widget =>
        (widget.type === TABLE || widget.type === FORM) && widget.isActive
    );
  }
);

const makeWidgetErrorSelector = widgetId =>
  createSelector(
    makeWidgetByIdSelector(widgetId),
    widgetState => widgetState.error
  );

/*
  Остальные селекторы
*/

const makeFormModelPrefixSelector = formName =>
  createSelector(
    makeWidgetByIdSelector(formName),
    widgetState => widgetState.modelPrefix || 'resolve'
  );

export {
  widgetsSelector,
  makeWidgetByIdSelector,
  makeWidgetIsInitSelector,
  makeWidgetVisibleSelector,
  makeWidgetEnabledSelector,
  makeWidgetLoadingSelector,
  makeWidgetSizeSelector,
  makeWidgetCountSelector,
  makeWidgetSortingSelector,
  makeWidgetFilterVisibilitySelector,
  makeWidgetValidationSelector,
  makeSelectedIdSelector,
  makeIsActiveSelector,
  makeWidgetPageSelector,
  makeWidgetPageIdSelector,
  makeTypeSelector,
  makeWidgetDataProviderSelector,
  isAnyTableFocusedSelector,
  makeWidgetErrorSelector,
  makeFormModelPrefixSelector,
};
