/**
 * @namespace ModelsStore
 */

/**
 * ModelsStore item
 * @typedef {Object.<string, any>} ModelsStore.item
 */

/**
 * ModelsStore store
 * @typedef {Object.<string, ModelsStore.item[]> | {}} ModelsStore.state
 */

/**
 * Payload для SET
 * @typedef ModelsStore.setModelPayload
 * @property {string} prefix
 * @property {string} key
 * @property {object} model
 */

/**
 * Payload для REMOVE
 * @typedef ModelsStore.removeModelPayload
 * @property {string} prefix
 * @property {string | number} key
 */

/**
 * Payload для SYNC
 * @typedef ModelsStore.syncModelPayload
 * @property {string} prefix
 * @property {string[]} keys
 * @property {object} model
 */

/**
 * Payload для UPDATE
 * @typedef ModelsStore.updateModelPayload
 * @property {string} prefix
 * @property {string} key
 * @property {string} field
 * @property {any} value
 */

/**
 * Payload для UPDATE_MAP
 * @typedef ModelsStore.updateMapModelPayload
 * @property {string} prefix
 * @property {string} key
 * @property {string} field
 * @property {any} value
 * @property {string} map
 */

/**
 * Payload для CLEAR
 * @typedef ModelsStore.clearModelPayload
 * @property {string[]} prefixes
 * @property {string} key
 * @property {string[]} exclude
 */

/**
 * Payload для MERGE
 * @typedef ModelsStore.combineModelsPayload
 * @property {any} combine
 */

/**
 * Payload для REMOVE_ALL
 * @typedef ModelsStore.removeAllModelPayload
 * @property {string} key
 */
