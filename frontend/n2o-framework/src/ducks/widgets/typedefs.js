/**
 * @namespace WidgetsStore
 */

/**
 * Overlay store
 * @typedef {object} WidgetsStore.state
 */

/**
 * Payload для экшена REGISTER
 * @typedef WidgetsStore.registerWidgetPayload
 * @property {string} widgetId
 * @property {object} initProps
 * @property {boolean} preInit
 */

/**
 * Payload для экшена DATA_REQUEST
 * @typedef WidgetsStore.dataRequestWidgetPayload
 * @property {string} widgetId
 * @property {string} modelId
 * @property {object} options
 */

/**
 * Payload для экшена RESOLVE
 * @typedef WidgetsStore.resolveWidgetPayload
 * @property {string} widgetId
 * @property {string} modelId
 * @property {object} model
 */

/**
 * Payload для экшена CHANGE_FILTERS_VISIBILITY
 * @typedef WidgetsStore.changeFiltersVisibilityPayload
 * @property {string} widgetId
 * @property {boolean} isFilterVisible
 */

/**
 * Payload для экшена ALERT_ADD
 * @typedef WidgetsStore.alertAddWidgetPayload
 * @property {string} widgetId
 * @property {string} alertKey
 */

/**
 * Payload для экшена ALERT_REMOVE
 * @typedef WidgetsStore.alertRemoveWidgetPayload
 * @property {string} widgetId
 * @property {string} alertKey
 */
