import React from 'react'
import PropTypes from 'prop-types'

import listContainer from '../listContainer'

// eslint-disable-next-line import/no-named-as-default
import N2OSelect from './N2OSelect'

/**
 * Контейнер для {@link N2OSelect}
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onSelect - callback при выборе значения из popup
 * @reactProps {function} onScrollENd - callback при прокрутке скролла popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - size
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 */

function N2OSelectContainer(props) {
    const { filter, data, isLoading } = props
    const filterType = filter === 'server' ? false : filter

    return (
        <N2OSelect
            {...props}
            options={data}
            filter={filterType}
            loading={isLoading}
        />
    )
}

N2OSelectContainer.propTypes = {
    data: PropTypes.any,
    isLoading: PropTypes.bool,
    loading: PropTypes.bool,
    options: PropTypes.array,
    valueFieldId: PropTypes.string.isRequired,
    labelFieldId: PropTypes.string.isRequired,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
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
    groupFieldId: PropTypes.string,
    format: PropTypes.string,
    searchByTap: PropTypes.bool,
    fetchData: PropTypes.func,
    onSearch: PropTypes.func,
}

N2OSelectContainer.defaultProps = {
    loading: false,
    disabled: false,
    disabledValues: [],
    resetOnBlur: false,
    filter: false,
    // eslint-disable-next-line react/default-props-match-prop-types
    hasCheckboxes: false,
    searchByTap: false,
}

export default listContainer(N2OSelectContainer)
