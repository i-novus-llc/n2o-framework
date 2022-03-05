import React from 'react'
import PropTypes from 'prop-types'
import throttle from 'lodash/throttle'
import debounce from 'lodash/debounce'
import { connect } from 'react-redux'

import { alertsByKeySelector } from '../../ducks/alerts/store'

/**
 * HOC для контейнеров {@Link InputSelectContainer} и {@Link N2OSelectContainer}
 * @param WrappedComponent - оборачиваемый компонент
 */

function withListContainer(WrappedComponent) {
    /**
     * Класс для хока
     * @reactProps {boolean} loading - флаг анимации загрузки
     * @reactProps {array} options - данные
     * @reactProps {string} labelFieldId - поле для для названия
     * @reactProps {function} onInput - callback при вводе в инпут
     * @reactProps {function} onScrollEnd - callback при прокрутке скролла popup
     * @reactProps {function} onOpen - callback на открытие попапа
     * @reactProps {string} queryId - queryId
     * @reactProps {number} size - size
     * @reactProps {function} fetchData - callback на загрузку данных
     * @reactProps {array} options - данные с сервера
     */
    const WithListContainer = ({
        _fetchData,
        dataProvider,
        onOpen,
        onInput,
        count,
        size,
        page,
        data,
        onScrollEnd,
        loading,
        labelFieldId,
        sortFieldId,
        valueFieldId,
        ...rest
    }) => {
        /**
         * Совершает вызов апи с параметрами
         * @param optionalParams {object} - дополнительные параметра запроса
         * @param concat {boolean} - флаг добавления новых данных к текущим
         */
        const callApiWithParams = (optionalParams = {}, concat = false) => {
            const sortId = sortFieldId || valueFieldId || labelFieldId

            const params = {
                size,
                page,
                [`sorting.${sortId}`]: 'ASC',
                ...optionalParams,
            }

            _fetchData(params, concat)
        }

        /**
         * Обрабатывает открытие попапа
         * @private
         */
        const handleOpen = () => {
            callApiWithParams({ page: 1 })

            if (onOpen) {
                onOpen()
            }
        }

        /**
         * Обрабатывает серверный поиск
         * @param value - Значение для поиска
         * @param delay - Задержка при вводе
         * @private
         */
        const handleSearch = debounce((value) => {
            const quickSearchParam = (dataProvider && dataProvider.quickSearchParam) || 'search'

            callApiWithParams({ [quickSearchParam]: value, page: 1 })
        }, 300)

        const handleItemOpen = (value) => {
            callApiWithParams({ 'filter.parent_id': value }, true)
        }

        /**
         * Обрабатывает изменение инпута
         * @param value {string|number} - новое значение
         * @private
         */
        const handleInputChange = (value) => {
            if (onInput) {
                onInput(value)
            }
        }

        /**
         * Обрабатывает конец скролла
         * @private
         */
        const handleScrollEnd = throttle((filter = {}) => {
            if ((page && size && count) && (page * size < count)) {
                callApiWithParams({ page: page + 1, ...filter }, true)
            }
            if (onScrollEnd) {
                onScrollEnd()
            }
        }, 400)

        return (
            <WrappedComponent
                {...rest}
                labelFieldId={labelFieldId}
                valueFieldId={valueFieldId}
                data={data}
                isLoading={loading}
                onInput={handleInputChange}
                onScrollEnd={handleScrollEnd}
                onOpen={handleOpen}
                onSearch={handleSearch}
                handleItemOpen={handleItemOpen}
                _fetchData={_fetchData}
            />
        )
    }

    WithListContainer.propTypes = {
        loading: PropTypes.bool,
        queryId: PropTypes.string,
        sortFieldId: PropTypes.string,
        valueFieldId: PropTypes.string,
        size: PropTypes.number,
        labelFieldId: PropTypes.string,
        fetchData: PropTypes.func,
        _fetchData: PropTypes.func,
        options: PropTypes.array,
        data: PropTypes.array,
        onOpen: PropTypes.func,
        onInput: PropTypes.func,
        onScrollEnd: PropTypes.func,
        quickSearchParam: PropTypes.string,
        dataProvider: PropTypes.object,
        page: PropTypes.number,
        count: PropTypes.number,
    }

    WithListContainer.defaultProps = {
        data: [],
    }

    return connect(
        mapStateToProps,
        null,
    )(WithListContainer)
}

const mapStateToProps = (state, ownProps) => ({
    alerts: alertsByKeySelector(`${ownProps.form}.${ownProps.labelFieldId}`)(state),
})

export default withListContainer
