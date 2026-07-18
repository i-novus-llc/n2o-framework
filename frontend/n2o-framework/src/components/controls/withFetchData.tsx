import React, { Component, FC } from 'react'
import { Dispatch } from 'redux'
import { connect, ReactReduxContext } from 'react-redux'
import get from 'lodash/get'
import isArray from 'lodash/isArray'
import has from 'lodash/has'
import unionBy from 'lodash/unionBy'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'
import isEmpty from 'lodash/isEmpty'

import { State as Store } from '../../ducks/State'
import cachingStore from '../../utils/cacher'
import { fetchInputSelectData } from '../../core/api'
import { addAlert } from '../../ducks/alerts/store'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { WithDataSource } from '../../core/widget/WithDataSource'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { ModelPrefix } from '../../core/models/types'
import { Alert } from '../../ducks/alerts/Alerts'
import { FETCH_TYPE } from '../../core/widget/const'
import { GLOBAL_KEY } from '../../ducks/alerts/constants'

enum SearchSide {
    CLIENT = 'client',
    SERVER = 'server',
}

type DataItem = Record<string, string | number>

export interface Props {
    data: DataItem[]
    dataProvider: { quickSearchParam: string }
    quickSearchParam?: string
    datasource: string
    datasourceModel: []
    caching: boolean
    fetch: string
    size: number
    valueFieldId: string
    sortFieldId: string
    searchMinLength: number
    searchMinLengthHint?: string
    value: Record<string, string> | null
    page: number
    count: number
    searchSide: SearchSide
    labelFieldId: string
    parentFieldId?: string
    addAlert(obj: object): void
    fetchData(obj: object): void
    setFilter(obj: object): void
    setRef(): void
}

export type SearchMinLengthHint = false | true

interface State {
    data: Props['data']
    clientFilteredData: null | Props['data']
    clientFilteredCount: null | number
    loading: boolean
    count: number
    size: Props['size']
    page: number
    abortController?: AbortController | null
    searchMinLengthHint: SearchMinLengthHint
    quickSearchParam: string
    merge: boolean
}

export type getSearchMinLengthHintType = (
    customHint?: string,
    component?: React.ComponentType
) => null | string | JSX.Element

export type WrappedComponentProps = Pick<Props, 'valueFieldId' | 'sortFieldId'> & Pick<State, 'loading' | 'count' | 'size' | 'page'> & {
    options: Props['data']
    fetchData(params: Record<string, string>, merge: boolean, cacheReset?: boolean): void
    ref: Props['setRef']
    getSearchMinLengthHint: getSearchMinLengthHintType
    quickSearchParam: string
}

interface Paging {
    count: number
    size: number
    page: number
}

interface Error {
    response: Response
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
            const { dataProvider, quickSearchParam = 'search', size = 10 } = this.props

