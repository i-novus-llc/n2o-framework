import React from 'react'
import PropTypes from 'prop-types'
import pathToRegexp from 'path-to-regexp'
import { connect } from 'react-redux'
import get from 'lodash/get'
import isArray from 'lodash/isArray'
import has from 'lodash/has'
import unionBy from 'lodash/unionBy'

import cachingStore from '../../utils/cacher'
import { fetchInputSelectData } from '../../core/api'
import { addAlert, removeAlerts } from '../../actions/alerts'
import { dataProviderResolver } from '../../core/dataProviderResolver'

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 * @param apiCaller - promise для вызова апи
 */

function withFetchData(WrappedComponent, apiCaller = fetchInputSelectData) {
    class WithFetchData extends React.Component {
        constructor(props) {
            super(props)

            this.state = {
                data: [],
                isLoading: false,
                count: 0,
                size: props.size,
                page: 1,
                hasError: false,
            }

            this._fetchData = this._fetchData.bind(this)
            this._findResponseInCache = this._findResponseInCache.bind(this)
            this._fetchDataProvider = this._fetchDataProvider.bind(this)
            this._addAlertMessage = this._addAlertMessage.bind(this)
            this._setErrorMessage = this._setErrorMessage.bind(this)
            this._setResponseToData = this._setResponseToData.bind(this)
        }

        static getDerivedStateFromProps(nextProps) {
            if (nextProps.data && nextProps.data.length) {
                return {
                    data: nextProps.data,
                }
            }

            return null
        }

        /**
     * Поиск в кеше запроса
     * @param params
     * @returns {*}
     * @private
     */
        _findResponseInCache(params) {
            const { caching } = this.props

            if (caching && cachingStore.find(params)) {
                return cachingStore.find(params)
            }

            return false
        }

        /**
     * Вывод сообщения
     * @param messages
     * @private
     */
        _addAlertMessage(messages) {
            const { hasError } = this.state
            const { addAlert, removeAlerts } = this.props

            !hasError && this.setState({ hasError: true })

            removeAlerts()
            isArray(messages)
                ? messages.map(m => addAlert({ ...m, closeButton: false }))
                : addAlert({ ...messages, closeButton: false })
        }

        /**
     * Вывод сообщения с ошибкой
     * @param response
     * @private
     */
        async _setErrorMessage({ response }) {
            let errorMessage = null

            if (response) {
                errorMessage = await response.json()
            } else {
                errorMessage = arguments[0].body
            }
            const messages = get(errorMessage, 'meta.alert.messages', false)

            messages && this._addAlertMessage(messages)
        }

        /**
     * Взять данные с сервера с помощью dataProvider
     * @param dataProvider
     * @param extraParams
     * @returns {Promise<void>}
     * @private
     */
        async _fetchDataProvider(dataProvider, extraParams = {}) {
            const {
                basePath,
                baseQuery: queryParams,
                headersParams,
            } = dataProviderResolver(this.context.store.getState(), dataProvider)

            let response = this._findResponseInCache({
                basePath,
                queryParams,
                extraParams,
            })

            if (!response) {
                response = await apiCaller(
                    { headers: headersParams, query: { ...queryParams, ...extraParams } },
                    null,
                    {
                        basePath,
                    },
                )

                cachingStore.add({ basePath, queryParams, extraParams }, response)
            }

            return response
        }

        /**
     *  Обновить данные если запрос успешен
     * @param list
     * @param count
     * @param size
     * @param page
     * @param merge
     * @private
     */
        _setResponseToData({ list, count, size, page }, merge = false) {
            const { valueFieldId } = this.props

            this.setState({
                data: merge
                    ? unionBy(this.state.data, list, valueFieldId || 'id')
                    : list,
                isLoading: false,
                count,
                size,
                page,
            })
        }

        /**
     * Получает данные с сервера
     * @param extraParams - параметры запроса
     * @param concat - флаг объединения данных
     * @returns {Promise<void>}
     * @private
     */

        async _fetchData(extraParams = {}, merge = false) {
            const { dataProvider, removeAlerts } = this.props
            const { hasError, data } = this.state

            if (!dataProvider) { return }
            this.setState({ loading: true })
            try {
                if (!merge && !data) { this.setState({ data: [] }) }
                const response = await this._fetchDataProvider(
                    dataProvider,
                    extraParams,
                )

                if (has(response, 'message')) { this._addAlertMessage(response.message) }

                this._setResponseToData(response, merge)
                hasError && removeAlerts()
            } catch (err) {
                await this._setErrorMessage(err)
            } finally {
                this.setState({ loading: false })
            }
        }

        /**
     * Рендер
     */

        render() {
            return (
                <WrappedComponent
                    {...this.props}
                    {...this.state}
                    _fetchData={this._fetchData}
                    ref={this.props.setRef}
                />
            )
        }
    }

    WithFetchData.propTypes = {
        caching: PropTypes.bool,
        size: PropTypes.number,
    }

    WithFetchData.contextTypes = { store: PropTypes.object }

    WithFetchData.defaultProps = {
        caching: false,
        size: 10,
    }

    const mapDispatchToProps = (dispatch, ownProps) => ({
        addAlert: message => dispatch(addAlert(`${ownProps.form}.${ownProps.labelFieldId}`, message)),
        removeAlerts: () => dispatch(removeAlerts(`${ownProps.form}.${ownProps.labelFieldId}`)),
    })

    return connect(
        null,
        mapDispatchToProps,
    )(WithFetchData)
}

export default withFetchData
