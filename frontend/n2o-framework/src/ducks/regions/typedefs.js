/**
 * @namespace RegionsStore
 */

/**
 * RegionsStore item
 * @typedef {Object.<string, any>} RegionsStore.item
 */

/**
 * RegionsStore store
 * @typedef {Object.<string, RegionsStore.item[]>} RegionsStore.state
 */

/**
 * Payload для REGISTER_REGION
 * @typedef RegionsStore.registerRegionPayload
 * @property {string} regionId
 * @property {RegionsStore.item} regionState
 */

/**
 * Payload для SET_ACTIVE_REGION_ENTITY
 * @typedef RegionsStore.setActiveRegionEntityPayload
 * @property {string} regionId
 * @property {string} activeEntity
 */
