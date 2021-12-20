import PropTypes from 'prop-types'

/**
 * Методы для взаимодействия с datasource
 * @typedef {Object} WithDataSourceTypes
 * @property {boolean} visible
 * @property {string} id componentId
 * @property {string} datasource datasourceId
 * @property {function} dispatch
 * @property {function} addComponent
 * @property {function} removeComponent
 * @property {function} switchRegistration
 * @property {function} setFilter
 * @property {function} setResolve
 * @property {function} setSorting
 * @property {function} setPage
 * @property {function} fetchData
 */
export const WithDataSourceTypes = {
    visible: PropTypes.bool,
    id: PropTypes.string,
    datasource: PropTypes.string,
    dispatch: PropTypes.func,
    addComponent: PropTypes.func,
    removeComponent: PropTypes.func,
    switchRegistration: PropTypes.func,
    setFilter: PropTypes.func,
    setResolve: PropTypes.func,
    setSelected: PropTypes.func,
    setSorting: PropTypes.func,
    setPage: PropTypes.func,
    setSize: PropTypes.func,
    fetchData: PropTypes.func,
}
