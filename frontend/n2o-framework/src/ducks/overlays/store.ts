import { createSlice, createAction } from '@reduxjs/toolkit'

import OverlayResolver from './OverlayResolver'
import { CLOSE } from './constants'
import { State } from './Overlays'
import { InsertOverlay, Insert, Remove } from './Actions'

const initialState: State = []

/* TODO OverlaysRefactoring перевести все действия открытия overlays на action insert,
    сделать уникальные mode для выбора src, убрать type */
const overlaysSlice = createSlice({
    name: 'n2o/overlays',
    initialState,
    reducers: {
        INSERT_MODAL: {
            prepare(name, visible, mode) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                    },
                })
            },

            reducer(state, action: InsertOverlay) {
                const { name, visible } = action.payload

                state.push({
                    name,
                    visible,
                    mode: 'modal',
                    type: 'page',
                    props: { ...action.payload },
                })
            },
        },

        INSERT_DRAWER: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(name, visible, mode) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                    },
                })
            },

            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action: InsertOverlay) {
                const { name, visible, mode } = action.payload

                state.push({
                    name,
                    visible,
                    mode,
                    type: 'page',
                    props: { ...action.payload },
                })
            },
        },

        INSERT_DIALOG: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(name, visible, mode) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                    },
                })
            },

            // eslint-disable-next-line sonarjs/no-identical-functions
            reducer(state, action: InsertOverlay) {
                const { name, visible, mode } = action.payload

                state.push({
                    name,
                    visible,
                    mode,
                    type: 'page',
                    props: { ...action.payload },
                })
            },
        },

        insert: {
            prepare(name, visible, mode, type, props) {
                return ({
                    payload: {
                        name,
                        visible,
                        mode,
                        type,
                        props,
                    },
                })
            },

            reducer(state, action: Insert) {
                const { name, visible, mode, type, props } = action.payload
                const insertedId = `${type}-${mode}_${props?.target}`

                if (state.some(({ id }) => id === insertedId)) {
                    return
                }

                state.push({
                    name,
                    id: insertedId,
                    visible,
                    mode,
                    type,
                    props,
                })
            },
        },

        remove: {
            prepare(id) {
                return ({
                    payload: { id },
                })
            },

            reducer(state, action: Remove) {
                const { id: removedId } = action.payload

                return state.filter(({ id }) => id !== removedId)
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
    insert,
    remove,
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
