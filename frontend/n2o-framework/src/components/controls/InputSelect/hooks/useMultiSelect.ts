import { KeyboardEvent, useCallback, FocusEvent } from 'react'

import { id } from '../../../../utils/id'
import type { Option, UseMultiSelectParams } from '../types'
import { createLimitedTags } from '../helpers/multi'
import { getField, getOptionById } from '../helpers/common'
import { SUMMARY_TAG_ID } from '../const'

import { useSelect } from './useSelect'

export const useMultiSelect = ({
    options,
    selected,
    onChange,
    fetchData,
    onSearch,
    closePopupOnSelect,
    openOnFocus,
    inputLabelFieldId,
    popUpFullSize,
    enableCustomTags,
    maxTagCount,
    enabledFieldId,
    toggleOnInputClick,
    onBlur: baseOnBlur,
    resetOnBlur,
    summaryFormat,
    loading,
    quickSearchParam,
    disabled,
    propsOnKeyDown,
}: UseMultiSelectParams) => {
    const {
        inputValue,
        setForceInputFocus,
        onClearInput,
        onSelect: baseOnSelect,
        onKeyDown: baseOnKeyDown,
        activeValueId,
        setInputValue,
        popUpRef,
        tagsRef,
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
        filterOnOpen: true,
        loading,
        quickSearchParam,
        disabled,
    })

    // Добавление опции к выбранным
    const onSelect = useCallback(
        (option: Option) => {
            baseOnSelect()
            // readonly elements
            const newSelected = [...selected, option]

            onChange(newSelected)
        },
        [baseOnSelect, selected, onChange],
    )

    // Полная очистка выбранных значений и поля ввода
    const onClear = useCallback(() => {
        onClearInput()

        if (enabledFieldId) {
            const newSelected = selected.filter(item => !item[enabledFieldId])

            onChange(newSelected)

            return
        }

        onChange([])
    }, [onClearInput, enabledFieldId, onChange, selected])

    // Callback на blur
    const onBlur = useCallback((e: FocusEvent<HTMLInputElement>) => {
        baseOnBlur?.()

        const related = e?.relatedTarget
        const isClickInsidePopUp = popUpRef.current?.contains(related) ?? false
        const isClickInsideTag = tagsRef.current?.contains(related) ?? false

        if (resetOnBlur && !isClickInsidePopUp && !isClickInsideTag) {
            setInputValue('')
        }
    }, [baseOnBlur, popUpRef, resetOnBlur, setInputValue, tagsRef])

    // Удаление отдельной опции
    const onRemoveItem = useCallback(
        (option: Option) => {
            const { id } = option
            const newSelected = selected.filter((item: Option) => item.id !== id)

            onChange(newSelected)
            setForceInputFocus()
        },
        [selected, onChange, setForceInputFocus],
    )

    // Callback на key down
    const onKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        baseOnKeyDown(e)
        const input = e.currentTarget

        if (e.key === 'Backspace' && inputValue === '' && selected.length > 0) {
            e.preventDefault()

            const newSelected = [...selected]
            const lastIndex = newSelected.length - 1
            const last = newSelected[lastIndex]
            const enabled = getField<boolean>(last, enabledFieldId)

            if (enabled === false) {
                let indexToRemove = -1

                for (let i = lastIndex - 1; i >= 0; i -= 1) {
                    const item = newSelected[i]
                    const itemEnabled = getField<boolean>(item, enabledFieldId)

                    if (itemEnabled !== false) {
                        indexToRemove = i

                        break
                    }
                }
                if (indexToRemove !== -1) {
                    newSelected.splice(indexToRemove, 1)
                }
            } else {
                newSelected.pop()
            }

            onChange(newSelected)
            setForceInputFocus()
        }
        if (e.key === 'Enter') {
            propsOnKeyDown?.(e)

            if (e.key === 'Enter' && enableCustomTags && input.value) {
                onSelect({ [inputLabelFieldId]: input.value, id: id() })
                onClearInput()
            }

            const optionToRemove = getOptionById(activeValueId, selected)

            if (optionToRemove) {
                onRemoveItem(optionToRemove)

                return
            }

            const optionToAdd = getOptionById(activeValueId, options)

            if (optionToAdd) {
                onSelect(optionToAdd)
            }
        }
    }

    // Удаление тэга
    const onTagRemove = (removedId: string | number) => {
        if (removedId === SUMMARY_TAG_ID) { return }
        const newSelected = selected.filter(({ id }) => id !== removedId)

        onChange(newSelected)
        setForceInputFocus()
    }

    // смапленные теги
    const tags = createLimitedTags(selected, inputLabelFieldId, enabledFieldId, maxTagCount, summaryFormat)

    return {
        inputValue,
        setForceInputFocus,
        onSelect,
        onClear,
        onRemoveItem,
        onKeyDown,
        onTagRemove,
        tags,
        activeValueId,
        onBlur,
        popUpRef,
        tagsRef,
        ...rest,
    }
}
