/**
 * @namespace PagesStore
 */

/**
 * Page item
 * @typedef {Object.<string, any>} PagesStore.item
 */

/**
 * PagesStore store
 * @typedef {Object.<string, PagesStore.item>} PagesStore.state
 */

/**
 * Payload для экшена METADATA_REQUEST
 * @typedef PagesStore.metaDataRequestPayload
 * @property {string} pageId
 * @property {boolean} rootPage
 * @property {string} pageUrl
 * @property {Object.<string, string>} mapping
 */

/**
 * Payload для экшена METADATA_SUCCESS
 * @typedef PagesStore.metaDataSuccessPayload
 * @property {string} pageId
 * @property {Object.<string, any>} json
 */

/**
 * Payload для экшена METADATA_FAIL
 * @typedef PagesStore.metaDataFailPayload
 * @property {Object.<string, any>} payload
 * @property {Object.<string, any>} meta
 */

/**
 * Payload для экшена SET_STATUS
 * @typedef PagesStore.setStatusPayload
 * @property {string} pageId
 * @property {number} status
 */
