/**
 * Created by emamoshin on 22.12.2017.
 */
import { createSelector } from 'reselect';

/*
 Базовые селекторы
 */

/**
 * Базовый селектор всех страниц
 * @param state
 */
const pagesSelector = state => {
  return state.pages || {};
};

/*
 Селекторы генераторы
 */

/**
 * Селектор-генератор для получения страницы по ID
 * @param pageId
 */
const makePageByIdSelector = pageId =>
  createSelector(pagesSelector, pagesState => {
    return pagesState[pageId];
  });

/**
 * Селектор-генератор для получения метеданных страницы по ID
 * @param pageId
 */
const makePageMetadataByIdSelector = pageId =>
  createSelector(makePageByIdSelector(pageId), pageState => {
    return pageState && pageState.metadata;
  });

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param pageId
 */
const makePageLoadingByIdSelector = pageId =>
  createSelector(makePageByIdSelector(pageId), pageState => {
    return pageState && pageState.loading;
  });
/**
 * Селектор-генератор для получения статуса ошибки по ID
 * @param pageId
 */
const makePageErrorByIdSelector = pageId =>
  createSelector(makePageByIdSelector(pageId), pageState => {
    return pageState && pageState.error;
  });

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param pageId
 */
const makePageRoutesByIdSelector = pageId =>
  createSelector(makePageMetadataByIdSelector(pageId), pageState => {
    return pageState && pageState.routes;
  });

/*
 Остальные селекторы
 */
const makeWidgetMetadataSelector = (pageId, widgetId) =>
  createSelector(makePageMetadataByIdSelector(pageId), pageState => {
    return pageState && pageState.widgets[widgetId];
  });

const makePageActionsByIdSelector = pageId =>
  createSelector(makePageMetadataByIdSelector(pageId), pageState => {
    return pageState && pageState.actions;
  });

const makePageToolbarByIdSelector = pageId =>
  createSelector(makePageMetadataByIdSelector(pageId), pageState => {
    return pageState && pageState.toolbar;
  });

const makePageTitleByIdSelector = pageId =>
  createSelector(makePageMetadataByIdSelector(pageId), pageState => {
    return pageState && pageState.page && pageState.page.title;
  });

/**
 *  Получение свойства disabled страницы по ее id
 * @param pageId
 */
const makePageDisabledByIdSelector = pageId =>
  createSelector(makePageByIdSelector(pageId), pageState => {
    return pageState && pageState.disabled;
  });

export {
  pagesSelector,
  makePageByIdSelector,
  makePageMetadataByIdSelector,
  makePageLoadingByIdSelector,
  makePageRoutesByIdSelector,
  makeWidgetMetadataSelector,
  makePageActionsByIdSelector,
  makePageToolbarByIdSelector,
  makePageErrorByIdSelector,
  makePageTitleByIdSelector,
  makePageDisabledByIdSelector
};