            this.state = {
                data: [],
                clientFilteredData: null,
                loading: false,
                count: 0,
                clientFilteredCount: null,
                size,
                page: 1,
                searchMinLengthHint: false,
                quickSearchParam: dataProvider?.quickSearchParam || quickSearchParam,
                merge: false,
            }
        }

        componentDidMount() {
            const { datasourceModel, data } = this.props

            if (!isEmpty(datasourceModel)) {
                this.setState({ data: datasourceModel })
            } else if (!isEmpty(data)) {
                this.setState({ data })
            }
        }

        componentDidUpdate({
            datasourceModel: prevDatasourceModel,
            data: prevData,
            count: prevCount,
            page: prevPage,
            size: prevSize,
        }: Props) {
            const { datasourceModel, data, page, count, size } = this.props
            const { merge } = this.state

            if (count !== prevCount) {
                this.setState({ count })
            }

            if (page !== prevPage) {
                this.setState({ page })
            }

            if (size !== prevSize) {
                this.setState({ size })
            }

            if (datasourceModel && !isEqual(datasourceModel, prevDatasourceModel)) {
                this.setState({
                    data: merge ? [...prevDatasourceModel, ...datasourceModel] : datasourceModel,
                })

                return
            }

            if (data && !isEqual(data, prevData)) {
                this.setState({
                    data: merge ? [...prevData, ...data] : data,
                })
            }
        }

        /**
         * Поиск в кеше запроса
         * @param params
         * @returns {*}
         * @private
         */
        findResponseInCache = (params: object) => {
            const { caching = false } = this.props

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
        addAlertMessage = (messages: object) => {
            const { addAlert } = this.props

            if (isArray(messages)) {
                messages.forEach(m => addAlert({ ...m, closeButton: false }))
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
        setErrorMessage = async ({ response, body }: Error) => {
            let errorMessage

            if (response) {
                errorMessage = await response.json()
            } else {
                errorMessage = body
            }

            try { errorMessage = JSON.parse(errorMessage) } catch (e) { /**/ }
            const messages = get(errorMessage, 'meta.alert.messages', null)

            if (messages) { this.addAlertMessage(messages) }
        }

        /**
         * Взять данные с сервера с помощью dataProvider
         * @param dataProvider
         * @param extraParams
         * @param cacheReset
         * @returns {Promise<void>}
         * @private
         */
        fetchDataProvider = (dataProvider: object, extraParams = {}, cacheReset = false) => {
            const { store } = this.context
            const { abortController } = this.state
            const { caching = false } = this.props

            if (abortController) { abortController.abort() }

            const {
                basePath,
                baseQuery: queryParams,
                headersParams,
                // @ts-ignore import from js file
            } = dataProviderResolver(store.getState(), dataProvider)

            const params = {
                basePath,
                queryParams,
                extraParams,
            }

            const cached = caching ? this.findResponseInCache(params) : null

            if (cached && !cacheReset) { return cached }

            const controller = new AbortController()

            this.setState({ abortController: controller })

            return apiCaller(
                { headers: headersParams, query: { ...queryParams, ...extraParams } },
                // @ts-ignore import from js file
                { basePath },
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
        setResponseToData = (
            {
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
            merge = false,
        ) => {
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

        getSearchMinLengthHint = (hint?: string, component?: React.ComponentType): null | string | JSX.Element => {
            const { searchMinLengthHint } = this.state

            if (!searchMinLengthHint) { return null }

            if (hint && !component) { return hint }
            if (hint && component) {
                const Component = component

                return <Component>{hint}</Component>
            }

            const {
                searchMinLength,
                searchMinLengthHint: propsSearchMinLengthHint,
            } = this.props

            const defaultHint = `Введите не менее ${searchMinLength} символов`

            if (component) {
                const Component = component

                return <Component>{propsSearchMinLengthHint || defaultHint}</Component>
            }

            return propsSearchMinLengthHint || defaultHint
        }

        /**
         * Получает данные с сервера
         * @param extraParams - параметры запроса
         * @param merge - флаг объединения данных
         * @param cacheReset - флаг принудительного сбрасывания cache
         * @returns {Promise<void>}
         * @private
         */
        fetchData = async (
            extraParams: Record<string, string> = {},
            merge = false,
            cacheReset = false,
            // TODO рефакторинг
            // eslint-disable-next-line sonarjs/cognitive-complexity
        ): Promise<void> => {
            const {
                dataProvider,
                datasource,
                searchMinLength,
                searchSide,
                labelFieldId,
                valueFieldId,
                parentFieldId,
            } = this.props
            const { data, quickSearchParam } = this.state
            const value = extraParams[quickSearchParam]

            if (searchMinLength) {
                if (!value || value?.length < searchMinLength) {
                    this.setState({ searchMinLengthHint: true })

                    return
                }

                this.setState({ searchMinLengthHint: false })
            }

            this.setState({ merge })

            if (searchSide === SearchSide.CLIENT && !isEmpty(data)) {
                if (!value) {
                    this.setState({
                        clientFilteredData: null,
                        clientFilteredCount: null,
                    })
                } else {
                    const idMap = new Map()

                    data.forEach((item) => {
                        const id = item[valueFieldId]

                        if (id != null) { idMap.set(id, item) }
                    })

                    // поиск по labelFieldId
                    const matchedItems = data.filter((item) => {
                        const itemValue = item[labelFieldId]

                        if (!itemValue) { return false }

                        return String(itemValue).toLowerCase().includes(String(value).toLowerCase())
                    })

                    const resultIds = new Set()

                    matchedItems.forEach((item) => {
                        let current = item
                        const visited = new Set()

                        while (current) {
                            const currentId = current[valueFieldId]

                            if (currentId != null) {
                                if (visited.has(currentId)) { break }
                                visited.add(currentId)
                                resultIds.add(currentId)
                            }

                            // поиск по parentFieldId
                            if (parentFieldId) {
                                const parentId = current[parentFieldId]

                                if (parentId && idMap.has(parentId)) {
                                    current = idMap.get(parentId)
                                } else {
                                    break
                                }
                            } else {
                                break
                            }
                        }
                    })

                    const clientFilteredData = data.filter(item => resultIds.has(item[valueFieldId]))

                    this.setState({
                        clientFilteredData,
                        clientFilteredCount: clientFilteredData.length,
                    })

                    return
                }
            }
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

                if (Number(extraParams.page) === 1) {
                    this.setState({ clientFilteredData: [], clientFilteredCount: 0 })
                }

                const response = await this.fetchDataProvider(
                    dataProvider,
                    extraParams,
                    cacheReset,
                )

                if (has(response, 'message')) { this.addAlertMessage(response.message) }

                this.setResponseToData(response, merge)
            } catch (err) {
                await this.setErrorMessage(err as Error)
            } finally {
                this.setState({ loading: false })
                this.setState({ clientFilteredData: null, clientFilteredCount: null })
            }
        }

        componentWillUnmount() {
            const { abortController } = this.state

            if (abortController) { abortController.abort() }
        }

        render() {
            const { valueFieldId, sortFieldId, setRef, searchSide } = this.props
            const { data, clientFilteredData, loading, count, clientFilteredCount, size, page, quickSearchParam } = this.state

            return (
                <WrappedComponent
                    {...this.props}
                    {...this.state}
                    options={searchSide === SearchSide.CLIENT && clientFilteredData ? clientFilteredData : data}
                    loading={loading}
                    count={searchSide === SearchSide.CLIENT && typeof clientFilteredCount === 'number' ? clientFilteredCount : count}
                    size={size}
                    page={page}
                    valueFieldId={valueFieldId}
                    sortFieldId={sortFieldId}
                    fetchData={this.fetchData}
                    ref={setRef}
                    getSearchMinLengthHint={this.getSearchMinLengthHint}
                    quickSearchParam={quickSearchParam}
                />
            )
        }
    }

    WithFetchData.contextType = ReactReduxContext

    const mapStateToProps = (state: Store, { datasource, caching }: Props) => ({
        datasourceModel: getModelByPrefixAndNameSelector(ModelPrefix.source, datasource)(state),
        widget: false,
        fetch: caching ? FETCH_TYPE.lazy : FETCH_TYPE.always,
    })

    const mapDispatchToProps = (dispatch: Dispatch) => ({
        addAlert: (message: Alert) => dispatch(addAlert(message.placement || GLOBAL_KEY, message)),
    })

    // FIXME
    return connect(mapStateToProps, mapDispatchToProps, null, { pure: false })(WithDataSource(WithFetchData as never))
}

export default withFetchData
