import React, { Component, FC } from 'react'
import { connect, ReactReduxContext } from 'react-redux'
import get from 'lodash/get'
import isArray from 'lodash/isArray'
import has from 'lodash/has'
import unionBy from 'lodash/unionBy'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

// @ts-ignore import from js file
import cachingStore from '../../utils/cacher'
// @ts-ignore import from js file
import { fetchInputSelectData, FETCH_CONTROL_VALUE } from '../../core/api'
// @ts-ignore import from js file
import { addAlert, removeAllAlerts } from '../../ducks/alerts/store'
// @ts-ignore import from js file
import { dataProviderResolver } from '../../core/dataProviderResolver'
// @ts-ignore import from js file
import { fetchError } from '../../actions/fetch'
// @ts-ignore import from js file
import { WithDataSource } from '../../core/widget/WithDataSource'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { ModelPrefix } from '../../core/datasource/const'

type Props = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    data: any[],
    dataProvider: object,
    datasource: string,
    datasourceModel: [],
    caching: boolean,
    fetch: string,
    size: number,
    valueFieldId: string,
    sortFieldId: string,
    addAlert(obj: object): void,
    removeAlerts(): void,
    fetchError(obj: object): void,
    fetchData(obj: object): void,
    setFilter(obj: object): void,
    setRef(): void,
}

type State = {
    data: Props['data'],
    loading: boolean,
    count: number,
    size: Props['size'],
    page: number,
    abortController?: AbortController | null
}

type WrappedComponentProps = Pick<Props, 'valueFieldId' | 'sortFieldId'> & Pick<State, 'loading' | 'count' | 'size' | 'page'> & {
    options: Props['data'],
    fetchData(params: object, concat: boolean): void,
    ref: Props['setRef']
}

type Paging = {
    count: number,
    size: number,
    page: number
}

type Error = {
    response: Response,
    body: string
}

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 * @param apiCaller - promise для вызова апи
 *
 * TODO разобраться почему нормально не вешается ref, необходимый для ReduxField, если оборачиваемый компонент функциональный, а не классовый
 */
export function withFetchData(WrappedComponent: FC<WrappedComponentProps>, apiCaller = fetchInputSelectData) {
    class WithFetchData extends Component<Props, State> {
        constructor(props: Props) {
            super(props)

            this.state = {
                data: [],
                loading: false,
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
            const { datasourceModel, data } = this.props

            if (!isEmpty(datasourceModel)) {
                this.setState({ data: datasourceModel })
            } else if (!isEmpty(data)) {
                this.setState({ data })
            }
        }

        componentDidUpdate({ datasourceModel: prevDatasourceModel }: Props) {
            const { datasourceModel } = this.props

            if (datasourceModel) {
                const isNotEqual = !isEqual(datasourceModel, prevDatasourceModel)

                if (isNotEqual) {
                    this.setState({ data: datasourceModel })
                }
            }
        }

        /**
         * Поиск в кеше запроса
         * @param params
         * @returns {*}
         * @private
         */
        findResponseInCache(params: object) {
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
        addAlertMessage(messages: object) {
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
        async setErrorMessage({ response, body }: Error) {
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
         * @param cacheReset
         * @returns {Promise<void>}
         * @private
         */
        fetchDataProvider(dataProvider: object, extraParams = {}, cacheReset = false) {
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

            const params = {
                basePath,
                queryParams,
                extraParams,
            }

            const cached = this.findResponseInCache(params)

            if (cached && !cacheReset) {
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
            ).then((response: Response) => {
                cachingStore.add({ basePath, queryParams, extraParams }, response)
                this.setState({ abortController: null })

                return response
            })
        }

        /**
         *  Обновить данные если запрос успешен
         */
        setResponseToData({
            list,
            count,
            size,
            page,
            paging,
        }: {
            list: Props['data'],
            count: Paging['count'],
            size: Paging['size'],
            page: Paging['page'],
            paging: Paging },
        merge = false) {
            const { valueFieldId } = this.props
            const { data } = this.state
            const { count: pagingCount, size: pagingSize, page: pagingPage } = paging || {}

            this.setState({
                data: merge
                    ? unionBy(data, list, valueFieldId || 'id')
                    : list,
                loading: false,
                count: count || pagingCount,
                size: size || pagingSize,
                page: page || pagingPage,
            })
        }

        /**
         * Получает данные с сервера
         * @param extraParams - параметры запроса
         * @param merge - флаг объединения данных
         * @param cacheReset - флаг принудительного сбрасывания cache
         * @returns {Promise<void>}
         * @private
         */
        async fetchData(extraParams = {}, merge = false, cacheReset = false) {
            const { dataProvider, fetchError, datasource } = this.props
            const { data } = this.state

            if (datasource) {
                // eslint-disable-next-line @typescript-eslint/no-explicit-any
                const { search } = extraParams as any
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
                    cacheReset,
                )

                if (has(response, 'message')) { this.addAlertMessage(response.message) }

                this.setResponseToData(response, merge)
            } catch (err) {
                await this.setErrorMessage(err as Error)
                fetchError(err as Error)
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
            const { valueFieldId, sortFieldId, setRef } = this.props
            const { data, loading, count, size, page } = this.state

            return (
                <WrappedComponent
                    {...this.props}
                    {...this.state}
                    options={data}
                    loading={loading}
                    count={count}
                    size={size}
                    page={page}
                    valueFieldId={valueFieldId}
                    sortFieldId={sortFieldId}
                    fetchData={this.fetchData}
                    ref={setRef}
                />
            )
        }

        static defaultProps = {
            caching: false,
            size: 10,
        } as Props
    }

    WithFetchData.contextType = ReactReduxContext

    const mapStateToProps = (
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        state: any,
        { datasource, caching }: { datasource: string, caching: boolean },
    ) => ({
        datasourceModel: getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)(state),
        widget: false,
        fetch: caching ? 'lazy' : 'always',
    })

    const mapDispatchToProps = (
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        dispatch: (arg: any) => void,
        ownProps: {
            form: number | string
            labelFieldId: number | string
            datasource: string
            caching: boolean
        },
    ) => ({
        addAlert: (message: string) => dispatch(addAlert(`${ownProps.form}.${ownProps.labelFieldId}`, message)),
        removeAlerts: () => dispatch(removeAllAlerts(`${ownProps.form}.${ownProps.labelFieldId}`)),
        fetchError: (error: Error) => dispatch(fetchError(FETCH_CONTROL_VALUE, {}, error)),
    })

    return connect(mapStateToProps, mapDispatchToProps, null, { pure: false })(WithDataSource(WithFetchData))
}

export default withFetchData
