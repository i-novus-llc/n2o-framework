import { createSlice, createAction } from '@reduxjs/toolkit'
import set from 'lodash/set'

import { SET_WIDGET_METADATA } from '../widgets/constants'

import { MAP_URL } from './constants'
import PageResolver from './PageResolver'

/**
 * @type {PagesStore.state}
 */
export const initialState = {}

const pageSlice = createSlice({
    name: 'n2o/pages',
    initialState,
    reducers: {
        METADATA_REQUEST: {
            /**
             * @param {string} pageId - id страницы
             * @param {boolean} rootPage - признак модального окна
             * @param {string} pageUrl - url с возможностью плейсхолдерами из mapping
             * @param {Object.<string, string>} mapping - маппинг для плейсхолдеров pageUrl
             * @property {{payload: PagesStore.metaDataRequestPayload}}
             */
            prepare(pageId, rootPage, pageUrl, mapping) {
                return {
                    payload: { pageId, rootPage, pageUrl, mapping },
                }
            },

            /**
             * Запрос за метаданными страницы
             * @param {PagesStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {PagesStore.metaDataRequestPayload} action.payload
             */
            reducer(state, action) {
                const { pageId } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = true
                state[pageId].error = false
                state[pageId].metadata = {}
            },
        },

        METADATA_SUCCESS: {
            /**
             * @param {string} pageId - уникальный индефикатор виджета
             * @param {Object.<string, any>} json - response в виде json
             * @return {{payload: PagesStore.metaDataSuccessPayload}}
             */
            prepare(pageId, json) {
                return ({
                    payload: { pageId, json },
                })
            },

            /**
             * Успешный запрос за данными.
             * @param {PagesStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {PagesStore.metaDataSuccessPayload} action.payload
             */
            reducer(state, action) {
                const { pageId, json } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = false
                state[pageId].error = false
                state[pageId].metadata = json
            },
        },

        METADATA_FAIL: {
            /**
             * @param {string} pageId - уникальный индефикатор виджета
             * @param {Object.<string, any>} err - response в виде json
             * @param {Object.<string, any>} meta - meta для экшена
             * @return {PagesStore.metaDataFailPayload}
             */
            prepare(pageId, err, meta) {
                return ({
                    payload: { pageId, err },
                    meta,
                })
            },

            /**
             * Ошибка при запросе за данными.
             * @param {PagesStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {PagesStore.metaDataFailPayload.payload} action.payload
             */
            reducer(state, action) {
                const { pageId, err } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = false
                state[pageId].error = err
            },
        },

        /**
         * Сброс к значению поумолчанию.
         * @param {PagesStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        RESET(state, action) {
            if (!state[action.payload]) {
                state[action.payload] = PageResolver.defaultState
            }

            state[action.payload] = PageResolver.defaultState
        },

        /**
         * Заблокировать страницу.
         * @param {PagesStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        DISABLE(state, action) {
            if (!state[action.payload]) {
                state[action.payload] = PageResolver.defaultState
            }

            state[action.payload].disabled = true
        },

        /**
         * Разблокировать страницу.
         * @param {PagesStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        ENABLE(state, action) {
            if (!state[action.payload]) {
                state[action.payload] = PageResolver.defaultState
            }
            state[action.payload].disabled = false
        },

        SET_STATUS: {
            /**
             * @param { string } pageId
             * @param { number } status
             * @return {{payload: PagesStore.setStatusPayload}}
             */
            prepare(pageId, status) {
                return ({
                    payload: { pageId, status },
                })
            },

            /**
             * Присвоить статус запроса
             * @param {PagesStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {PagesStore.setStatusPayload} action.payload
             */
            reducer(state, action) {
                const { pageId, status } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].status = status
            },
        },
    },

    extraReducers: {
        [SET_WIDGET_METADATA](state, action) {
            const { pageId, widgetId, metadata } = action.payload

            if (!state[pageId]) {
                state[pageId] = PageResolver.defaultState
            }

            set(state[pageId], ['metadata', 'widgets', widgetId], metadata)
        },
    },
})

export default pageSlice.reducer

// Actions
export const mapUrl = createAction(MAP_URL, pageId => ({
    payload: { pageId },
}))

export const {
    DISABLE: disablePage,
    ENABLE: enablePage,
    METADATA_FAIL: metadataFail,
    RESET: resetPage,
    METADATA_REQUEST: metadataRequest,
    METADATA_SUCCESS: metadataSuccess,
    SET_STATUS: setStatus,
} = pageSlice.actions
