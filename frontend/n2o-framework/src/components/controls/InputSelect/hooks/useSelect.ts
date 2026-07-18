import { useState, useCallback, useRef, ChangeEvent } from 'react'
import { useClickOutside } from '@i-novus/n2o-components/lib/utils/useClickOutside'

import type { SelectPaging, UseSelectProps } from '../types'

import { useTriggerWidth } from './useTriggerWidth'
import { useSelectKeyboardNavigation } from './useSelectKeyBoardNavigation'

/**
 * Базовый хук для селектов, обрабатывает общий функционал
 * (запросы, состояние попапа, общая логикой фокусировки, клик вне, ввод/очистку input-value)
 * передает базовые эвенты
 * */
export function useSelect({
    fetchData: propsFetchData,
    onSearch,
    closePopupOnSelect,
    openOnFocus,
    popUpFullSize,
    options,
    selected,
    filterOnOpen = false,
    loading,
    quickSearchParam,
    labelFieldId,
    disabled,
}: UseSelectProps) {
    const [open, setOpen] = useState(false)
    const [inputValue, setInputValue] = useState<string>('')

    const inputRef = useRef<HTMLInputElement | null>(null)
    const inputComponentRef = useRef<HTMLInputElement | null>(null)
    const popUpRef = useRef<HTMLDivElement | null>(null)
    const popUpItemRef = useRef<HTMLDivElement | null>(null)
    const tagsRef = useRef<HTMLDivElement | null>(null)
    const selectPostfixRef = useRef<HTMLDivElement | null>(null)
    const triggerRef = useRef<HTMLDivElement>(null)

    const setForceInputFocus = useCallback(() => { inputRef.current?.focus() }, [])

    const fetchData = useCallback(() => {
        const paging: SelectPaging = { page: 1 }

        // TODO тут возможно недоработка с отправкой quickSearchParam
        if (Array.isArray(selected) && quickSearchParam && inputValue) {
            paging[quickSearchParam as 'value'] = inputValue
        }

        propsFetchData(paging)
    }, [inputValue, propsFetchData, quickSearchParam, selected])

    const onInput = useCallback((e: ChangeEvent<HTMLInputElement>) => {
        const { value } = e.target

        setInputValue(value)
        onSearch?.(value)
        setOpen(true)
    }, [onSearch])

    const onInputClick = useCallback(() => {
        if (loading) { return }

        if (!open) {
            setOpen(true)

            if (inputValue && filterOnOpen) {
                onSearch(inputValue)

                return
            }

            fetchData?.()
        }

        if (open) {
            setOpen(false)
        }
    }, [loading, open, inputValue, filterOnOpen, fetchData, onSearch])

    const onToggle = useCallback(() => {
        if (!open) {
            fetchData?.()
        }
        setForceInputFocus()
        setOpen(!open)
    }, [open, fetchData, setForceInputFocus])

    const onClearInput = useCallback(() => {
        setInputValue('')
        setForceInputFocus()

        if (open) { fetchData() }
    }, [fetchData, open, setForceInputFocus])

    const onSelect = useCallback(() => {
        setForceInputFocus()
        if (closePopupOnSelect) { setOpen(false) }
    }, [closePopupOnSelect, setForceInputFocus])

    const { onKeyDown, activeValueId } = useSelectKeyboardNavigation({
        options,
        selected,
        open,
        onToggle,
    })

    useClickOutside({
        isActive: open,
        onClickOutside: () => setOpen(false),
        excludeRefs: [inputRef, popUpRef, tagsRef, inputComponentRef, popUpItemRef, selectPostfixRef],
    })

    const triggerWidth = useTriggerWidth(triggerRef, popUpFullSize)

    // Открытие и фокусировка / закрытие, при клике на область вне html input, (прим. tags)
    const onTriggerClick = () => {
        if (disabled) { return }

        if (!inputRef.current?.contains(document.activeElement)) {
            if (open) {
                setOpen(false)

                return
            }

            setOpen(true)
            setForceInputFocus()

            if (inputValue && filterOnOpen) {
                onSearch(inputValue)

                return
            }

            fetchData?.()
        }
    }

    return {
        open,
        setOpen,
        inputValue,
        setInputValue,
        inputRef,
        inputComponentRef,
        popUpRef,
        popUpItemRef,
        tagsRef,
        selectPostfixRef,
        setForceInputFocus,
        onInput,
        onInputClick,
        onToggle,
        onClearInput,
        onSelect,
        triggerWidth,
        triggerRef,
        onKeyDown,
        activeValueId,
        onTriggerClick,
    }
}
