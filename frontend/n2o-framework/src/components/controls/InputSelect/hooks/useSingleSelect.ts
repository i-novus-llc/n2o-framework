import { useCallback, ChangeEvent, KeyboardEvent, useEffect } from 'react'

import { usePrevious } from '../../../../utils/usePrevious'
import { getField } from '../helpers/common'
import { findOptionToAdd } from '../helpers/single'
import type { Option, UseSingleSelectParams } from '../types'

import { useSelect } from './useSelect'

export const useSingleSelect = ({
    options,
    selected,
    onChange,
    inputLabelFieldId,
    labelFieldId,
    fetchData,
    onSearch,
    onBlur: baseOnBlur,
    resetOnBlur,
    closePopupOnSelect,
    openOnFocus,
    popUpFullSize,
    defaultValue,
    toggleOnInputClick,
    loading,
    quickSearchParam,
    enabledFieldId,
    disabled,
    propsOnKeyDown,
}: UseSingleSelectParams) => {
    const {
        onInput: baseOnInput,
        onClearInput,
        onSelect: baseOnSelect,
        onKeyDown: baseOnKeyDown,
        setInputValue,
        activeValueId,
        inputValue,
        ...rest
    } = useSelect({
        fetchData,
        onSearch,
        closePopupOnSelect,
        openOnFocus,
        popUpFullSize,
        options,
        selected,
        toggleOnInputClick,
        loading,
        quickSearchParam,
        labelFieldId,
        disabled,
    })

    const prevDefaultValue = usePrevious(defaultValue)

    useEffect(() => {
        // Установка default в инпут
        if (defaultValue) {
            setInputValue(String(defaultValue[inputLabelFieldId]))

            return
        }

        // Было очищено из вне (clear model)
        if (prevDefaultValue) {
            setInputValue('')
        }
    }, [defaultValue, inputLabelFieldId, prevDefaultValue, setInputValue])

    // Обработчик ввода: очищает выбор, если поле пустое
    const onInput = useCallback(
        (e: ChangeEvent<HTMLInputElement>) => {
            baseOnInput(e)
            if (!e.target.value) {
                onChange(null)
            }
        },
        [baseOnInput, onChange],
    )

    // Обработчик выбора опции из попапа
    const onSelect = useCallback(
        (option: Option) => {
            baseOnSelect()
            onChange(option)
            setInputValue(getField<string>(option, inputLabelFieldId))
        },
        [baseOnSelect, onChange, setInputValue, inputLabelFieldId],
    )

    // Очистка выбранного значения и поля ввода
    const onClear = useCallback(() => {
        onClearInput()
        onChange(null)
    }, [onClearInput, onChange])

    const selectedInputValue = selected ? getField<string>(selected, inputLabelFieldId) : ''

    // Callback на blur
    const onBlur = useCallback((e: ChangeEvent<HTMLInputElement>) => {
        baseOnBlur?.()

        if (resetOnBlur) {
            if (!e.target.value) {
                setInputValue('')

                return
            }
            setInputValue(selectedInputValue)
        }
    }, [baseOnBlur, resetOnBlur, selectedInputValue, setInputValue])

    // Callback на key down
    const onKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        baseOnKeyDown(e)

        if (!options.length) { return }

        if (e.key === 'Enter') {
            const optionToAdd = findOptionToAdd({
                activeValueId,
                options,
                inputValue,
                labelFieldId,
                enabledFieldId,
            })

            if (optionToAdd) { onSelect(optionToAdd) }

            // TODO пока костыль, для AdvancedTableFilter
            // запускает поиск с установленной моделью, проблема в асинхронности
            // событие срабатывает быстрее чем set model в onSelect
            setTimeout(() => propsOnKeyDown?.(e), 0)
        }
    }

    return {
        onInput,
        onSelect,
        onClear,
        onBlur,
        onKeyDown,
        activeValueId,
        inputValue,
        ...rest,
    }
}
