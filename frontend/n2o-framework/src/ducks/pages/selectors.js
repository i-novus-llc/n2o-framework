import { createSelector } from '@reduxjs/toolkit'
import has from 'lodash/has'
import get from 'lodash/get'

import { findDeep } from '../../utils/findDeep'

/*
 Базовые селекторы
 */

/**
 * Базовый селектор всех страниц
 * @param {Object.<any, any>} state
 * @return {Pages.store}
 */
export const pagesSelector = state => state.pages || {}

/*
 Селекторы генераторы
 */

/**
 * Селектор-генератор для получения страницы по ID
 * @param {string} pageId
 * @return {Pages.item | undefined}
 */
export const makePageByIdSelector = pageId => createSelector(
    pagesSelector,
    pagesState => pagesState[pageId],
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
 * Селектор-генератор для получения виджетов страницы по ID
 * @param {string} pageId
 * @return {any}
 */
export const makePageWidgetsByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    (metadata = {}) => (has(metadata, 'widget')
        ? metadata.widget
        : get(findDeep(metadata, 'src', 'FormWidget'), '[0]')),
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
 * Селектор-генератор для получения статуса загрузки по ID
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageRoutesByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.routes,
)

/*
 Остальные селекторы
 */

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
