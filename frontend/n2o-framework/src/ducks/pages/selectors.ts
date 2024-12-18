import { createSelector } from '@reduxjs/toolkit'

import { State } from '../State'
import { EMPTY_OBJECT } from '../../utils/emptyTypes'

import { Page } from './Pages'

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

export const makeRootPageSelector = () => createSelector(
    [
        pagesSelector,
    ],
    pagesState => Object.values(pagesState).find(page => page.rootPage),
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

function getParentPage(pages: Record<string, Page>, pageId: string): Page | null {
    const page = pages[pageId]

    if (!page) { return null }
    if (page.rootPage) { return page }
    if (page.parentId) { return getParentPage(pages, page.parentId) }

    return page
}

export const makeAnchorLocationByIdSelector = (pageId: string) => createSelector(
    pagesSelector,
    (pages) => {
        const page = getParentPage(pages, pageId)

        return page?.rootPage ? undefined : page?.location
    },
)

export const makeIsRootChildByIdSelector = (pageId: string) => createSelector(
    pagesSelector,
    pages => (!!(getParentPage(pages, pageId)?.rootPage)),
)
