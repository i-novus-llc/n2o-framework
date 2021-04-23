import React, { Component } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import unionWith from 'lodash/unionWith'
import map from 'lodash/map'
import omit from 'lodash/omit'
import isArray from 'lodash/isArray'
import { withProps, compose, setDisplayName } from 'recompose'

import listContainer from '../listContainer.js'

import { propTypes, defaultProps } from './allProps'
import InputSelectTree from './InputSelectTree'

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
 */

class InputSelectTreeContainer extends Component {
    constructor(props) {
        super(props)
        this.state = {
            data: props.data,
        }
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps.data !== prevState.data && nextProps.ajax) {
            return { data: unionWith(nextProps.data, prevState.data, isEqual) }
        }
        return { data: nextProps.data }
    }

    render() {
        return (
            <InputSelectTree
                {...this.props}
                data={this.state.data}
                loading={this.props.isLoading}
            />
        )
    }
}

InputSelectTreeContainer.propTypes = propTypes
InputSelectTreeContainer.defaultProps = defaultProps

const overrideDataWithValue = withProps(({ data, value, parentFieldId }) => {
    const newValue = isArray(value) ? value : [value]
    if (isEmpty(data) && !isEmpty(value)) {
        return {
            data: map(newValue, val => ({
                ...omit(val, ['hasChildren']),
            })),
        }
    }
})

export { InputSelectTreeContainer }
export default compose(
    setDisplayName('InputSelectTreeContainer'),
    listContainer,
    overrideDataWithValue,
)(InputSelectTreeContainer)
