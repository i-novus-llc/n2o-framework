import { createSlice } from '@reduxjs/toolkit'
import set from 'lodash/set'

import { SET_WIDGET_METADATA } from '../widgets/constants'

import PageResolver from './PageResolver'
import { Metadata, State } from './Pages'
import { MetadataFail, MetadataRequest, MetadataSuccess, Reset, SetPageLoading, SetStatus } from './Actions'

export const initialState: State = {}

export const pageSlice = createSlice({
    name: 'n2o/pages',
    initialState,
    reducers: {
        METADATA_REQUEST: {
            prepare(pageId, rootPage, pageUrl, mapping) {
                return {
                    payload: { pageId, rootPage, pageUrl, mapping },
                }
            },

            reducer(state, action: MetadataRequest) {
                const { pageId, pageUrl } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = true
                state[pageId].error = false
                state[pageId].metadata = {}
                state[pageId].pageUrl = pageUrl
            },
        },

        METADATA_SUCCESS: {
            prepare(pageId, json, pageUrl) {
                return ({
                    payload: { pageId, json, pageUrl },
                })
            },

            reducer(state, action: MetadataSuccess) {
                const { pageId, json, pageUrl } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = false
                state[pageId].error = false
                state[pageId].metadata = json
                state[pageId].pageUrl = pageUrl
            },
        },

        METADATA_FAIL: {
            prepare(pageId, err, meta) {
                return ({
                    payload: { pageId, err },
                    meta,
                })
            },

            reducer(state, action: MetadataFail) {
                const { pageId, err } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = false
                state[pageId].error = err
            },
        },

        RESET(state, action: Reset) {
            if (!state[action.payload]) {
                state[action.payload] = PageResolver.defaultState
            }

            state[action.payload] = PageResolver.defaultState
        },

        DISABLE(state, action) {
            if (!state[action.payload]) {
                state[action.payload] = PageResolver.defaultState
            }

            state[action.payload].disabled = true
        },

        ENABLE(state, action) {
            if (!state[action.payload]) {
                state[action.payload] = PageResolver.defaultState
            }
            state[action.payload].disabled = false
        },

        SET_STATUS: {
            prepare(pageId: string, status: number | null) {
                return ({
                    payload: { pageId, status },
                })
            },

            reducer(state, action: SetStatus) {
                const { pageId, status } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].status = status
            },
        },

        SET_PAGE_LOADING: {
            prepare(pageId, loading, spinner) {
                return ({
                    payload: { pageId, loading, spinner },
                })
            },

            reducer(state: State, action: SetPageLoading) {
                const { pageId, loading, spinner } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].loading = loading

                if (loading) {
                    state[pageId].spinner = spinner
                } else {
                    delete state[pageId].spinner
                }
            },
        },
    },

    extraReducers: {
        [SET_WIDGET_METADATA](state: State, action) {
            const {
                pageId,
                widgetId,
                metadata,
            }: { pageId: string, widgetId: string, metadata: Metadata } = action.payload

            if (!state[pageId]) {
                state[pageId] = PageResolver.defaultState
            }

            set(state[pageId], ['metadata', 'widgets', widgetId], metadata)
        },
    },
})

export default pageSlice.reducer

export const {
    DISABLE: disablePage,
    ENABLE: enablePage,
    METADATA_FAIL: metadataFail,
    RESET: resetPage,
    METADATA_REQUEST: metadataRequest,
    METADATA_SUCCESS: metadataSuccess,
    SET_STATUS: setStatus,
    SET_PAGE_LOADING: setPageLoading,
} = pageSlice.actions
