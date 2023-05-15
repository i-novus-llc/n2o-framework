import React from 'react'
import PropTypes from 'prop-types'
import { compose, withProps } from 'recompose'
import { connect, ReactReduxContext } from 'react-redux'
import get from 'lodash/get'
import isArray from 'lodash/isArray'
import has from 'lodash/has'
import unionBy from 'lodash/unionBy'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

import cachingStore from '../../utils/cacher'
import { fetchInputSelectData, FETCH_CONTROL_VALUE } from '../../core/api'
import { addAlert, removeAllAlerts } from '../../ducks/alerts/store'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { fetchError } from '../../actions/fetch'
import { WithDataSource } from '../../core/widget/WithDataSource'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { ModelPrefix } from '../../core/datasource/const'

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 * @param apiCaller - promise для вызова апи
 *
 * TODO разобраться почему нормально не вешается ref, необходимый для ReduxField, если оборачиваемый компонент функциональный, а не классовый
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
            }

            this.fetchData = this.fetchData.bind(this)
            this.findResponseInCache = this.findResponseInCache.bind(this)
            this.fetchDataProvider = this.fetchDataProvider.bind(this)
            this.addAlertMessage = this.addAlertMessage.bind(this)
            this.setErrorMessage = this.setErrorMessage.bind(this)
            this.setResponseToData = this.setResponseToData.bind(this)
        }

        componentDidMount() {
            const { data } = this.state
            const { datasourceModel } = this.props

            if (datasourceModel) {
                const { datasourceModel } = this.props

                if (isEmpty(data) && !isEmpty(datasourceModel)) {
                    this.setState({ data: datasourceModel })
                }
            }
        }

        componentDidUpdate({ datasourceModel: prevDatasourceModel }) {
            const { datasourceModel } = this.props

            if (datasourceModel) {
                const isNotEqual = !isEqual(datasourceModel, prevDatasourceModel)

                if (isNotEqual) {
                    this.setState({ data: datasourceModel })
                }
            }
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
            const { addAlert } = this.props

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
            let errorMessage

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
            const { abortController } = this.state

            if (abortController) {
                abortController.abort()
            }

            const {
                basePath,
                baseQuery: queryParams,
                headersParams,
            } = dataProviderResolver(store.getState(), dataProvider)

            const cached = this.findResponseInCache({
                basePath,
                queryParams,
                extraParams,
            })

            if (cached) {
                return cached
            }

            const controller = new AbortController()

            this.setState({ abortController: controller })

            return apiCaller(
                { headers: headersParams, query: { ...queryParams, ...extraParams } },
                {
                    basePath,
                },
                controller.signal,
            ).then((response) => {
                cachingStore.add({ basePath, queryParams, extraParams }, response)
                this.setState({ abortController: null })

                return response
            })
        }

        /**
         *  Обновить данные если запрос успешен
         */
        setResponseToData({ list, count, size, page, paging }, merge = false) {
            const { valueFieldId } = this.props
            const { data } = this.state
            const { count: pagingCount, size: pagingSize, page: pagingPage } = paging || {}

            this.setState({
                data: merge
                    ? unionBy(data, list, valueFieldId || 'id')
                    : list,
                isLoading: false,
                count: count || pagingCount,
                size: size || pagingSize,
                page: page || pagingPage,
            })
        }

        /**
         * Получает данные с сервера
         * @param extraParams - параметры запроса
         * @param merge - флаг объединения данных
         * @returns {Promise<void>}
         * @private
         */
        async fetchData(extraParams = {}, merge = false) {
            const { dataProvider, fetchError, datasource } = this.props
            const { data } = this.state

            if (datasource) {
                const { search } = extraParams
                const { sortFieldId, setFilter, fetchData } = this.props

                setFilter({ [sortFieldId]: search })

                fetchData({ ...omit(extraParams, 'search'), [sortFieldId]: search })

                return
            }

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
            } catch (err) {
                await this.setErrorMessage(err)
                fetchError(err)
            } finally {
                this.setState({ loading: false })
            }
        }

        componentWillUnmount() {
            const { abortController } = this.state

            if (abortController) {
                abortController.abort()
            }
        }

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
        setFilter: PropTypes.func,
        fetchData: PropTypes.func,
        removeAlerts: PropTypes.func,
        fetchError: PropTypes.func,
        valueFieldId: PropTypes.string,
        sortFieldId: PropTypes.string,
        dataProvider: PropTypes.object,
        datasource: PropTypes.string,
        datasourceModel: PropTypes.object,
        setRef: PropTypes.oneOfType([
            PropTypes.func,
            PropTypes.shape({ current: PropTypes.instanceOf(Element) }),
        ]),
    }

    WithFetchData.contextType = ReactReduxContext

    WithFetchData.defaultProps = {
        caching: false,
        size: 10,
    }

    const mapStateToProps = (state, { datasource }) => ({
        datasourceModel: getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)(state),
    })

    const mapDispatchToProps = (dispatch, ownProps) => ({
        addAlert: message => dispatch(addAlert(`${ownProps.form}.${ownProps.labelFieldId}`, message)),
        removeAlerts: () => dispatch(removeAllAlerts(`${ownProps.form}.${ownProps.labelFieldId}`)),
        fetchError: error => dispatch(fetchError(FETCH_CONTROL_VALUE, {}, error)),
    })

    return compose(
        connect(
            mapStateToProps,
            mapDispatchToProps,
            null,
            {
                pure: false,
            },
        ),
        withProps(props => ({
            fetch: props.caching ? 'lazy' : 'always',
            widget: false,
        })),
        WithDataSource,
    )(WithFetchData)
}

export default withFetchData
