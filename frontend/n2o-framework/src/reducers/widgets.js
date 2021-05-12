import omit from 'lodash/omit'
import mapValues from 'lodash/mapValues'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
import isNaN from 'lodash/isNaN'

import {
    REGISTER,
    DATA_REQUEST,
    DATA_SUCCESS,
    DATA_FAIL,
    RESOLVE,
    REMOVE,
    SHOW,
    HIDE,
    ENABLE,
    DISABLE,
    LOADING,
    UNLOADING,
    SORT_BY,
    CHANGE_COUNT,
    CHANGE_SIZE,
    CHANGE_PAGE,
    CHANGE_FILTERS_VISIBILITY,
    TOGGLE_FILTERS_VISIBILITY,
    RESET_STATE,
    SET_TABLE_SELECTED_ID,
    SET_ACTIVE,
    DISABLE_ON_FETCH,
} from '../constants/widgets'

/**
 * Описание объекта состояния виджета в хранилище
 * @type {Object}
 * @property {boolean} isInit - инициализирован ли виджета
 * @property {boolean} isEnabled - доступен ли виджет
 * @property {boolean} isVisible - виден ли виджет
 * @property {boolean} isLoading - состояние загрузки
 * @property {string} selectedId - id выбраной записи в виджете
 * @property {number} size - размер выборки
 * @property {number} page - страница выборки
 */
export const widgetState = {
    isInit: false,
    isEnabled: true,
    isVisible: true,
    isLoading: false,
    isResolved: false,
    selectedId: null,
    isFilterVisible: true,
    isActive: false,
    type: null,
    dataProvider: {},
    sorting: {},
    filter: {
        key: null,
        type: null,
    },
    /* Query props */
    count: 0,
    /* System props */
    pageId: null,
    containerId: null,
    validation: {},
    error: null,
}

// eslint-disable-next-line complexity
function resolve(state = widgetState, action) {
    switch (action.type) {
        case DATA_REQUEST:
            return { ...state, isLoading: true }
        case DATA_SUCCESS:
            return { ...state, isLoading: false }
        case DATA_FAIL:
            return { ...state,
                isLoading: false,
                error: get(action, 'payload.err', true) }
        case RESOLVE:
            return { ...state, isResolved: true }
        case SHOW:
            return { ...state, isVisible: true }
        case HIDE:
            return { ...state, isVisible: false }
        case ENABLE:
            return { ...state, isEnabled: true }
        case DISABLE:
        case DISABLE_ON_FETCH:
            return { ...state, isEnabled: false }
        case LOADING:
            return { ...state, isLoading: true }
        case UNLOADING:
            return { ...state, isLoading: false }
        case SORT_BY: {
            if (action.payload.sortDirection === 'NONE') {
                return {
                    ...state,
                    sorting: {},
                }
            }

            return {
                ...state,
                sorting: { [action.payload.fieldKey]: action.payload.sortDirection },
            }
        }
        case CHANGE_SIZE:
            return { ...state, size: action.payload.size }
        case CHANGE_PAGE:
            return { ...state, page: action.payload.page }
        case CHANGE_COUNT:
            return { ...state, count: action.payload.count }
        case CHANGE_FILTERS_VISIBILITY:
            return { ...state, isFilterVisible: action.payload.isFilterVisible }
        case TOGGLE_FILTERS_VISIBILITY:
            return { ...state, isFilterVisible: !state.isFilterVisible }
        case RESET_STATE:
            return { ...state, isInit: false }
        case SET_TABLE_SELECTED_ID:
            return { ...state, selectedId: resolveSelectedId(action.payload.value) }
        case SET_ACTIVE:
            return { ...state, isActive: true }
        default:
            return state
    }
}

function resolveSelectedId(selectedId) {
    if (selectedId !== '' && !isNaN(+selectedId)) {
        selectedId = +selectedId
    }

    return selectedId
}

/**
 * Редюсер виджета
 * @ignore
 */
// eslint-disable-next-line complexity
export default function widgets(state = {}, action) {
    switch (action.type) {
        case REGISTER: {
            let smartState = {}
            const currentState = state[action.payload.widgetId] || {}

            if (!isEmpty(currentState)) {
                smartState = {
                    selectedId: currentState.selectedId ? currentState.selectedId : null,
                }
                if (!isEmpty(currentState.sorting)) {
                    smartState.sorting = currentState.sorting
                }
            }

            return {
                ...state,
                [action.payload.widgetId]: {

                    ...widgetState,
                    ...currentState,
                    ...action.payload.initProps,
                    ...smartState,
                    isInit: !action.payload.preInit,
                    type: action.payload.initProps.type,
                },
            }
        }
        case DATA_REQUEST:
        case DATA_SUCCESS:
        case DATA_FAIL:
        case RESOLVE:
        case DISABLE:
        case DISABLE_ON_FETCH:
        case ENABLE:
        case SHOW:
        case HIDE:
        case LOADING:
        case UNLOADING:
        case SORT_BY:
        case CHANGE_SIZE:
        case CHANGE_COUNT:
        case CHANGE_PAGE:
        case CHANGE_FILTERS_VISIBILITY:
        case TOGGLE_FILTERS_VISIBILITY:
        case SET_TABLE_SELECTED_ID:
        case RESET_STATE:
            return { ...state,
                [action.payload.widgetId]: resolve(
                    state[action.payload.widgetId],
                    action,
                ) }
        case SET_ACTIVE:
            return {
                ...mapValues(state, value => ({ ...value, isActive: false })),
                [action.payload.widgetId]: resolve(
                    state[action.payload.widgetId],
                    action,
                ),
            }
        case REMOVE:
            return omit(state, action.payload.widgetId)
        default:
            return state
    }
}
