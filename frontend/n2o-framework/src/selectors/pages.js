/**
 * Created by emamoshin on 22.12.2017.
 */
import { createSelector } from 'reselect'
import has from 'lodash/has'
import get from 'lodash/get'

import { findDeep } from '../utils/findDeep'

/*
 Базовые селекторы
 */

/**
 * Базовый селектор всех страниц
 * @param state
 */
const pagesSelector = state => state.pages || {}

/*
 Селекторы генераторы
 */

/**
 * Селектор-генератор для получения страницы по ID
 * @param pageId
 */
const makePageByIdSelector = pageId => createSelector(
    pagesSelector,
    pagesState => pagesState[pageId],
)

/**
 * Селектор-генератор для получения метеданных страницы по ID
 * @param pageId
 */
const makePageMetadataByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.metadata,
)

/**
 * Селектор-генератор для получения виджетов страницы по ID
 * @param pageId
 */
const makePageWidgetsByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    (metadata = {}) => (has(metadata, 'widget')
        ? metadata.widget
        : get(findDeep(metadata, 'src', 'FormWidget'), '[0]')),
)

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param pageId
 */
const makePageLoadingByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.loading,
)
/**
 * Селектор-генератор для получения статуса ошибки по ID
 * @param pageId
 */
const makePageErrorByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.error,
)

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param pageId
 */
const makePageRoutesByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.routes,
)

/*
 Остальные селекторы
 */
const makeWidgetMetadataSelector = (pageId, widgetId) => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.widget[widgetId],
)

const makePageActionsByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.actions,
)

const makePageToolbarByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.toolbar,
)

const makePageTitleByIdSelector = pageId => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState && pageState.page && pageState.page.title,
)

/**
 *  Получение свойства disabled страницы по ее id
 * @param pageId
 */
const makePageDisabledByIdSelector = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.disabled,
)
/**
 * Получение свойсва status страницы по ee d
 * @param pageId
 * @return
 */
const makePageStatusByIdSelected = pageId => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState && pageState.status,
)

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
    makePageDisabledByIdSelector,
    makePageWidgetsByIdSelector,
    makePageStatusByIdSelected,
}
