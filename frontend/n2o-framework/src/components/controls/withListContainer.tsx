import React, { FC } from 'react'
import { connect } from 'react-redux'
import debounce from 'lodash/debounce'

// @ts-ignore import from js file
import { alertsByKeySelector } from '../../ducks/alerts/selectors'

type Props = {
    data: [],
    options: [],
    dataProvider?: {
        quickSearchParam?: string
    },
    loading: boolean, // флаг анимации загрузки
    sortFieldId: string,
    valueFieldId: string,
    labelFieldId: string, // поле для названия
    quickSearchParam: string,
    searchMinLength: number,
    throttleDelay: number,
    size?: number,
    page?: number,
    count?: number,
    onOpen(valueObj?: object): void, // callback на открытие попапа
    onInput(value: string | number): void, // callback при вводе в инпут
    onScrollEnd(): void, // callback при прокрутке скролла popup
    fetchData(params: object, concat: boolean, cacheReset: boolean): void,
}

type WrappedComponentProps = Omit<Props, 'data' | 'quickSearchParam' | 'throttleDelay' | 'onOpen' |'onInput' | 'onScrollEnd'>&{
    onSearch(value: string): void,
}

export function withListContainer(WrappedComponent: FC<WrappedComponentProps>) {
    const WithListContainer = ({
        dataProvider,
        count,
        size,
        page,
        options,
        loading,
        labelFieldId,
        sortFieldId,
        valueFieldId,
        searchMinLength,
        throttleDelay,
        data,
        onOpen,
        onInput,
        onScrollEnd,
        fetchData,
        ...rest
    }: Props) => {
        /**
         * Совершает вызов апи с параметрами
         * @param optionalParams {object} - дополнительные параметра запроса
         * @param concat {boolean} - флаг добавления новых данных к текущим
         * @param cacheReset {boolean} - флаг принудительного сбрасывания cache в withFetchData
         */
        const callApiWithParams = (optionalParams = {}, concat = false, cacheReset = false) => {
            const sortId = sortFieldId || valueFieldId || labelFieldId

            const params = {
                size,
                page,
                [`sorting.${sortId}`]: 'ASC',
                ...optionalParams,
            }

            fetchData(params, concat, cacheReset)
        }
        /**
         * Обрабатывает серверный поиск
         * @param value - Значение для поиска
         * @param delay - Задержка при вводе
         * @private
         */
        const delay = throttleDelay || 300

        const handleSearch = debounce((value) => {
            if (searchMinLength && value && value.length < searchMinLength) {
                return
            }

            const quickSearchParam = dataProvider?.quickSearchParam || 'search'

            callApiWithParams({ [quickSearchParam]: value, page: 1 })
        }, delay)

        return (
            <WrappedComponent
                {...rest}
                options={options}
                labelFieldId={labelFieldId}
                valueFieldId={valueFieldId}
                sortFieldId={sortFieldId}
                loading={loading}
                size={size}
                count={count}
                page={page}
                fetchData={callApiWithParams}
                onSearch={handleSearch}
                searchMinLength={searchMinLength}
            />
        )
    }

    WithListContainer.defaultProps = {
        options: [],
    } as Props

    return connect(
        mapStateToProps,
        null,
    )(WithListContainer)
}

type OwnProps = {
    form: string,
    labelFieldId: string
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const mapStateToProps = (state: any, ownProps: OwnProps) => ({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    alerts: (alertsByKeySelector(`${ownProps.form}.${ownProps.labelFieldId}` as any))(state),
})

export default withListContainer
