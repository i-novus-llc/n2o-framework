import React, { useEffect, useMemo, useState } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import unionWith from 'lodash/unionWith'
import isNaN from 'lodash/isNaN'
import get from 'lodash/get'
import { type Props } from '@i-novus/n2o-components/lib/inputs/InputSelectTree/types'
import { InputSelectTreeComponent } from '@i-novus/n2o-components/lib/inputs/InputSelectTree/InputSelectTree'
import { type TOption } from '@i-novus/n2o-components/lib/inputs/InputSelect/types'

import listContainer from '../listContainer'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../../utils/emptyTypes'

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

function InputSelectTreeContainerBody({
    children = null,
    hasChildrenFieldId = 'hasChildren',
    disabled = false,
    loading = false,
    parentFieldId = 'parentId',
    valueFieldId = 'id',
    labelFieldId = 'name',
    iconFieldId = 'icon',
    badgeFieldId = 'badge',
    badgeColorFieldId = 'color',
    sortFieldId = 'name',
    hasCheckboxes = false,
    multiSelect = false,
    closePopupOnSelect = false,
    data = EMPTY_ARRAY,
    searchPlaceholder = '',
    transitionName = 'slide-up',
    choiceTransitionName = 'zoom',
    showCheckedStrategy = 'all',
    allowClear = true,
    placeholder = '',
    showSearch = true,
    dropdownPopupAlign = {
        points: ['tl', 'bl'],
        overflow: {
            adjustY: true,
        },
    },
    options = [],
    onSearch = NOOP_FUNCTION,
    onSelect = NOOP_FUNCTION,
    onChange = NOOP_FUNCTION,
    onClose = NOOP_FUNCTION,
    onToggle = NOOP_FUNCTION,
    onOpen = NOOP_FUNCTION,
    onFocus = NOOP_FUNCTION,
    onBlur = NOOP_FUNCTION,
    onInput = NOOP_FUNCTION,
    ...props
}: Props) {
    const { ajax, value } = props
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
            hasChildrenFieldId={hasChildrenFieldId}
            disabled={disabled}
            loading={loading}
            parentFieldId={parentFieldId}
            valueFieldId={valueFieldId}
            labelFieldId={labelFieldId}
            iconFieldId={iconFieldId}
            badgeFieldId={badgeFieldId}
            badgeColorFieldId={badgeColorFieldId}
            sortFieldId={sortFieldId}
            hasCheckboxes={hasCheckboxes}
            multiSelect={multiSelect}
            closePopupOnSelect={closePopupOnSelect}
            data={data}
            searchPlaceholder={searchPlaceholder}
            transitionName={transitionName}
            choiceTransitionName={choiceTransitionName}
            showCheckedStrategy={showCheckedStrategy}
            allowClear={allowClear}
            placeholder={placeholder}
            showSearch={showSearch}
            dropdownPopupAlign={dropdownPopupAlign}
            options={mappedOptions}
            onSearch={onSearch}
            onSelect={onSelect}
            onChange={onChange}
            onClose={onClose}
            onToggle={onToggle}
            onOpen={onOpen}
            onFocus={onFocus}
            onBlur={onBlur}
            onInput={onInput}
        >
            {children}
        </InputSelectTreeComponent>
    )
}

export const InputSelectTreeContainer = listContainer<Props>(InputSelectTreeContainerBody)

export default InputSelectTreeContainer
