import React, { useEffect, useMemo, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import unionWith from 'lodash/unionWith'
import isNaN from 'lodash/isNaN'
import get from 'lodash/get'

import listContainer from '../listContainer'

import { InputSelectTreeComponent } from './InputSelectTree'

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

function optionsHasValue(options, selectedValueId) {
    if (!options.length) {
        return false
    }

    return options.some(({ id }) => id === selectedValueId)
}

function createValue(value) {
    const numberValue = Number(value)

    return isNaN(numberValue) ? value : numberValue
}

function mapIds(options, valueFieldId, parentFieldId) {
    return options.map((option) => {
        const id = option[valueFieldId]
        const parent = option[parentFieldId]

        return { ...option, [valueFieldId]: createValue(id), [parentFieldId]: parent ? createValue(parent) : null }
    })
}

function InputSelectTreeContainer(props) {
    const { data: options, ajax, value, valueFieldId, isLoading, parentFieldId } = props
    const [unionOptions, setOptions] = useState(options)

    const optionsWithValues = useMemo(() => {
        if (isEmpty(value)) { return options }

        const values = Array.isArray(value) ? value : [value]

        if (isEmpty(options)) {
            return values
        }

        const newOptions = [...options]

        for (const selectedValue of values) {
            const selectedValueId = get(selectedValue, valueFieldId, null)

            if (!optionsHasValue(options, selectedValueId)) {
                newOptions.push(selectedValue)
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
            loading={isLoading}
        />
    )
}

InputSelectTreeContainer.defaultProps = {
    children: null,
    hasChildrenFieldId: 'hasChildren',
    disabled: false,
    loading: false,
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'name',
    iconFieldId: 'icon',
    badgeFieldId: 'badge',
    badgeColorFieldId: 'color',
    sortFieldId: 'name',
    hasCheckboxes: false,
    multiSelect: false,
    closePopupOnSelect: false,
    data: [],
    searchPlaceholder: '',
    transitionName: 'slide-up',
    choiceTransitionName: 'zoom',
    showCheckedStrategy: 'all',
    allowClear: true,
    placeholder: '',
    showSearch: true,
    dropdownPopupAlign: {
        points: ['tl', 'bl'],
        overflow: {
            adjustY: true,
        },
    },
    options: [],
    onSearch: () => {},
    onSelect: () => {},
    onChange: () => {},
    onClose: () => {},
    onToggle: () => {},
    onOpen: () => {},
    onFocus: () => {},
    onBlur: () => {},
    onInput: () => {},
}

export default listContainer(InputSelectTreeContainer)
