import mapValues from 'lodash/mapValues'

import {
    CHANGE_BUTTON_VISIBILITY,
    CHANGE_BUTTON_TITLE,
    CHANGE_BUTTON_COUNT,
    CHANGE_BUTTON_SIZE,
    CHANGE_BUTTON_COLOR,
    CHANGE_BUTTON_DISABLED,
    TOGGLE_BUTTON_DISABLED,
    TOGGLE_BUTTON_VISIBILITY,
    REGISTER_BUTTON,
    CHANGE_BUTTON_HINT,
    CHANGE_BUTTON_MESSAGE,
    CHANGE_BUTTON_ICON,
    CHANGE_BUTTON_CLASS,
    CHANGE_BUTTON_STYLE,
    REMOVE_BUTTON,
} from '../constants/toolbar'
import { RESET_STATE } from '../constants/widgets'

export const buttonState = {
    isInit: true,
    visible: true,
    count: 0,
    size: null,
    color: null,
    title: null,
    hint: null,
    message: null,
    icon: null,
    disabled: false,
    loading: false,
    error: null,
    conditions: null,
}

function resolve(state = buttonState, action) {
    switch (action.type) {
        case CHANGE_BUTTON_VISIBILITY:
            return { ...state, visible: action.payload.visible }
        case TOGGLE_BUTTON_VISIBILITY:
            return { ...state, visible: !state.visible }
        case TOGGLE_BUTTON_DISABLED:
            return { ...state, disabled: !state.disabled }
        case CHANGE_BUTTON_TITLE:
            return { ...state, title: action.payload.title }
        case CHANGE_BUTTON_DISABLED:
            return { ...state, disabled: action.payload.disabled }
        case CHANGE_BUTTON_SIZE:
            return { ...state, size: action.payload.size }
        case CHANGE_BUTTON_COLOR:
            return { ...state, color: action.payload.color }
        case CHANGE_BUTTON_COUNT:
            return { ...state, count: action.payload.count }
        case CHANGE_BUTTON_MESSAGE:
            return { ...state, message: action.payload.message }
        case CHANGE_BUTTON_HINT:
            return { ...state, hint: action.payload.hint }
        case CHANGE_BUTTON_ICON:
            return { ...state, icon: action.payload.icon }
        case CHANGE_BUTTON_CLASS:
            return { ...state, className: action.payload.className }
        case CHANGE_BUTTON_STYLE:
            return { ...state, style: action.payload.style }
        case RESET_STATE:
            return { ...state, isInit: false }
        default:
            return state
    }
}

/**
 * Редюсер колонок
 * @ignore
 */
export default function toolbar(state = {}, action) {
    const { key, buttonId } = action.payload || {}

    switch (action.type) {
        case REGISTER_BUTTON:
            return { ...state,
                [key]: {
                    ...state[key],
                    [buttonId]: { ...buttonState, ...action.payload },
                } }
        case CHANGE_BUTTON_VISIBILITY:
        case CHANGE_BUTTON_TITLE:
        case CHANGE_BUTTON_COUNT:
        case CHANGE_BUTTON_SIZE:
        case CHANGE_BUTTON_COLOR:
        case CHANGE_BUTTON_DISABLED:
        case TOGGLE_BUTTON_DISABLED:
        case TOGGLE_BUTTON_VISIBILITY:
        case CHANGE_BUTTON_MESSAGE:
        case CHANGE_BUTTON_HINT:
        case CHANGE_BUTTON_ICON:
        case CHANGE_BUTTON_STYLE:
        case CHANGE_BUTTON_CLASS: {
            return {
                ...state,
                [key]: {
                    ...state[key],
                    [buttonId]: resolve(state[key][buttonId], action),
                },
            }
        }
        case REMOVE_BUTTON: {
            return {
                ...state,
                [key]: undefined,
            }
        }
        case RESET_STATE: {
            const { widgetId } = action.payload

            return {
                ...state,
                [widgetId]: mapValues(
                    state[widgetId],
                    (button, buttonId) => resolve(state[widgetId][buttonId], action),
                ),
            }
        }
        default:
            return state
    }
}
