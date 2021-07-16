import { createSlice, createAction } from '@reduxjs/toolkit'
import omit from 'lodash/omit'

import OverlayResolver from './OverlayResolver'
import { CLOSE } from './constants'

const initialState = []

const overlaysSlice = createSlice({
    name: 'n2o/overlays',
    initialState,
    reducers: {
        INSERT_MODAL: {
            /**
             * @param {string} name
             * @param {boolean} visible
             * @param {string} mode
             * @param {OverlayStore.item} addition
             * @return {{payload: OverlayStore.insertOverlayPayload}}
             */
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
            /**
             * Регистрация модального окна
             * @param {OverlayStore.store} state
             * @param {Object} action
             * @param {string} action.type
             * @param {OverlayStore.insertOverlayPayload} action.payload
             */
            reducer(state, action) {
                const { name, visible } = action.payload

                state.push({
                    visible,
                    name,
                    mode: 'modal',
                    props: omit(action.payload, 'mode'),
                })
            },
        },

        INSERT_DRAWER: {
            /**
             * @param {string} name
             * @param {boolean} visible
             * @param {string} mode
             * @param {OverlayStore.item} addition
             * @return {{payload: OverlayStore.insertDrawerPayload}}
             */
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
            /**
             * Регистрация дравера
             * @param {OverlayStore.store} state
             * @param {Object} action
             * @param {string} action.type
             * @param {OverlayStore.insertDrawerPayload} action.payload
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { name, visible } = action.payload

                state.push({
                    visible,
                    name,
                    mode: 'drawer',
                    props: action.payload,
                })
            },
        },

        INSERT_DIALOG: {
            /**
             * @param {string} name
             * @param {boolean} visible
             * @param {OverlayStore.item} addition
             * @return {{payload: OverlayStore.insertDialogPayload}}
             */
            prepare(name, visible, addition) {
                return ({
                    payload: {
                        name,
                        visible,
                        ...addition,
                    },
                })
            },

            /**
             * Регистрация диалога
             * @param {OverlayStore.store} state
             * @param {Object} action
             * @param {string} action.type
             * @param {OverlayStore.insertDialogPayload} action.payload
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action) {
                const { name, visible } = action.payload

                state.push({
                    visible,
                    name,
                    mode: 'dialog',
                    props: action.payload,
                })
            },
        },

        /**
         * Показать окно
         * @param {OverlayStore.store} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        SHOW(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].visible = true
            }
        },

        /**
         * Скрыть окно
         * @param {OverlayStore.store} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        HIDE(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].visible = false
            }
        },

        /**
         * Удалить последнее окно в массиве
         * @param {OverlayStore.store} state
         */
        DESTROY(state) {
            return state.slice(0, -1)
        },

        /**
         * Удалить N окон с конца массиве
         * @param {OverlayStore.store} state
         * @param {Object} action
         * @param {string} action.type
         * @param {number} action.payload
         */
        DESTROY_OVERLAYS(state, action) {
            return state.slice(0, -action.payload)
        },

        /**
         * Показать подтверждение закрытия окна
         * @param {OverlayStore.store} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        SHOW_PROMPT(state, action) {
            const index = OverlayResolver.findIndexByNameInArray(state, action.payload)

            if (index >= 0) {
                state[index].showPrompt = true
            }
        },

        /**
         * Скрыть подтверждение закрытия окна
         * @param {OverlayStore.store} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
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

export const closeOverlay = createAction(CLOSE, (name, prompt) => ({
    payload: { name, prompt },
}))
