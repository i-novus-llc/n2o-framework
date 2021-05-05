import get from 'lodash/get'

import {
    INSERT_MODAL,
    INSERT_DRAWER,
    INSERT_DIALOG,
    DESTROY,
    DESTROY_OVERLAYS,
    HIDE,
    SHOW,
    SHOW_PROMPT,
    HIDE_PROMPT,
} from '../constants/overlays'

const defaultState = {
    visible: false,
    name: null,
    showPrompt: false,
    mode: 'modal',
    props: {},
}

function resolve(state = defaultState, action) {
    switch (action.type) {
        case INSERT_MODAL:
            return { ...state,
                visible: action.payload.visible,
                name: action.payload.name,
                mode: 'modal',
                props: { ...action.payload } }
        case INSERT_DRAWER:
            return { ...state,
                visible: action.payload.visible,
                name: action.payload.name,
                mode: 'drawer',
                props: { ...action.payload } }
        case INSERT_DIALOG:
            return { ...state,
                visible: action.payload.visible,
                name: action.payload.name,
                mode: 'dialog',
                props: { ...action.payload } }
        case SHOW:
            return { ...state, visible: true }
        case HIDE:
            return { ...state, visible: false }
        default:
            return state
    }
}

/**
 * Редюсер экшенов оверлеев
 */
export default function overlays(state = [], action) {
    const index = state.findIndex(
        overlay => overlay.name === get(action, 'payload.name'),
    )

    switch (action.type) {
        case INSERT_MODAL:
        case INSERT_DRAWER:
        case INSERT_DIALOG:
            return [...state, resolve({}, action)]
        case SHOW:
            if (index >= 0) {
                state[index].visible = true

                return state.slice()
            }

            return state
        case HIDE:
            if (index >= 0) {
                state[index].visible = false

                return state.slice()
            }

            return state
        case DESTROY:
            return state.slice(0, -1)
        case DESTROY_OVERLAYS:
            return state.slice(0, -action.payload.count)
        case SHOW_PROMPT:
            state[index].showPrompt = true

            return state.slice()
        case HIDE_PROMPT:
            state[index].showPrompt = false

            return state.slice()
        default:
            return state
    }
}
