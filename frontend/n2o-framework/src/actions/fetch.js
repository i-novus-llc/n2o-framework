import {
    FETCH_START,
    FETCH_END,
    FETCH_ERROR,
    FETCH_CANCEL,
    FETCH_ERROR_CONTINUE,
} from '../constants/fetch'

import createActionHelper from './createActionHelper'

export function fetchStart(fetchType, options) {
    return createActionHelper(FETCH_START)({ fetchType, options })
}

export function fetchEnd(fetchType, options, response) {
    return createActionHelper(FETCH_END)({ fetchType, options, response })
}

export function fetchError(fetchType, options, error) {
    return createActionHelper(FETCH_ERROR)({ fetchType, options, error })
}

export function fetchCancel(fetchType, options) {
    return createActionHelper(FETCH_CANCEL)({ fetchType, options })
}

export function fetchErrorContinue() {
    return createActionHelper(FETCH_ERROR_CONTINUE)()
}
