import React, { useEffect, useMemo, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import unionWith from 'lodash/unionWith'

import { Props, defaultProps } from '@i-novus/n2o-components/lib/inputs/InputSelectTree/allProps'
import { InputSelectTreeComponent } from '@i-novus/n2o-components/lib/inputs/InputSelectTree/InputSelectTree'
import { TOption } from '@i-novus/n2o-components/lib/inputs/InputSelect/types'

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

function optionsHasValue(options: TOption[], selectedValueId: string) {
    if (!options.length) {
        return false
    }

    return options.some(({ id }) => id === selectedValueId)
}

function InputSelectTreeContainer(props: Props) {
    const { options, ajax, value, valueFieldId } = props
    const [unionOptions, setOptions] = useState(options)

    const optionsWithValues = useMemo(() => {
        if (isEmpty(value)) { return options }

        const values = Array.isArray(value) ? value : [value]

        if (isEmpty(options)) {
            return values as typeof options
        }

        const newOptions = [...options]

        for (const selectedValue of values) {
            const selectedValueId = selectedValue[valueFieldId]

            if (!optionsHasValue(options, selectedValueId)) {
                newOptions.push(selectedValue)
            }
        }

        return newOptions
    }, [value, options])

    useEffect(() => {
        if (!isEqual(optionsWithValues, unionOptions)) {
            if (ajax) {
                setOptions(unionWith(optionsWithValues, unionOptions, isEqual))
            } else {
                setOptions(optionsWithValues)
            }
        }
    }, [optionsWithValues, ajax, unionOptions])

    return (
        <InputSelectTreeComponent
            {...props}
            options={unionOptions}
        />
    )
}

InputSelectTreeContainer.defaultProps = defaultProps

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default listContainer(InputSelectTreeContainer as any)
