import React, { useCallback, useMemo } from 'react'
import classNames from 'classnames'
import map from 'lodash/map'
import split from 'lodash/split'
import { useTranslation } from 'react-i18next'

import { Props as InputContentProps } from './InputContent'
import { TOption } from './types'

const SelectedItem = ({ id, title, callback, maxTagTextLength, disabled }: SelectedItemProps) => {
    const callbackWrapper = useCallback((event) => {
        event.stopPropagation()
        callback(event)
    }, [callback])
    const truncatedTitle = `${split(title, '', maxTagTextLength).join('')}...`
    const tagTitle = maxTagTextLength && title.length > maxTagTextLength
        ? truncatedTitle
        : title

    return (
        <span
            key={id}
            className={classNames('selected-item n2o-multiselect', {
                'max-text-length': maxTagTextLength,
            })}
            title={title}
        >
            <span className="n2o-eclipse-content">{tagTitle}</span>
            <button
                type="button"
                className="close"
                onClick={callbackWrapper}
                disabled={disabled}
            >
                <i className="fa fa-times fa-1" />
            </button>
        </span>
    )
}

type SelectedItemProps = {
    callback(arg: Event): void,
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
 * @reactProps {function} onDeleteAll - callback удаление всех выбранных элементов при мульти выборе
 * @reactProps {boolean} collapseSelected - флаг сжатия выбранных элементов
 * @reactProps {number} lengthToGroup - от скольки элементов сжимать выбранные элементы
 * @reactProps {number} maxTagTextLength - максимальная длина текста в тэге, до усечения
 */

type InputElementsProps = Pick<InputContentProps,
'selected' | 'labelFieldId' | 'onRemoveItem' | 'collapseSelected' | 'lengthToGroup' | 'maxTagTextLength' | 'disabled'> &
{onDeleteAll: InputContentProps['clearSelected']}

export function InputElements({
    onRemoveItem,
    selected,
    labelFieldId,
    disabled,
    collapseSelected,
    lengthToGroup,
    onDeleteAll,
    maxTagTextLength,
}: InputElementsProps) {
    const { t } = useTranslation()

    if (collapseSelected && selected.length > lengthToGroup) {
        const id = selected.length
        const title = `${t('selected')} ${selected.length}`

        return (
            <SelectedItem
                id={id}
                title={title}
                callback={onDeleteAll}
                maxTagTextLength={maxTagTextLength}
                disabled={disabled}
            />
        )
    }

    const getTitle = (item: TOption, labelFieldId: InputElementsProps['labelFieldId']) => {
        if (typeof item === 'string') {
            return item
        }

        return item[labelFieldId as keyof TOption] || ''
    }

    const list = map(selected, (item, index) => {
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

    return <>{list}</>
}
