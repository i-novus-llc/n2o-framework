import { createAction, createSlice } from '@reduxjs/toolkit'

import { RESET_STATE } from '../../constants/widgets'

import ButtonResolver from './ButtonResolver'
import { CALL_ACTION_IMPL } from './constants'

const initialState = {}

const toolbarSlice = createSlice({
    name: 'n2o/toolbar',
    initialState,
    reducers: {
        CHANGE_BUTTON_VISIBILITY: {
            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {boolean} visible
             * @return {{payload: ToolbarStore.changeVisibilityPayload}}
             */
            prepare(key, buttonId, visible) {
                return ({
                    payload: { key, buttonId, visible },
                })
            },

            /**
             * Смена видимости кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeVisibilityPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, visible } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].visible = visible
                }
            },
        },

        CHANGE_BUTTON_TITLE: {
            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {string} title
             * @return {{payload: ToolbarStore.changeTitlePayload}}
             */
            prepare(key, buttonId, title) {
                return ({
                    payload: { key, buttonId, title },
                })
            },

            /**
             * Изменение заголовка кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeTitlePayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, title } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].title = title
                }
            },
        },

        CHANGE_BUTTON_COUNT: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {number} count
             * @return {{payload: ToolbarStore.changeCountPayload}}
             */
            prepare(key, buttonId, count) {
                return ({
                    payload: { key, buttonId, count },
                })
            },

            /**
             * Изменение счетчика кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeCountPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, count } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].count = count
                }
            },
        },

        CHANGE_BUTTON_SIZE: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {string} size
             * @return {{payload: ToolbarStore.changeSizePayload}}
             */
            prepare(key, buttonId, size) {
                return ({
                    payload: { key, buttonId, size },
                })
            },

            /**
             * Изменение размера кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeSizePayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, size } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].size = size
                }
            },
        },

        CHANGE_BUTTON_COLOR: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {string} color
             * @return {{payload: ToolbarStore.changeColorPayload}}
             */
            prepare(key, buttonId, color) {
                return ({
                    payload: { key, buttonId, color },
                })
            },

            /**
             * Изменение цвета кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeColorPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, color } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].color = color
                }
            },
        },

        CHANGE_BUTTON_HINT: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {string} hint
             * @return {{payload: ToolbarStore.changeHintPayload}}
             */
            prepare(key, buttonId, hint) {
                return ({
                    payload: { key, buttonId, hint },
                })
            },

            /**
             * Изменение подсказки кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeHintPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, hint } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].hint = hint
                }
            },
        },

        CHANGE_BUTTON_MESSAGE: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {any} message
             * @return {{payload: ToolbarStore.changeMessagePayload}}
             */
            prepare(key, buttonId, message) {
                return ({
                    payload: { key, buttonId, message },
                })
            },

            /**
             * Изменение message кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeMessagePayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, message } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].message = message
                }
            },
        },

        CHANGE_BUTTON_ICON: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {string} icon
             * @return {{payload: ToolbarStore.changeIconPayload}}
             */
            prepare(key, buttonId, icon) {
                return ({
                    payload: { key, buttonId, icon },
                })
            },

            /**
             * Изменение иконки кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeIconPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, icon } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].icon = icon
                }
            },
        },

        CHANGE_BUTTON_CLASS: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {string} className
             * @return {{payload: ToolbarStore.changeClassPayload}}
             */
            prepare(key, buttonId, className) {
                return ({
                    payload: { key, buttonId, className },
                })
            },

            /**
             * Изменение css-класса кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeClassPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, className } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].className = className
                }
            },
        },

        CHANGE_BUTTON_STYLE: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {CSSStyleDeclaration} style
             * @return {{payload: ToolbarStore.changeStylePayload}}
             */
            prepare(key, buttonId, style) {
                return ({
                    payload: { key, buttonId, style },
                })
            },

            /**
             * Изменение css-класса кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeStylePayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, style } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].style = style
                }
            },
        },

        CHANGE_BUTTON_DISABLED: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {boolean} disabled
             * @return {{payload: ToolbarStore.changeDisabledPayload}}
             */
            prepare(key, buttonId, disabled) {
                return ({
                    payload: { key, buttonId, disabled },
                })
            },

            /**
             * Изменение блокировки кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.changeDisabledPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId, disabled } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].disabled = disabled
                }
            },
        },

        TOGGLE_BUTTON_DISABLED: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @return {{payload: ToolbarStore.toggleDisabledPayload}}
             */
            prepare(key, buttonId) {
                return ({
                    payload: { key, buttonId },
                })
            },

            /**
             * Изменение переключение блокировки кнопки на противоположное состояние
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.toggleDisabledPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].disabled = !state[key][buttonId].disabled
                }
            },
        },

        TOGGLE_BUTTON_VISIBILITY: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @return {{payload: ToolbarStore.toggleVisibilityPayload}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, buttonId) {
                return ({
                    payload: { key, buttonId },
                })
            },

            /**
             * Изменение переключение видимости кнопки на противоположное состояние
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.toggleVisibilityPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].visible = !state[key][buttonId].visible
                }
            },
        },

        REGISTER_BUTTON: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @param {Object.<string, any>} buttonState
             * @return {{payload: ToolbarStore.registerButtonPayload}}
             */
            prepare(
                key,
                buttonId,
                {
                    count,
                    resolveEnabled,
                    visible,
                    disabled,
                    containerKey,
                    conditions,
                    hintPosition,
                },
            ) {
                return ({
                    payload: {
                        key,
                        buttonId,
                        count,
                        resolveEnabled,
                        visible,
                        disabled,
                        containerKey,
                        conditions,
                        hintPosition,
                    },
                })
            },

            /**
             * Регистрации кнопки
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.registerButtonPayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                state[key][buttonId] = { ...ButtonResolver.defaultState, ...action.payload }
            },
        },

        REMOVE_BUTTON: {

            /**
             * @param {string} key
             * @param {string} buttonId
             * @return {{payload: ToolbarStore.removePayload}}
             */
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, buttonId) {
                return ({
                    payload: { key, buttonId },
                })
            },

            /**
             * Удаление кнопки по buttonId
             * @param {ToolbarStore.state} state
             * @param {Object} action
             * @param {string} action.type
             * @param {ToolbarStore.removePayload} action.payload
             */
            reducer(state, action) {
                const { key, buttonId } = action.payload

                if (state[key]?.[buttonId]) {
                    delete state[key][buttonId]
                }
            },
        },

        /**
         * Удаление кнопок по key
         * @param {ToolbarStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {string} action.payload
         */
        REMOVE_BUTTONS(state, action) {
            if (state[action.payload]) {
                delete state[action.payload]
            }
        },
    },
    extraReducers: {

        /**
         * Удаление кнопки по buttonId
         * @param {ToolbarStore.state} state
         * @param {Object} action
         * @param {string} action.type
         * @param {ToolbarStore.resetStatePayload} action.payload
         */
        [RESET_STATE](state, action) {
            const { widgetId } = action.payload

            if (state[widgetId]) {
                Object.keys(state[widgetId]).forEach((key) => {
                    state[widgetId][key].isInit = false
                })
            }
        },
    },
})

