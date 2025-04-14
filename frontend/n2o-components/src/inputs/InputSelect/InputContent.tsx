import React, { ChangeEvent, KeyboardEvent } from 'react'
import find from 'lodash/find'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { InputElements as SelectedItems } from './SelectedItems'
import { getNextId, getPrevId, getFirstNotDisabledId } from './utils'
import { TOption } from './types'

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
    maxTagCount?: number,
    maxTagTextLength?: number,
    mode?: 'autocomplete',
    multiSelect?: boolean,
    onBlur?(): void,
    onClick?(): void,
    onFocus?(): void,
    onInputChange?(arg: string): void,
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
}

const noop = () => {}

export function InputContent({
    disabled = false,
    value,
    placeholder,
    onRemoveItem,
    onFocus,
    onBlur,
    onSelect,
    selected,
    labelFieldId,
    valueFieldId,
    multiSelect = false,
    onInputChange = noop,
    openPopUp = noop,
    closePopUp = noop,
    activeValueId,
    setActiveValueId,
    disabledValues = [],
    options,
    onClick = noop,
    onKeyDown = noop,
    isExpanded,
    autoFocus = false,
    setRef,
    tags,
    mode,
    maxTagTextLength,
    maxTagCount,
    className,
    readOnly = false,
}: Props) {
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
            !(e.target as HTMLInputElement).value
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
                const newSelected = findEquals || value

                onSelect(newSelected)
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
        if (onClick) {
            onClick()
        }
    }

    /**
     * Обработчик изменения инпута
     * @param e - событие изменения
     * @private
     */

    const handleInputChange = (e: ChangeEvent) => {
        onInputChange((e.target as HTMLInputElement).value)

        if (tags) {
            setActiveValueId(null)
        }
    }

    const getPlaceholder = selected.length > 0 ? '' : placeholder

    return (
        // eslint-disable-next-line react/jsx-no-useless-fragment
        <>
            {multiSelect ? (
                <>
                    <SelectedItems
                        selected={selected}
                        labelFieldId={labelFieldId}
                        onRemoveItem={onRemoveItem}
                        disabled={disabled}
                        maxTagTextLength={maxTagTextLength}
                        maxTagCount={maxTagCount}
                    />
                    <textarea
                        onKeyDown={handleKeyDown}
                        ref={setRef}
                        placeholder={getPlaceholder}
                        disabled={disabled}
                        value={value}
                        title={String(value)}
                        onChange={handleInputChange}
                        onClick={handleClick}
                        onFocus={onFocus}
                        onBlur={onBlur}
                        className={classNames('form-control n2o-inp', {
                            'n2o-inp--multi': multiSelect,
                        })}
                        autoFocus={autoFocus}
                    />
                </>
            ) : (
                <input
                    onKeyDown={handleKeyDown}
                    ref={setRef}
                    placeholder={getPlaceholder}
                    disabled={disabled}
                    title={String(value)}
                    value={value}
                    onChange={handleInputChange}
                    onClick={handleClick}
                    onFocus={onFocus}
                    onBlur={onBlur}
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
