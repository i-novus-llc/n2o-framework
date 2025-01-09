import { createSlice } from '@reduxjs/toolkit'

import PageResolver from './PageResolver'
import { State } from './Pages'
import {
    MetadataFail,
    MetadataRequest,
    MetadataSuccess,
    Reset,
    SetLocation,
    SetScroll,
} from './Actions'

export const initialState: State = {}

export const pageSlice = createSlice({
    name: 'n2o/pages',
    initialState,
    reducers: {
        METADATA_REQUEST: {
            prepare(pageId, rootPage, pageUrl, mapping, parentId) {
                return {
                    payload: { pageId, rootPage, pageUrl, mapping, parentId },
                }
            },

            reducer(state, action: MetadataRequest) {
                const { pageId, pageUrl, rootPage, parentId } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].id = pageId
                state[pageId].loading = true
                state[pageId].error = false
                state[pageId].metadata = {}
                state[pageId].pageUrl = pageUrl
                state[pageId].rootPage = rootPage
                state[pageId].parentId = parentId
            },
        },

        METADATA_SUCCESS: {
            prepare(pageId, json, pageUrl, rootPage, rootChild) {
                return ({
                    payload: { pageId, json, pageUrl, rootPage, rootChild },
                })
            },

            reducer(state, action: MetadataSuccess) {
                const { pageId, json, pageUrl, rootPage } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState

                    // TODO warn if not root page
                }

                state[pageId].id = json.id || pageId
                state[pageId].loading = false
                state[pageId].error = false
                state[pageId].metadata = json
                state[pageId].pageUrl = json.routes?.path || pageUrl
                state[pageId].rootPage = rootPage

                if (!rootPage && !state[pageId].parentId) {
                    const [pathname, search] = state[pageId].pageUrl.split('?')

                    state[pageId].location = {
                        hash: '',
                        state: undefined,
                        pathname,
                        search: search ? `?${search}` : '',
                    }
                }
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

        setLocation: {
            prepare(pageId, location) {
                return ({
                    payload: { pageId, location },
                })
            },

            reducer(state, action: SetLocation) {
                const { pageId, location } = action.payload

                if (!state[pageId]) {
                    // todo warn page isn't exist

                    return
                }

                if (state[pageId].rootPage || state[pageId].parentId) {
                    // todo warn page isn't anchor

                    return
                }

                if (typeof location === 'string') {
                    const [pathname, search] = location.split('?')

                    state[pageId].location = {
                        hash: '',
                        state: undefined,
                        pathname,
                        search: search ? `?${search}` : '',
                    }

                    return
                }

                state[pageId].location = location
            },
        },

        SET_PAGE_SCROLLING: {
            prepare(pageId, scroll) {
                return ({
                    payload: { pageId, scroll },
                })
            },

            reducer(state: State, action: SetScroll) {
                const { pageId, scroll } = action.payload

                if (!state[pageId]) {
                    state[pageId] = PageResolver.defaultState
                }

                state[pageId].scroll = scroll
            },
        },

        RESET(state, action: Reset) {
            delete state[action.payload]
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
    setLocation,
    SET_PAGE_SCROLLING: setPageScrolling,
} = pageSlice.actions
