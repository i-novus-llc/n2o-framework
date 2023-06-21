import { createSelector } from '@reduxjs/toolkit'

/*
 Базовые селекторы
 */

/**
 * Базовый селектор всех страниц
 * @param { GlobalState } state
 * @return TPageState
 */
export const pagesSelector = state => state.pages || {}

/*
 Селекторы генераторы
 */

/**
 * Селектор-генератор для получения страницы по ID
 */
export const makePageByIdSelector = pageId => createSelector(
    [
        pagesSelector,
    ],
    pagesState => pagesState[pageId],
)

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param {string} pageId
 * @return {boolean | undefined}
 */
export const makePageLoadingByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.loading,
)

/**
 * Селектор-генератор для получения статуса ошибки по ID
 * @param {string} pageId
 * @return {boolean | undefined}
 */
export const makePageErrorByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.error,
)

/**
 *  Получение свойства disabled страницы по ее id
 * @param {string} pageId
 * @return {boolean | undefined}
 */
export const makePageDisabledByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.disabled,
)

/**
 * Получение свойсва status страницы по ee d
 * @param {string} pageId
 * @return {number | undefined}
 */
export const makePageStatusByIdSelected = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.status,
)

/**
 * Получение свойсва spinner страницы по ee d
 * @param {string} pageId
 * @return {number | undefined}
 */
export const makePageSpinnerByIdSelected = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.spinner,
)

/**
 * Селектор-генератор для получения метеданных страницы по ID
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageMetadataByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.metadata,
)

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageRoutesByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.routes,
)

/**
 * Селектро toolbar из metadata по id
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageToolbarByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.toolbar,
)

/**
 * Селектро title из metadata по id
 * @param {string} pageId
 * @return {string | undefined}
 */
export const makePageTitleByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.page && pageState.page.title,
)
