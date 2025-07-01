import { createSlice, createAction } from '@reduxjs/toolkit'

import OverlayResolver from './OverlayResolver'
import { CLOSE } from './constants'
import { State } from './Overlays'
import type { InsertOverlay, Insert, Remove } from './Actions'

export const initialState: State = []

/* TODO OverlaysRefactoring перевести все действия открытия overlays на action insert,
    сделать уникальные mode для выбора src, убрать type */
export const overlaysSlice = createSlice({
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

            reducer(state, { payload, meta }: InsertOverlay) {
                const { name, visible } = payload
                const { pageId } = meta || {}

                state.push({
                    name,
                    visible,
                    mode: 'modal',
                    type: 'page',
                    /* TODO OverlaysRefactoring перевести на id */
                    id: name,
                    parentPage: pageId,
                    props: { ...payload },
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
            reducer(state, { payload }: InsertOverlay) {
                const { name, visible, mode } = payload

                state.push({
                    name,
                    visible,
                    mode,
                    type: 'page',
                    /* TODO OverlaysRefactoring перевести на id */
                    id: name,
                    props: { ...payload },
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

            reducer(state, { payload }: Insert) {
                const { name, visible, mode, type, props } = payload
                const baseId = `${type}-${mode}_${props?.target}`
                const overlayId = props?.operation?.id ? `${baseId}_${props.operation.id}` : baseId

                if (state.some(({ id }) => id === overlayId)) { return }

                state.push({
                    name,
                    id: overlayId,
                    visible,
                    mode,
                    type,
                    props,
                })
            },
        },

        remove: {
            prepare(id?: string) {
                return ({ payload: { id } })
            },

            reducer(state, { payload }: Remove) {
                const { id: overlayId } = payload

                if (overlayId) {
                    state = state.filter(({ id }) => id !== overlayId)

                    return state
                }

                state.splice(state.length - 1, 1)

                return state
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
