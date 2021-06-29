/**
 * @namespace ToolbarStore
 */

/**
 * @typedef {Object.<string, any>} ToolbarStore.state
 */

/**
 * Пейлод для CHANGE_BUTTON_VISIBILITY
 * @typedef ToolbarStore.changeVisibilityPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {boolean} visible
 */

/**
 * Пейлод для CHANGE_BUTTON_TITLE
 * @typedef ToolbarStore.changeTitlePayload
 * @property {string} key
 * @property {string} buttonId
 * @property {string} title
 */

/**
 * Пейлод для CHANGE_BUTTON_COUNT
 * @typedef ToolbarStore.changeCountPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {number} count
 */

/**
 * Пейлод для CHANGE_BUTTON_SIZE
 * @typedef ToolbarStore.changeSizePayload
 * @property {string} key
 * @property {string} buttonId
 * @property {string} size
 */

/**
 * Пейлод для CHANGE_BUTTON_COLOR
 * @typedef ToolbarStore.changeColorPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {string} color
 */

/**
 * Пейлод для CHANGE_BUTTON_HINT
 * @typedef ToolbarStore.changeHintPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {string} hint
 */

/**
 * Пейлод для CHANGE_BUTTON_MESSAGE
 * @typedef ToolbarStore.changeMessagePayload
 * @property {string} key
 * @property {string} buttonId
 * @property {any} message
 */

/**
 * Пейлод для CHANGE_BUTTON_ICON
 * @typedef ToolbarStore.changeIconPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {string} icon
 */

/**
 * Пейлод для CHANGE_BUTTON_CLASS
 * @typedef ToolbarStore.changeClassPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {string} className
 */

/**
 * Пейлод для CHANGE_BUTTON_STYLE
 * @typedef ToolbarStore.changeStylePayload
 * @property {string} key
 * @property {string} buttonId
 * @property {CSSStyleDeclaration} style
 */

/**
 * Пейлод для CHANGE_BUTTON_DISABLED
 * @typedef ToolbarStore.changeDisabledPayload
 * @property {string} key
 * @property {string} buttonId
 * @property {boolean} disabled
 */

/**
 * Пейлод для TOGGLE_BUTTON_DISABLED
 * @typedef ToolbarStore.toggleDisabledPayload
 * @property {string} key
 * @property {string} buttonId
 */

/**
 * Пейлод для TOGGLE_BUTTON_VISIBILITY
 * @typedef ToolbarStore.toggleVisibilityPayload
 * @property {string} key
 * @property {string} buttonId
 */

/**
 * Пейлод для REGISTER_BUTTON
 * @typedef {Object.<string, any>} ToolbarStore.registerButtonPayload
 */

/**
 * Пейлод для REMOVE_BUTTON
 * @typedef ToolbarStore.removePayload
 * @property {string} key
 * @property {string} buttonId
 */

/**
 * Пейлод для RESET_STATE
 * @typedef ToolbarStore.resetStatePayload
 * @property {string} widgetId
 * @property {string} buttonId
 * @property {string} buttonKey
 */
