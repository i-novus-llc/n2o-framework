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
 * Payload для экшена DATA_SUCCESS
 * @typedef WidgetsStore.dataSuccessWidgetPayload
 * @property {string} widgetId
 */

/**
 * Payload для экшена DATA_FAIL
 * @typedef WidgetsStore.dataFailWidgetPayload
 * @property {string} widgetId
 * @property {object} err
 */

/**
 * Payload для экшена RESOLVE
 * @typedef WidgetsStore.resolveWidgetPayload
 * @property {string} widgetId
 * @property {string} modelId
 * @property {object} model
 */

/**
 * Payload для экшена RESOLVE
 * @typedef WidgetsStore.sortByWidgetPayload
 * @property {string} widgetId
 * @property {string} fieldKey
 * @property {string} sortDirection
 */

/**
 * Payload для экшена CHANGE_PAGE
 * @typedef WidgetsStore.changePageWidgetPayload
 * @property {string} widgetId
 * @property {string} page
 */

/**
 * Payload для экшена CHANGE_COUNT
 * @typedef WidgetsStore.changeCountWidgetPayload
 * @property {string} widgetId
 * @property {number} count
 */

/**
 * Payload для экшена CHANGE_FILTERS_VISIBILITY
 * @typedef WidgetsStore.changeFiltersVisibilityPayload
 * @property {string} widgetId
 * @property {boolean} isFilterVisible
 */

/**
 * Payload для экшена SET_TABLE_SELECTED_ID
 * @typedef WidgetsStore.setTableSelectedIdPayload
 * @property {string} widgetId
 * @property {string} value
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
