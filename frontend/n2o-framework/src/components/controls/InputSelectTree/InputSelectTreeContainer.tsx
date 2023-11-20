import React, { useEffect, useMemo, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import unionWith from 'lodash/unionWith'
import isNaN from 'lodash/isNaN'
import get from 'lodash/get'

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

function optionsHasValue(options: TOption[], selectedValueId: string | number | null) {
    if (!options.length) {
        return false
    }

    return options.some(({ id }) => id === selectedValueId)
}

function createValue(value: string | number) {
    const numberValue = Number(value)

    return isNaN(numberValue) ? value : numberValue
}

function mapIds(options: TOption[], valueFieldId: keyof TOption, parentFieldId: keyof TOption) {
    return options.map((option) => {
        const id: string | number = option[valueFieldId]
        const parent: string | number | undefined = option[parentFieldId]

        return { ...option, [valueFieldId]: createValue(id), [parentFieldId]: parent ? createValue(parent) : null }
    })
}

function InputSelectTreeContainer(props: Props) {
    const { options, ajax, value, valueFieldId, parentFieldId } = props
    const [unionOptions, setOptions] = useState(options)

    const optionsWithValues = useMemo(() => {
        if (isEmpty(value)) { return options }

        const values = Array.isArray(value) ? value : [value]

        if (isEmpty(options)) {
            return values as typeof options
        }

        const newOptions = [...options]

        for (const selectedValue of values) {
            const selectedValueId: string | number | null = get(selectedValue, valueFieldId, null)

            if (!optionsHasValue(options, selectedValueId)) {
                newOptions.push(selectedValue as TOption)
            }
        }

        return newOptions
    }, [value, options, valueFieldId])

    useEffect(() => {
        if (!isEqual(optionsWithValues, unionOptions)) {
            if (ajax) {
                setOptions(unionWith(optionsWithValues, unionOptions, isEqual))
            } else {
                setOptions(optionsWithValues)
            }
        }
    }, [optionsWithValues, ajax, unionOptions])

    const mappedOptions = mapIds(unionOptions, valueFieldId, parentFieldId)

    return (
        <InputSelectTreeComponent
            {...props}
            options={mappedOptions}
        />
    )
}

InputSelectTreeContainer.defaultProps = defaultProps

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default listContainer(InputSelectTreeContainer as any)
