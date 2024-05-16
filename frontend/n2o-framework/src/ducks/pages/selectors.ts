import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

/*
 Базовые селекторы
 */

/**
 * Базовый селектор всех страниц
 * @param { GlobalState } state
 * @return TPageState
 */
export const pagesSelector = (state: State) => state.pages || EMPTY_OBJECT

/*
 Селекторы генераторы
 */

/**
 * Селектор-генератор для получения страницы по ID
 */
export const makePageByIdSelector = (pageId: string) => createSelector(
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
export const makePageLoadingByIdSelector = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.loading,
)

/**
 * Селектор-генератор для получения статуса ошибки по ID
 * @param {string} pageId
 * @return {boolean | undefined}
 */
export const makePageErrorByIdSelector = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.error,
)

/**
 *  Получение свойства disabled страницы по ее id
 * @param {string} pageId
 * @return {boolean | undefined}
 */
export const makePageDisabledByIdSelector = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.disabled,
)

/**
 * Получение свойсва status страницы по ee d
 * @param {string} pageId
 * @return {number | undefined}
 */
export const makePageStatusByIdSelected = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.status,
)

/**
 * Получение свойсва spinner страницы по ee d
 * @param {string} pageId
 * @return {number | undefined}
 */
export const makePageSpinnerByIdSelected = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.spinner,
)

/**
 * Селектор-генератор для получения метеданных страницы по ID
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageMetadataByIdSelector = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.metadata,
)

/**
 * Селектор-генератор для получения метеданных страницы по ID
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageUrlByIdSelector = (pageId: string) => createSelector(
    makePageByIdSelector(pageId),
    pageState => pageState?.pageUrl,
)

/**
 * Селектор-генератор для получения статуса загрузки по ID
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageRoutesByIdSelector = (pageId: string) => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState?.routes,
)

/**
 * Селектро toolbar из metadata по id
 * @param {string} pageId
 * @return {Object.<string, any> | undefined}
 */
export const makePageToolbarByIdSelector = (pageId: string) => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState?.toolbar,
)

/**
 * Селектро title из metadata по id
 * @param {string} pageId
 * @return {string | undefined}
 */
export const makePageTitleByIdSelector = (pageId: string) => createSelector(
    makePageMetadataByIdSelector(pageId),
    pageState => pageState?.page?.title,
)
