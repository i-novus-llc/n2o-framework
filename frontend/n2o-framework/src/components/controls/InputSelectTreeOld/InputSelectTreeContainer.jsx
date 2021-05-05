import React from 'react'
import PropTypes from 'prop-types'

import InputSelectTree from '../InputSelectTree/InputSelectTree'
import withFetchData from '../withFetchData'
import listContainer from '../listContainer'

/**
 * Контейнер для {@link InputSelect}
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onSelect - callback при выборе значения из popup
 * @reactProps {function} onScrollEnd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - size
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {boolean} collapseSelected - флаг сжатия выбранных элементов
 * @reactProps {number} lengthToGroup - от скольки элементов сжимать выбранные элементы
 * @reactProps {function} fetchData
 * @reactProps {function} onSearch
 * @reactProps {function} _handleItemOpen
 * @reactProps {boolean} ajax
 */

class InputSelectTreeContainer extends React.Component {
    /**
   * Рендер
   */

    render() {
        const { filter } = this.props

        const filterType = filter === 'server' ? false : filter

        return (
            <InputSelectTree
                {...this.props}
                options={this.props.data}
                value={this.props.value}
                filter={filterType}
                key={this.key}
                loading={this.props.isLoading}
            />
        )
    }
}

InputSelectTreeContainer.propTypes = {
    loading: PropTypes.bool,
    options: PropTypes.array,
    valueFieldId: PropTypes.string.isRequired,
    labelFieldId: PropTypes.string.isRequired,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    disabled: PropTypes.bool,
    disabledValues: PropTypes.array,
    filter: PropTypes.oneOf(['includes', 'startsWith', 'endsWith', 'server']),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onInput: PropTypes.func,
    onSelect: PropTypes.func,
    onScrollEnd: PropTypes.func,
    placeholder: PropTypes.string,
    resetOnBlur: PropTypes.bool,
    onOpen: PropTypes.func,
    onClose: PropTypes.func,
    size: PropTypes.number,
    multiSelect: PropTypes.bool,
    groupFieldId: PropTypes.string,
    closePopupOnSelect: PropTypes.bool,
    hasCheckboxes: PropTypes.bool,
    format: PropTypes.string,
    collapseSelected: PropTypes.bool,
    lengthToGroup: PropTypes.number,
    fetchData: PropTypes.func,
    onSearch: PropTypes.func,
    handleItemOpen: PropTypes.func,
    ajax: PropTypes.bool,
}

InputSelectTreeContainer.defaultProps = {
    loading: false,
    disabled: false,
    disabledValues: [],
    resetOnBlur: false,
    filter: false,
    multiSelect: false,
    closePopupOnSelect: true,
    hasCheckboxes: false,
    collapseSelected: true,
    lengthToGroup: 3,
}

export default listContainer(InputSelectTreeContainer)
