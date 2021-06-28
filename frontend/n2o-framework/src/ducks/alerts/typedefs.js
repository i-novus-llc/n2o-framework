/**
 * @namespace AlertsStore
 */

/**
 * Alert item
 * @typedef {Object} AlertsStore.item
 * @property {string | null} field
 * @property { 'info' | 'danger' | 'warning' | 'success'} severity
 * @property {string} text
 * @property {Object.<string, number> | null} timeout
 * @property {boolean | null} closeButton
 * @property {string} position
 * @property {boolean} loader
 * @property {boolean} animate
 * @property {any} choice
 * @property {string[] | null} stacktrace
 * @property {string} label
 * @property {string} id
 * @property {object} [data]
 */

/**
 * Alert store
 * @typedef {Object.<string, AlertsStore.item[]>} AlertsStore.state
 */

/**
 * Payload для ADD
 * @typedef AlertsStore.addPayload
 * @property {string} key
 * @property {AlertsStore.item} alert
 */

/**
 * Payload для ADD_MULTI
 * @typedef {Object} AlertsStore.addMultiPayload
 * @property {string} key
 * @property {AlertsStore.item[]} AlertsStore
 */

/**
 * Payload для REMOVE
 * @typedef AlertsStore.removePayload
 * @property {string} id
 * @property {string} key
 */

/**
 * Payload для REMOVE_ALL
 * @typedef {string} AlertsStore.removeAllPayload
 */
