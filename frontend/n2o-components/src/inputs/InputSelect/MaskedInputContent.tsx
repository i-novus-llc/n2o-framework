import React, { KeyboardEvent, ReactNode, useCallback, useMemo, useState } from 'react'
import find from 'lodash/find'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../utils/emptyTypes'
import { useMask } from '../helpers/input/useMask'
import { useInputController } from '../helpers/input/useInputController'

import { InputElements as SelectedItems } from './SelectedItems'
import { getNextId, getPrevId, getFirstNotDisabledId } from './utils'
import { TOption } from './types'

export enum MaskPasteMode {
    FREE = 'free',
    STRICT = 'strict',
}

/**
 * Извлекает реально введённые данные из строки с маской.
 * @param maskedProps - строка, содержащая символы маски ('_') и введённые данные
 * @param mask - маска
 * @returns только реально введённые символы с сохранением разделителей между ними
 */
export function extractRealData(maskedProps: string, mask?: string): string {
    if (!mask) { return maskedProps }

    let masked = maskedProps

    // TODO не совпадает типизация input, заявлено string по факту может быть string[]
    if (Array.isArray(masked)) {
        masked = maskedProps[0] || ''
    }

    // Проверка, является ли символ буквой или цифрой (реально введённым)
    const isRealChar = (ch: string): boolean => /[\dA-Za-z]/.test(ch)

    // Находим последний реальный символ в строке
    let lastRealIndex = -1

    for (let i = masked.length - 1; i >= 0; i--) {
        if (isRealChar(masked[i])) {
            lastRealIndex = i

            break
        }
    }

    // Если реальных символов нет – возвращаем пустую строку
    if (lastRealIndex === -1) {
        return ''
    }

    // Берём часть строки до последнего реального символа включительно
    const relevant = masked.substring(0, lastRealIndex + 1)

    // Удаляем все оставшиеся символы маски (подчёркивания)
    return relevant.replace(/_/g, '')
}

const isMaskFilled = (mask: string, value?: string): boolean => {
    if (!value) { return true }

    // Количество цифровых символов в маске (количество '9')
    const digitsCount = mask.split(/\d/g).length - 1
    // Количество фактически введенных цифр
    const enteredDigits = value.replace(/\D/g, '').length

    return enteredDigits === digitsCount
}

/**
 * InputSelectGroup
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {string} value - текущее значение инпута
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {function} onRemoveItem - callback при нажатии на удаление элемента из выбранных при мульти выборе
 * @reactProps {function} onFocus - событие фокуса
 * @reactProps {function} onBlur - событие потери фокуса
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} valueFieldId
 * @reactProps {boolean} multiSelect - фдаг мульти выбора
 * @reactProps {number} [maxTagCount] - от скольки элементов сжимать выбранные элементы
 * @reactProps {number} maxTagTextLength - максимальная длина текста в тэге, до усечения
 * @reactProps {function} onInputChange - callback при изменение инпута
 * @reactProps {function} openPopUp - открытие попапа
 * @reactProps {function} closePopUp - закрытие попапа
 * @reactProps {string} activeValueId
 * @reactProps {function} setActiveValueId
 * @reactProps {array} disabledValues
 * @reactProps {object} options
 * @reactProps {function} onSelect - событие выбора
 * @reactProps {function} onClick - событие клика
 * @reactProps {boolean} isExpanded - флаг видимости popUp
 */

export type Props = {
    activeValueId: string | number,
    autoFocus?: boolean,
    closePopUp?(arg: boolean): void,
    disabled?: boolean,
    disabledValues?: Array<Exclude<Props['value'], void>>,
    isExpanded?: boolean,
    labelFieldId: string,
    inputLabelFieldId?: string,
    enabledFieldId?: string,
    maxTagCount?: number,
    maxTagTextLength?: number,
    mode?: 'autocomplete',
    multiSelect?: boolean,
    onBlur?(): void,
    onClick?(): void,
    onFocus?(): void,
    onInputChange?(arg: string, fetch?: boolean): void,
    onKeyDown?(evt: KeyboardEvent<HTMLTextAreaElement | HTMLInputElement>): void,
    onRemoveItem(item: TOption, index?: number | null): void,
    onSelect(arg?: TOption | Props['value']): void,
    openPopUp?(arg: boolean): void,
    options: TOption[],
    placeholder?: string,
    selected: Props['options'],
    setActiveValueId(id: string | number | null): void,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    setRef?(arg: any): (arg2: any) => void,
    tags?: boolean,
    value?: string | number,
    valueFieldId: string
    className?: string
    readOnly?: boolean
    elementAttributes?: {
        close?: ReactNode | string
    },
    mask?: string
    maskPasteMode?: MaskPasteMode
    clearOnBlur?: boolean
}

