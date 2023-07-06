import { createSlice, createAction } from '@reduxjs/toolkit'

import OverlayResolver from './OverlayResolver'
import { CLOSE } from './constants'
import { State } from './Overlays'
import { InsertOverlay } from './Actions'

const initialState: State = []

const overlaysSlice = createSlice({
    name: 'n2o/overlays',
    initialState,
    reducers: {
        INSERT_MODAL: {
            /* FIXME бэк не присылает mode, но для drawer и dialog присылает */
            prepare(name, visible, mode, addition) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                        ...addition,
                    },
                })
            },

            reducer(state, action: InsertOverlay) {
                const { name, visible } = action.payload

                state.push({
                    visible,
                    name,
                    mode: 'modal',
                    props: action.payload,
                })
            },
        },

        INSERT_DRAWER: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(name, visible, mode, addition) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                        ...addition,
                    },
                })
            },

            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action: InsertOverlay) {
                const { name, visible, mode } = action.payload

                state.push({
                    visible,
                    name,
                    mode,
                    props: action.payload,
                })
            },
        },

        INSERT_DIALOG: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(name, visible, mode, addition) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                        ...addition,
                    },
                })
            },

            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action: InsertOverlay) {
                const { name, visible, mode } = action.payload

                state.push({
                    visible,
                    name,
                    mode,
                    props: action.payload,
                })
            },
        },

        SHOW(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].visible = true
            }
        },

        HIDE(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].visible = false
            }
        },

        DESTROY(state) {
            return state.slice(0, -1)
        },

        /**
         * Удалить N окон с конца массиве
         */
        DESTROY_OVERLAYS(state, action) {
            return state.slice(0, -action.payload)
        },

        SHOW_PROMPT(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].showPrompt = true
            }
        },

        HIDE_PROMPT(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].showPrompt = false
            }
        },
    },
})

export default overlaysSlice.reducer

// Actions
export const {
    DESTROY: destroyOverlay,
    DESTROY_OVERLAYS: destroyOverlays,
    HIDE: hideOverlay,
    SHOW: showOverlay,
    SHOW_PROMPT: showPrompt,
    HIDE_PROMPT: hidePrompt,
    INSERT_DIALOG: insertDialog,
    INSERT_DRAWER: insertDrawer,
    INSERT_MODAL: insertOverlay,
} = overlaysSlice.actions

/**
 * События при попытке закрыть окно
 * @param {string} name
 * @param {boolean} prompt
 * @return {{payload: {name: string, prompt: boolean}}}
 */

export const closeOverlay = createAction(CLOSE, (name: string, prompt: boolean) => ({
    payload: { name, prompt },
}))
