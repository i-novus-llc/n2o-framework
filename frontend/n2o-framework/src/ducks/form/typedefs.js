/**
 * @namespace FormPluginStore
 */

/**
 * FormPluginStore store
 * @typedef {{registeredFields: Object.<string, any>}} FormPluginStore.state
 */

/**
 * FormPluginStore item
 * @typedef {Object.<string, any>} FormPluginStore.item
 */

/**
 * Payload для экшена REGISTER_FIELD_EXTRA
 * @typedef FormPluginStore.registerFieldExtraPayload
 * @property {string} form
 * @property {string} name
 * @property {FormPluginStore.item} initialState
 */

/**
 * Payload для экшена DISABLE_FIELD
 * @typedef FormPluginStore.disableFieldPayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена ENABLE_FIELD
 * @typedef FormPluginStore.enableFieldPayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена SHOW_FIELD
 * @typedef FormPluginStore.showFieldPayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена HIDE_FIELD
 * @typedef FormPluginStore.hideFieldPayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена ADD_FIELD_MESSAGE
 * @typedef FormPluginStore.addFieldMessagePayload
 * @property {string} form
 * @property {string} name
 * @property {Object.<string, any>} message
 */

/**
 * Payload для экшена REMOVE_FIELD_MESSAGE
 * @typedef FormPluginStore.removeFieldMessagePayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена REGISTER_DEPENDENCY
 * @typedef FormPluginStore.registerFieldDependencyPayload
 * @property {string} form
 * @property {string} name
 * @property {object} dependency
 */

/**
 * Payload для экшена SET_FIELD_FILTER
 * @typedef FormPluginStore.setFilterValuePayload
 * @property {string} form
 * @property {string} name
 * @property {object} filter
 */

/**
 * Payload для экшена SET_REQUIRED
 * @typedef FormPluginStore.setRequiredPayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена UNSET_REQUIRED
 * @typedef FormPluginStore.unsetRequiredPayload
 * @property {string} form
 * @property {string} name
 */

/**
 * Payload для экшена SET_LOADING
 * @typedef FormPluginStore.setLoadingPayload
 * @property {string} form
 * @property {string} name
 * @property {boolean} loading
 */

/**
 * Payload для экшена SHOW_FIELDS
 * @typedef FormPluginStore.showMultiFieldsPayload
 * @property {string} form
 * @property {any[]} names
 */

/**
 * Payload для экшена HIDE_FIELDS
 * @typedef FormPluginStore.hideMultiFieldsPayload
 * @property {string} form
 * @property {any[]} names
 */

/**
 * Payload для экшена DISABLE_FIELDS
 * @typedef FormPluginStore.disableMultiFieldsPayload
 * @property {string} form
 * @property {any[]} names
 */

/**
 * Payload для экшена ENABLE_FIELDS
 * @typedef FormPluginStore.enableMultiFieldsPayload
 * @property {string} form
 * @property {any[]} names
 */
