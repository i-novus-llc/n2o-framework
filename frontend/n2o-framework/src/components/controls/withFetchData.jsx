import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import get from 'lodash/get'
import isArray from 'lodash/isArray'
import has from 'lodash/has'
import unionBy from 'lodash/unionBy'

import cachingStore from '../../utils/cacher'
import { fetchInputSelectData } from '../../core/api'
import { addAlert, removeAllAlerts } from '../../ducks/alerts/store'
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

            this.fetchData = this.fetchData.bind(this)
            this.findResponseInCache = this.findResponseInCache.bind(this)
            this.fetchDataProvider = this.fetchDataProvider.bind(this)
            this.addAlertMessage = this.addAlertMessage.bind(this)
            this.setErrorMessage = this.setErrorMessage.bind(this)
            this.setResponseToData = this.setResponseToData.bind(this)
        }

        static getDerivedStateFromProps(nextProps) {
            const { data } = nextProps

            if (data && data.length) {
                return {
                    data,
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
        findResponseInCache(params) {
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
        addAlertMessage(messages) {
            const { hasError } = this.state
            const { addAlert, removeAlerts } = this.props

            if (!hasError) {
                this.setState({ hasError: true })
            }

            removeAlerts()

            if (isArray(messages)) {
                messages.map(m => addAlert({ ...m, closeButton: false }))
            } else {
                addAlert({ ...messages, closeButton: false })
            }
        }

        /**
         * Вывод сообщения с ошибкой
         * @param response
         * @param body
         * @private
         */
        async setErrorMessage({ response, body }) {
            let errorMessage = null

            if (response) {
                errorMessage = await response.json()
            } else {
                errorMessage = body
            }
            const messages = get(errorMessage, 'meta.alert.messages', false)

            if (messages) {
                this.addAlertMessage(messages)
            }
        }

        /**
     * Взять данные с сервера с помощью dataProvider
     * @param dataProvider
     * @param extraParams
     * @returns {Promise<void>}
     * @private
     */
        async fetchDataProvider(dataProvider, extraParams = {}) {
            const { store } = this.context
            const {
                basePath,
                baseQuery: queryParams,
                headersParams,
            } = dataProviderResolver(store.getState(), dataProvider)

            let response = this.findResponseInCache({
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
        setResponseToData({ list, count, size, page }, merge = false) {
            const { valueFieldId } = this.props
            const { data } = this.state

            this.setState({
                data: merge
                    ? unionBy(data, list, valueFieldId || 'id')
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

        async fetchData(extraParams = {}, merge = false) {
            const { dataProvider, removeAlerts } = this.props
            const { hasError, data } = this.state

            if (!dataProvider) { return }
            this.setState({ loading: true })
            try {
                if (!merge && !data) { this.setState({ data: [] }) }
                const response = await this.fetchDataProvider(
                    dataProvider,
                    extraParams,
                )

                if (has(response, 'message')) { this.addAlertMessage(response.message) }

                this.setResponseToData(response, merge)

                if (hasError) {
                    removeAlerts()
                }
            } catch (err) {
                await this.setErrorMessage(err)
            } finally {
                this.setState({ loading: false })
            }
        }

        /**
     * Рендер
     */

        render() {
            const { setRef } = this.props

            return (
                <WrappedComponent
                    {...this.props}
                    {...this.state}
                    _fetchData={this.fetchData}
                    ref={setRef}
                />
            )
        }
    }

    WithFetchData.propTypes = {
        caching: PropTypes.bool,
        size: PropTypes.number,
        data: PropTypes.array,
        addAlert: PropTypes.func,
        removeAlerts: PropTypes.func,
        valueFieldId: PropTypes.string,
        dataProvider: PropTypes.object,
        setRef: PropTypes.oneOfType([
            PropTypes.func,
            PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
        ]),
    }

    WithFetchData.contextTypes = { store: PropTypes.object }

    WithFetchData.defaultProps = {
        caching: false,
        size: 10,
    }

    const mapDispatchToProps = (dispatch, ownProps) => ({
        addAlert: message => dispatch(addAlert(`${ownProps.form}.${ownProps.labelFieldId}`, message)),
        removeAlerts: () => dispatch(removeAllAlerts(`${ownProps.form}.${ownProps.labelFieldId}`)),
    })

    return connect(
        null,
        mapDispatchToProps,
    )(WithFetchData)
}

export default withFetchData
