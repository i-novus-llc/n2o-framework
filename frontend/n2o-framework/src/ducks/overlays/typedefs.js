/**
 * @namespace OverlayStore
 */

/**
 * OverlayStore item
 * @typedef {Object.<string, any>} OverlayStore.item
 */

/**
 * OverlayStore store
 * @typedef {OverlayStore.item[]} OverlayStore.state
 */

/**
 * Payload для экшена INSERT_MODAL
 * @typedef OverlayStore.insertOverlayPayload
 * @property {string} name
 * @property {boolean} visible
 * @property {string} mode
 * @property {any} *
 */

/**
 * Payload для экшена INSERT_DRAWER
 * @typedef OverlayStore.insertDrawerPayload
 * @property {string} name
 * @property {boolean} visible
 * @property {string} mode
 * @property {any} *
 */

/**
 * Payload для экшена INSERT_DIALOG
 * @typedef OverlayStore.insertDialogPayload
 * @property {string} name
 * @property {boolean} visible
 * @property {any} *
 */
