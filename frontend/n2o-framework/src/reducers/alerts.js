import omit from 'lodash/omit'

import { ADD, ADD_MULTI, REMOVE, REMOVE_ALL } from '../constants/alerts'
import { id as generateId } from '../utils/id'

export const defaultState = {
    id: null,
    severity: 'danger',
    label: null,
    text: null,
    details: null,
    icon: null,
    timeout: null,
    closeButton: null,
}

function resolve(state = defaultState, { type, payload }) {
    switch (type) {
        case ADD:
            return [
                {
                    id: payload.id || generateId(),
                    severity: payload.severity,
                    label: payload.label,
                    text: payload.text,
                    details: payload.details,
                    closeButton: payload.closeButton,
                },
            ]
        case ADD_MULTI:
            return [
                ...payload.alerts.map(item => ({
                    ...item,
                })),
            ]
        default:
            return state
    }
}

/**
 * Редюсер удаления/добваления алертов
 * @ignore
 */
export default function alerts(state = {}, action) {
    switch (action.type) {
        case ADD:
        case ADD_MULTI:
            return { ...state,
                [action.payload.key]: state[action.payload.key]
                    ? state[action.payload.key].concat(
                        resolve(state[action.payload.key], action),
                    )
                    : resolve(state[action.payload.key], action) }
        case REMOVE:
            return { ...state,
                [action.payload.key]: state[action.payload.key].filter(
                    alert => alert.id !== action.payload.id,
                ) }
        case REMOVE_ALL:
            return omit(state, action.payload.key)
        default:
            return state
    }
}