export function MaskedInputContent({
    disabled = false,
    value,
    placeholder,
    onRemoveItem,
    onFocus,
    onBlur,
    onSelect,
    selected,
    labelFieldId,
    inputLabelFieldId,
    enabledFieldId,
    valueFieldId,
    multiSelect = false,
    onInputChange = NOOP_FUNCTION,
    openPopUp = NOOP_FUNCTION,
    closePopUp = NOOP_FUNCTION,
    activeValueId,
    setActiveValueId,
    disabledValues = EMPTY_ARRAY,
    options,
    onClick = NOOP_FUNCTION,
    onKeyDown = NOOP_FUNCTION,
    isExpanded,
    autoFocus = false,
    setRef,
    tags,
    mode,
    maxTagTextLength,
    maxTagCount,
    className,
    readOnly = false,
    elementAttributes = {},
    mask = '',
    maskPasteMode,
    clearOnBlur,
}: Props) {
    const [freePasteMode, setFreePasteMode] = useState(false)

    const config = useMemo(() => {
        if (!mask) { return { mask: [], placeholder: '' } }

        const maskArray = Array.from(mask)

        return {
            mask: maskArray.map(char => (char === '9' ? /\d/ : char)),
            placeholder: maskArray.map(char => (char === '9' ? '_' : char)).join(('')),
        }
    }, [mask])

    const { maskRef, maskedValue } = useMask({
        mask: config.mask,
        placeholder: config.placeholder,
        defaultValue: value,
    })

    const handleInputChange = (value: string) => {
        const input = value === null ? '' : value

        onInputChange(input, value !== null)

        if (tags) { setActiveValueId(null) }
    }

    const onBlurHandler = () => {
        if (clearOnBlur && value && !isMaskFilled(mask, `${value}`)) {
            onInputChange('', false)
        }

        onBlur?.()
    }

    const { stateValue, handleChange, handleBlur } = useInputController({
        value: maskedValue,
        onChange: handleInputChange,
        onBlur: onBlurHandler,
        onMessage: () => {},
        invalidText: '',
        clearOnBlur,
        validate: () => true,
        className,
        placeholder: config.placeholder,
    })

    const setOnlyElementFound = () => {
        if (mode !== 'autocomplete' && !multiSelect && options.length === 1) {
            const active: TOption = options[0]
            const currentActive = selected[0] || {}
            const { id } = active
            const { id: currentId } = currentActive

            if (currentId !== id) {
                onSelect(active)
                setActiveValueId(id)
            }
        }
    }

    /**
     * Обработчик изменения инпута при нажатии на клавишу
     * @param e - событие изменения
     * @private
     */
    const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement | HTMLInputElement>) => {
        if (onKeyDown) {
            onKeyDown(e)
        }
        if (
            e.key === 'Backspace' &&
            selected.length &&
            !extractRealData((e.target as HTMLInputElement).value, mask)
        ) {
            if (!multiSelect) {
                onRemoveItem(selected[0])
                setActiveValueId(null)
            } else {
                const endElementOfSelect = selected[selected.length - 1]

                onRemoveItem(endElementOfSelect)
            }
        } else if (e.key === 'ArrowDown') {
            e.preventDefault()
            if (!isExpanded) {
                openPopUp(true)
                setActiveValueId(
                    getFirstNotDisabledId(
                        options,
                        selected,
                        disabledValues,
                        valueFieldId,
                    ),
                )
            } else if (activeValueId) {
                setActiveValueId(
                    getNextId(
                        options,
                        activeValueId,
                        valueFieldId,
                        selected,
                        disabledValues,
                    ),
                )
            } else {
                setActiveValueId(
                    getFirstNotDisabledId(
                        options,
                        selected,
                        disabledValues,
                        valueFieldId,
                    ),
                )
            }
        } else if (e.key === 'ArrowUp') {
            e.preventDefault()
            setActiveValueId(
                getPrevId(
                    options,
                    activeValueId,
                    valueFieldId,
                    selected,
                    disabledValues,
                ),
            )
        } else if (e.key === 'Enter') {
            e.preventDefault()

            let findEquals = find(options, (item) => {
                if (!activeValueId) {
                    return item[labelFieldId as keyof TOption] === value
                }

                if (mode === 'autocomplete') {
                    return item[valueFieldId as keyof TOption] === activeValueId
                }

                return item.id === activeValueId
            })

            if (findEquals && selected.find(entity => isEqual(entity, findEquals))) { findEquals = undefined }

            if (mode === 'autocomplete') {
                const newSelected = findEquals || (typeof value === 'string' && isMaskFilled(mask, value) && value)

                if (newSelected) {
                    onSelect(newSelected)
                }

                setActiveValueId(null)
            } else if (!isEmpty(findEquals)) {
                onSelect(findEquals)
                setActiveValueId(null)
            }

            setOnlyElementFound()
        } else if (e.key === 'Escape') {
            closePopUp(false)
        }
    }

    const handleClick = () => {
        onClick?.()
    }

    const getPlaceholder = selected.length > 0 ? '' : placeholder

    const handlePaste = (e: React.ClipboardEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        if (!mask || maskPasteMode === MaskPasteMode.STRICT) { return }

        e.preventDefault()
        const pastedText = e.clipboardData.getData('text/plain')

        if (!pastedText) { return }

        setFreePasteMode(true)

        const target = e.currentTarget

        target.value = pastedText

        onInputChange(pastedText)
    }

    const mergedRef = useCallback((node) => {
        if (typeof setRef === 'function') { setRef(node) }
        if (typeof maskRef === 'function') { maskRef(node) }
    }, [setRef, maskRef])

    return (
        // eslint-disable-next-line react/jsx-no-useless-fragment
        <>
            {multiSelect ? (
                <>
                    <SelectedItems
                        selected={selected}
                        labelFieldId={labelFieldId}
                        inputLabelFieldId={inputLabelFieldId}
                        enabledFieldId={enabledFieldId}
                        onRemoveItem={onRemoveItem}
                        disabled={disabled}
                        maxTagTextLength={maxTagTextLength}
                        maxTagCount={maxTagCount}
                        close={elementAttributes.close}
                    />
                    <textarea
                        onKeyDown={handleKeyDown}
                        ref={mask ? mergedRef : setRef}
                        placeholder={getPlaceholder}
                        disabled={disabled}
                        value={freePasteMode ? value : stateValue}
                        title={value ? String(value) : ''}
                        // @ts-ignore будет удалено
                        onChange={mask ? NOOP_FUNCTION : handleChange}
                        // @ts-ignore будет удалено
                        onInput={mask ? handleChange : NOOP_FUNCTION}
                        onClick={handleClick}
                        onFocus={onFocus}
                        // @ts-ignore будет удалено
                        onBlur={handleBlur}
                        className={classNames('form-control n2o-inp', { 'n2o-inp--multi': multiSelect })}
                        autoFocus={autoFocus}
                        onPaste={handlePaste}
                    />
                </>
            ) : (
                <input
                    onKeyDown={handleKeyDown}
                    ref={mask ? mergedRef : setRef}
                    placeholder={getPlaceholder}
                    disabled={disabled}
                    title={value ? String(value) : ''}
                    value={freePasteMode ? value : stateValue}
                    // @ts-ignore будет удалено
                    onChange={mask ? NOOP_FUNCTION : handleChange}
                    // @ts-ignore будет удалено
                    onInput={mask ? handleChange : NOOP_FUNCTION}
                    onClick={handleClick}
                    onFocus={onFocus}
                    onBlur={handleBlur}
                    type="text"
                    className={classNames(className, { 'form-control n2o-inp': !readOnly })}
                    autoFocus={autoFocus}
                    autoComplete="nope"
                    readOnly={readOnly}
                />
            )}
        </>
    )
}