export default toolbarSlice.reducer

// Actions
/**
 * Вызов функции эшкен импла
 * @param {string} actionSrc
 * @param {object} options
 */
export const callActionImpl = createAction(CALL_ACTION_IMPL, (actionSrc, options) => ({
    payload: { actionSrc, options },
}))

export const {
    CHANGE_BUTTON_CLASS: changeButtonClass,
    CHANGE_BUTTON_COLOR: changeButtonColor,
    CHANGE_BUTTON_DISABLED: changeButtonDisabled,
    CHANGE_BUTTON_COUNT: changeButtonCount,
    CHANGE_BUTTON_HINT: changeButtonHint,
    CHANGE_BUTTON_ICON: changeButtonIcon,
    CHANGE_BUTTON_MESSAGE: changeButtonMessage,
    CHANGE_BUTTON_SIZE: changeButtonSize,
    CHANGE_BUTTON_STYLE: changeButtonStyle,
    CHANGE_BUTTON_TITLE: changeButtonTitle,
    CHANGE_BUTTON_VISIBILITY: changeButtonVisibility,
    REGISTER_BUTTON: registerButton,
    REMOVE_BUTTON: removeButton,
    REMOVE_BUTTONS: removeAllButtons,
    TOGGLE_BUTTON_DISABLED: toggleButtonDisabled,
    TOGGLE_BUTTON_VISIBILITY: toggleButtonVisibility,
} = toolbarSlice.actions
