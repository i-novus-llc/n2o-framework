/**
 * @namespace ColumnsStore
 */

/**
 * Overlay store
 * @typedef {object} ColumnsStore.state
 */

/**
 * Payload для экшена INSERT_MODAL
 * @typedef ColumnsStore.registerColumnPayload
 * @property {string} key
 * @property {string} columnId
 * @property {string} label
 * @property {boolean} disabled
 * @property {boolean} visible
 * @property {object} conditions
 */

/**
 * Payload для экшена CHANGE_COLUMN_VISIBILITY
 * @typedef ColumnsStore.changeVisibilityPayload
 * @property {string} key
 * @property {string} columnId
 * @property {boolean} visible
 */

/**
 * Payload для экшена CHANGE_COLUMN_DISABLED
 * @typedef ColumnsStore.changeDisabledPayload
 * @property {string} key
 * @property {string} columnId
 * @property {boolean} disabled
 */

/**
 * Payload для экшена TOGGLE_COLUMN_VISIBILITY
 * @typedef ColumnsStore.toggleVisibilityPayload
 * @property {string} key
 * @property {string} columnId
 */

/**
 * Payload для экшена CHANGE_FROZEN_COLUMN
 * @typedef ColumnsStore.changeFrozenPayload
 * @property {string} key
 * @property {string} columnId
 */
