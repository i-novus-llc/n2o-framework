/**
 * @namespace GlobalStore
 */

/**
 * GlobalStore store
 * @typedef {Object.<string, any>} GlobalStore.state
 * @property {boolean} loading
 * @property {string} locale
 * @property {boolean} ready
 * @property {any | null} error
 * @property {Object.<string, any>} messages
 * @property {Object.<string, any>} menu
 * @property {Object.<string, any>} user
 * @property {string | null} rootPageId
 * @property {string[]} locales
 */

/**
 * Payload для REQUEST_CONFIG_FAIL
 * @typedef GlobalStore.requestConfigFail
 * @property {Object.<string, any>} payload
 * @property {Object} meta
 * @property {Object.<string, any>} meta.alerts
 */
