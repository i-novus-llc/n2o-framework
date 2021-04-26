import mapValues from 'lodash/mapValues'

import {
    CHANGE_COLUMN_DISABLED,
    CHANGE_COLUMN_VISIBILITY,
    REGISTER_COLUMN,
    TOGGLE_COLUMN_VISIBILITY,
    CHANGE_FROZEN_COLUMN,
} from '../constants/columns'
import { RESET_STATE } from '../constants/widgets'

export const columnState = {
    isInit: true,
    visible: true,
    disabled: false,
    frozen: false,
}

function resolve(state = columnState, action) {
    switch (action.type) {
        case CHANGE_COLUMN_VISIBILITY:
            return { ...state, visible: action.payload.visible }
        case CHANGE_COLUMN_DISABLED:
            return { ...state, disabled: action.payload.disabled }
        case TOGGLE_COLUMN_VISIBILITY:
            return { ...state, visible: !state.visible }
        case RESET_STATE:
            return { ...state, isInit: false }
        case CHANGE_FROZEN_COLUMN:
            return { ...state, frozen: !state.frozen }
        default:
            return state
    }
}

/**
 * Редюсер колонок
 * @ignore
 */
export default function columns(state = {}, action) {
    const { key, columnId, ...rest } = action.payload || {}
    switch (action.type) {
        case REGISTER_COLUMN:
            return { ...state,
                [key]: {
                    ...state[key],
                    [columnId]: { ...columnState, ...action.payload },
                } }
        case CHANGE_COLUMN_VISIBILITY:
        case CHANGE_COLUMN_DISABLED:
        case TOGGLE_COLUMN_VISIBILITY:
            return { ...state,
                [key]: {
                    ...state[key],
                    [columnId]: resolve(state[key][columnId], action),
                } }
        case RESET_STATE: {
            const { widgetId } = action.payload
            return {
                ...state,
                [widgetId]: mapValues(state[widgetId], (column, columnId) => resolve(state[widgetId][columnId], action)),
            }
        }
        case CHANGE_FROZEN_COLUMN: {
            return { ...state,
                [key]: {
                    ...state[key],
                    [columnId]: resolve(state[key][columnId], action),
                } }
        }
        default:
            return state
    }
}
