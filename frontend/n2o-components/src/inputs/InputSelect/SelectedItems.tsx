import React, { ReactNode, useCallback, useMemo } from 'react'
import classNames from 'classnames'
import split from 'lodash/split'
import { useTranslation } from 'react-i18next'

import { Props as InputContentProps } from './InputContent'
import { TOption } from './types'

const SelectedItem = ({ id, title, callback, maxTagTextLength, disabled }: SelectedItemProps) => {
    const onClear = useCallback((event) => {
        event.stopPropagation()
        callback?.(event)
    }, [callback])
    const truncatedTitle = `${split(title, '', maxTagTextLength).join('')}...`
    const tagTitle = maxTagTextLength && title.length > maxTagTextLength
        ? truncatedTitle
        : title
    const button = callback ? (
        <button
            type="button"
            className="close"
            onClick={onClear}
            disabled={disabled}
        >
            <i className="fa fa-times fa-1" />
        </button>
    ) : null

    return (
        <span
            key={id}
            className={classNames('selected-item n2o-multiselect', {
                'max-text-length': maxTagTextLength,
            })}
            title={title}
        >
            <span className="n2o-eclipse-content">{tagTitle}</span>
            {button}
        </span>
    )
}

type SelectedItemProps = {
    callback?(arg: Event): void,
    disabled: boolean,
    id: string | number,
    maxTagTextLength: number,
    title: string
}

const ItemWrapper = ({ onRemoveItem, item, index, ...props }: ItemWrapperProps) => {
    const onRemove = useMemo(
        () => () => onRemoveItem.call(null, item, index),
        [item, index, onRemoveItem],
    )

    return (
        <SelectedItem
            {...props}
            callback={onRemove}
        />
    )
}

type ItemWrapperProps = Pick<InputElementsProps, 'onRemoveItem' | 'maxTagTextLength' | 'disabled'> & {
    id: string | number,
    index: number,
    item: TOption,
    title: string
}

/**
 * Компонент выбранных элементов для {@Link InputSelectGroup}
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {function} onRemoveItem - callback при нажатии на удаление элемента из выбранных при мульти выборе
 * @reactProps {number} [maxTagCount] - от скольки элементов сжимать выбранные элементы
 * @reactProps {number} maxTagTextLength - максимальная длина текста в тэге, до усечения
 */

type InputElementsProps = Pick<InputContentProps,
'selected' | 'labelFieldId' | 'onRemoveItem' | 'maxTagTextLength' | 'maxTagCount' | 'disabled'>

const getTitle = (item: TOption, labelFieldId: InputElementsProps['labelFieldId']) => {
    if (typeof item === 'string') {
        return item
    }

    return item[labelFieldId as keyof TOption] || ''
}

export function InputElements({
    onRemoveItem,
    selected,
    labelFieldId,
    disabled,
    maxTagTextLength,
    maxTagCount,
}: InputElementsProps) {
    const { t } = useTranslation()

    if (!selected?.length) { return null }

    let list = selected
    let rest: ReactNode = null

    if (typeof maxTagCount === 'number') {
        if (maxTagCount === 0) {
            const id = selected.length
            const title = `${t('selected')}: ${selected.length}`

            list = []
            rest = (
                <SelectedItem
                    id={id}
                    title={title}
                    maxTagTextLength={maxTagTextLength}
                    disabled={disabled}
                />
            )
        } else if (selected.length > maxTagCount) {
            list = list.slice(0, maxTagCount)
            rest = (
                <SelectedItem
                    id={selected.length}
                    title={`+ ${selected.length - maxTagCount}...`}
                    maxTagTextLength={maxTagTextLength}
                    disabled={disabled}
                />
            )
        }
    }

    const tags = list.map((item, index) => {
        const title = getTitle(item, labelFieldId)
        const id = item.id || index

        return (
            <ItemWrapper
                id={id}
                title={title}
                maxTagTextLength={maxTagTextLength}
                disabled={disabled}
                item={item}
                index={index}
                onRemoveItem={onRemoveItem}
            />
        )
    })

    return (
        <>
            {tags}
            {rest}
        </>
    )
}
