import {
    FETCH_START,
    FETCH_END,
    FETCH_ERROR,
    FETCH_CANCEL,
    FETCH_ERROR_CONTINUE,
} from '../constants/fetch'

// @ts-ignore ignore import error from js file
import createActionHelper from './createActionHelper'

export function fetchStart(fetchType: string, options: Record<string, unknown>) {
    return createActionHelper(FETCH_START)({ fetchType, options })
}

export function fetchEnd(fetchType: string, options: Record<string, unknown>, response: Record<string, unknown>) {
    return createActionHelper(FETCH_END)({ fetchType, options, response })
}

export function fetchError(fetchType: string, options: Record<string, unknown>, error: unknown) {
    return createActionHelper(FETCH_ERROR)({ fetchType, options, error })
}

export function fetchCancel(fetchType: string, options: Record<string, unknown>) {
    return createActionHelper(FETCH_CANCEL)({ fetchType, options })
}

export function fetchErrorContinue() {
    return createActionHelper(FETCH_ERROR_CONTINUE)()
}
