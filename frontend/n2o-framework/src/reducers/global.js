import { handleActions } from 'redux-actions'

import {
    CHANGE_LOCALE,
    REQUEST_CONFIG,
    REQUEST_CONFIG_SUCCESS,
    REQUEST_CONFIG_FAIL,
    CHANGE_ROOT_PAGE,
    SET_READY,
    REGISTER_LOCALES,
} from '../constants/global'

const defaultState = {
    ready: false,
    loading: false,
    error: null,
    locale: 'ru',
    messages: {},
    menu: {},
    rootPageId: null,
}

export default handleActions(
    {
        [SET_READY]: (state, action) => ({
            ...state,
            ready: true,
        }),
        [CHANGE_LOCALE]: (state, action) => ({
            ...state,
            locale: action.payload.locale,
        }),
        [REQUEST_CONFIG]: state => ({
            ...state,
            loading: true,
        }),
        [REQUEST_CONFIG_SUCCESS]: (state, action) => ({
            ...state,
            loading: false,
            ...action.payload.config,
        }),
        [REQUEST_CONFIG_FAIL]: (state, action) => ({
            ...state,
            loading: false,
            error: action.payload.error,
        }),
        [CHANGE_ROOT_PAGE]: (state, action) => ({
            ...state,
            rootPageId: action.payload.rootPageId,
        }),
        [REGISTER_LOCALES]: (state, action) => ({
            ...state,
            locales: action.payload.locales,
        }),
    },
    defaultState,
)
