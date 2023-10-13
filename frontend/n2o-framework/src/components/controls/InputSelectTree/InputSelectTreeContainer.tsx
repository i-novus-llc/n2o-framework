import React, { Component } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import unionWith from 'lodash/unionWith'
import map from 'lodash/map'
import omit from 'lodash/omit'
import isArray from 'lodash/isArray'

import { Props, defaultProps } from '@i-novus/n2o-components/lib/inputs/InputSelectTree/allProps'
import { InputSelectTreeComponent } from '@i-novus/n2o-components/lib/inputs/InputSelectTree/InputSelectTree'

import listContainer from '../listContainer'
// @ts-ignore import from js file
import propsResolver from '../../../utils/propsResolver'

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
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} fetchData - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - size
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 */

type State = {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    options: any[]
}

class InputSelectTreeContainer extends Component<Props, State> {
    constructor(props: Props) {
        super(props)
        this.state = {
            options: overrideOptions(props.options, props.value),
        }
    }

    static getDerivedStateFromProps(nextProps: Props, prevState: State) {
        if (nextProps.options !== prevState.options && nextProps.ajax) {
            return { options: unionWith(nextProps.options, prevState.options, isEqual) }
        }

        return { options: nextProps.options }
    }

    render() {
        const { options } = this.state
        const { loading, format } = this.props

        let formattedOptions = [...options]

        if (format) {
            formattedOptions = options
                .map(option => ({ ...option, formattedTitle: propsResolver({ format }, option).format }))
        }

        return (
            <InputSelectTreeComponent
                {...this.props}
                options={formattedOptions}
                loading={loading}
            />
        )
    }

    static defaultProps = defaultProps
}

const overrideOptions = (options: Props['options'], value: Props['value']) => {
    const newValue = isArray(value) ? value : [value]

    if (isEmpty(options) && !isEmpty(value)) {
        return map(newValue, val => ({
            ...omit(val, ['hasChildren']),
        }))
    }

    return options
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default listContainer(InputSelectTreeContainer as any)
