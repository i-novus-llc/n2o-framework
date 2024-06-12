import React from 'react'
import { createAction, createSlice } from '@reduxjs/toolkit'
import { isEmpty } from 'lodash'

import { RESET_STATE } from '../widgets/constants'

import ButtonResolver from './ButtonResolver'
import { CALL_ACTION_IMPL } from './constants'
import { State } from './Toolbar'
import {
    ChangeButtonClass, ChangeButtonColor, ChangeButtonCount, ChangeButtonDisabled,
    ChangeButtonHint, ChangeButtonIcon, ChangeButtonMessage, ChangeButtonSize,
    ChangeButtonStyle, ChangeButtonTitle, ChangeButtonVisibility, RegisterButton, ToggleButtonParam,
} from './Actions'

export const initialState: State = {}

export const toolbarSlice = createSlice({
    name: 'n2o/toolbar',
    initialState,
    reducers: {
        CHANGE_BUTTON_VISIBILITY: {
            prepare(key, buttonId, visible) {
                return ({
                    payload: { key, buttonId, visible },
                })
            },

            reducer(state, action: ChangeButtonVisibility) {
                const { key, buttonId, visible } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].visible = visible
                }
            },
        },

        CHANGE_BUTTON_TITLE: {
            prepare(key, buttonId, title) {
                return ({
                    payload: { key, buttonId, title },
                })
            },

            reducer(state, action: ChangeButtonTitle) {
                const { key, buttonId, title } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].title = title
                }
            },
        },

        CHANGE_BUTTON_COUNT: {
            prepare(key, buttonId, count) {
                return ({
                    payload: { key, buttonId, count },
                })
            },

            reducer(state, action: ChangeButtonCount) {
                const { key, buttonId, count } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].count = count
                }
            },
        },

        CHANGE_BUTTON_SIZE: {
            prepare(key, buttonId, size) {
                return ({
                    payload: { key, buttonId, size },
                })
            },
            reducer(state, action: ChangeButtonSize) {
                const { key, buttonId, size } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].size = size
                }
            },
        },

        CHANGE_BUTTON_COLOR: {
            prepare(key, buttonId, color) {
                return ({
                    payload: { key, buttonId, color },
                })
            },

            reducer(state, action: ChangeButtonColor) {
                const { key, buttonId, color } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].color = color
                }
            },
        },

        CHANGE_BUTTON_HINT: {
            prepare(key, buttonId, hint) {
                return ({
                    payload: { key, buttonId, hint },
                })
            },

            reducer(state, action: ChangeButtonHint) {
                const { key, buttonId, hint } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].hint = hint
                }
            },
        },

        CHANGE_BUTTON_MESSAGE: {
            prepare(key, buttonId, message) {
                return ({
                    payload: { key, buttonId, message },
                })
            },

            reducer(state, action: ChangeButtonMessage) {
                const { key, buttonId, message } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].message = message
                }
            },
        },

        CHANGE_BUTTON_ICON: {
            prepare(key, buttonId, icon) {
                return ({
                    payload: { key, buttonId, icon },
                })
            },

            reducer(state, action: ChangeButtonIcon) {
                const { key, buttonId, icon } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].icon = icon
                }
            },
        },

        CHANGE_BUTTON_CLASS: {
            prepare(key, buttonId, className) {
                return ({
                    payload: { key, buttonId, className },
                })
            },

            reducer(state, action: ChangeButtonClass) {
                const { key, buttonId, className } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].className = className
                }
            },
        },

        CHANGE_BUTTON_STYLE: {
            prepare(key, buttonId, style: React.CSSProperties) {
                return ({
                    payload: { key, buttonId, style },
                })
            },

            reducer(state, action: ChangeButtonStyle) {
                const { key, buttonId, style } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].style = style
                }
            },
        },

        CHANGE_BUTTON_DISABLED: {
            prepare(key, buttonId, disabled) {
                return ({
                    payload: { key, buttonId, disabled },
                })
            },

            reducer(state, action: ChangeButtonDisabled) {
                const { key, buttonId, disabled } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].disabled = disabled
                }
            },
        },

        TOGGLE_BUTTON_DISABLED: {
            prepare(key, buttonId) {
                return ({
                    payload: { key, buttonId },
                })
            },

            reducer(state, action: ToggleButtonParam) {
                const { key, buttonId } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].disabled = !state[key][buttonId].disabled
                }
            },
        },

        TOGGLE_BUTTON_VISIBILITY: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, buttonId) {
                return ({
                    payload: { key, buttonId },
                })
            },

            reducer(state, action: ToggleButtonParam) {
                const { key, buttonId } = action.payload

                if (state[key]?.[buttonId]) {
                    state[key][buttonId].visible = !state[key][buttonId].visible
                }
            },
        },

        registerButton: {
            prepare(
                containerId,
                buttonId,
                initialState,
            ) {
                return ({
                    payload: {
                        key: containerId,
                        buttonId,
                        initialState,
                    },
                })
            },

            reducer(state, action: RegisterButton) {
                const { key, buttonId, initialState } = action.payload

                if (!state[key]) {
                    state[key] = {}
                }

                state[key][buttonId] = {
                    buttonId,
                    key,
                    ...ButtonResolver.defaultState,
                    ...initialState,
                }
            },
        },

        REMOVE_BUTTON: {
            // eslint-disable-next-line sonarjs/no-identical-functions
            prepare(key, buttonId) {
                return ({
                    payload: { key, buttonId },
                })
            },

            reducer(state, action: ToggleButtonParam) {
                const { key, buttonId } = action.payload

                if (state[key]?.[buttonId]) {
                    delete state[key][buttonId]
                }

                if (isEmpty(state[key])) {
                    delete state[key]
                }
            },
        },

        REMOVE_BUTTONS(state, action) {
            if (state[action.payload]) {
                delete state[action.payload]
            }
        },
    },
    extraReducers: {
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
    registerButton,
    REMOVE_BUTTON: removeButton,
    REMOVE_BUTTONS: removeAllButtons,
    TOGGLE_BUTTON_DISABLED: toggleButtonDisabled,
    TOGGLE_BUTTON_VISIBILITY: toggleButtonVisibility,
} = toolbarSlice.actions
