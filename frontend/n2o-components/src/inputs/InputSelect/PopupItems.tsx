import React, { MouseEvent, useCallback } from 'react'
import isNil from 'lodash/isNil'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import cx from 'classnames'
import { DropdownItem } from 'reactstrap'
import { findDOMNode } from 'react-dom'
import scrollIntoView from 'scroll-into-view-if-needed'

import { Icon } from '../../display/Icon'
import { StatusText } from '../../display/StatusText/StatusText'
import { Badge } from '../../display/Badge/Badge'
import { isBadgeLeftPosition, isBadgeRightPosition, resolveBadgeProps } from '../../display/Badge/utils'
import { Checkbox } from '../Checkbox/Checkbox'
import { Shape } from '../../display/Badge/enums'

import { Props as InputContentProps } from './InputContent'
import {
    groupData,
    inArray,
} from './utils'
import { UNKNOWN_GROUP_FIELD_ID } from './constants'
import { BadgeType } from './PopupList'
import { Ref, TOption } from './types'
import { PopupIcon, PopupImage } from './snippets'

/**
 * Компонент попапа для {@link InputSelect}
 * @reactProps {array} options - массив данных
 * @reactProps {string} activeLabel - активный лейбел
 * @reactProps {function} setActiveLabel - смена активного лейбла
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {string} badge - поле баджа
 * @reactProps {string} enabledFieldId - поле для активности
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {function} onSelect - callback при выборе элемента
 * @reactProps {string} format - формат
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {array} selected - выбранные элементы
 * @reactProps {function} onRemoveItem - callback при удаление элемента
 * @reactProps {function} setActiveValueId
 * @reactProps {string} activeValueId
 * @reactProps {boolean} autocomplete
 */

export function PopupItems({
    loading,
    options,
    labelFieldId,
    iconFieldId,
    valueFieldId,
    imageFieldId,
    descriptionFieldId,
    statusFieldId,
    enabledFieldId,
    selected,
    groupFieldId,
    hasCheckboxes,
    format,
    badge,
    onRemoveItem,
    onSelect,
    setActiveValueId,
    activeValueId,
    autocomplete,
    renderIfEmpty,
    popUpItemRef,
}: Props) {
    /* FIXME, костыль для выбора элементов с помощью keyup / keydown, сложности с focus в InputSelect.
         Отвечает за scroll к последнему active элементу, нужно для lazy load см. в PopUpList */
    const handleRef = (item: React.ReactInstance | null | undefined) => {
        if (loading) {
            return
        }

        if (item) {
            // eslint-disable-next-line react/no-find-dom-node
            const el = findDOMNode(item) as Element

            if (el.classList.contains('active')) {
                scrollIntoView(el, { scrollMode: 'if-needed', block: 'nearest' })
            }
        }
    }

    // eslint-disable-next-line consistent-return
    const handleItemClick = ({ target }: MouseEvent<HTMLElement, Event>, item: TOption) => {
        if ((target as HTMLInputElement).nodeName === 'LABEL') { return false }
        if (inArray(selected, item)) {
            onRemoveItem(item)
        } else {
            onSelect(item)
        }
    }

    const withStatus = (item: TOption) => !isNil(get(item, statusFieldId))

    const displayTitle = (item: TOption) => {
        if (item.formattedTitle) {
            return item.formattedTitle
        }

        const text = get(item, labelFieldId)

        if (withStatus(item)) {
            const color = get(item, statusFieldId)

            return (
                <StatusText
                    text={text}
                    color={color}
                    textPosition="left"
                />
            )
        }

        return text
    }

    const getDisabled = (item: TOption) => {
        const enabledField = get(item, enabledFieldId)

        if (!isNil(enabledField)) {
            return !enabledField
        }

        return false
    }

    const onMouseOver = (item: TOption) => {
        if (setActiveValueId) {
            setActiveValueId(get(item, valueFieldId))
        }
    }

    const onMouseLeave = useCallback(() => {
        if (setActiveValueId) {
            setActiveValueId('')
        }
    }, [setActiveValueId])

    const renderSingleItem = (item: TOption, index: string | number) => {
        const {
            fieldId: badgeFieldId,
            position: badgePosition,
        } = badge || {}

        const shouldRenderBadge = badgeFieldId && badgePosition && badge

        const disabled = getDisabled(item)
        const isSelected = inArray(selected, item)

        return (
            <div ref={popUpItemRef}>
                <DropdownItem
                    ref={handleRef}
                    className={cx('n2o-eclipse-content', {
                        active: activeValueId === get(item, valueFieldId) && !disabled,
                        'selected': isSelected && !hasCheckboxes,
                        'n2o-eclipse-content__with-status': withStatus(item),
                    })}
                    onMouseOver={() => onMouseOver(item)}
                    onMouseLeave={onMouseLeave}
                    disabled={disabled}
                    key={index}
                    onClick={e => handleItemClick(e, item)}
                    title={displayTitle(item)}
                    toggle={false}
                >
                    <PopupIcon item={item} iconFieldId={iconFieldId} />
                    <PopupImage item={item} imageFieldId={imageFieldId} />
                    {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
                    {shouldRenderBadge && isBadgeLeftPosition(badgePosition) && <Badge key="badge-left" {...resolveBadgeProps(badge, item as any)} shape={badge?.shape || Shape.Square} />}
                    {hasCheckboxes ? (
                        <Checkbox
                            value={isSelected}
                            label={displayTitle(item)}
                            inline
                            tabIndex={-1}
                        />
                        ) : renderLabel(item)
                    }
                    {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
                    {shouldRenderBadge && isBadgeRightPosition(badgePosition) && <Badge key="badge-right" {...resolveBadgeProps(badge, item as any)} shape={badge?.shape || Shape.Square} />}
                    {descriptionFieldId && !isUndefined(get(item, descriptionFieldId)) && (
                        <DropdownItem
                            className={cx('n2o-eclipse-content__description', {
                                'n2o-eclipse-content__description-with-icon':
                                !hasCheckboxes && get(item, iconFieldId),
                                'n2o-eclipse-content__description-with-checkbox':
                                hasCheckboxes && !get(item, iconFieldId),
                                'n2o-eclipse-content__description-with-icon-checkbox':
                                hasCheckboxes && get(item, iconFieldId),
                            })}
                            header
                        >
                            {get(item, descriptionFieldId)}
                        </DropdownItem>
                    )}
                </DropdownItem>
            </div>
        )
    }

    const renderLabel = (item: TOption) => (
        <span className="n2o-input-select__label text-cropped">{displayTitle(item)}</span>
    )

    const renderSingleItems = (options: Props['options']) => options.map((item, i) => renderSingleItem(item, i))
    const renderGroupedItems = (options: Props['options'], groupFieldId: Props['groupFieldId']) => {
        const groupedData = groupData(options, groupFieldId)

        return Object.keys(groupedData).map(key => renderGroup(key, groupedData[key]))
    }

    const renderGroup = (key: string, value: Props['options']) => (
        <React.Fragment key={key}>
            {key && key !== UNKNOWN_GROUP_FIELD_ID ? (
                <DropdownItem key={key} header>
                    {key}
                </DropdownItem>
            ) : null}
            {renderSingleItems(value)}
            <DropdownItem divider />
        </React.Fragment>
    )

    const renderMenuItems = (options: Props['options']) => (groupFieldId
        ? renderGroupedItems(options, groupFieldId)
        : renderSingleItems(options))

    // eslint-disable-next-line consistent-return
    const renderMenu = (options: Props['options']) => {
        if (!loading && options.length === 0 && !renderIfEmpty) {
            return null
        }
        if (options?.[0] !== null && options.length) {
            return renderMenuItems(options)
        }
        if (!loading && options.length === 0) {
            return <DropdownItem header>Ничего не найдено</DropdownItem>
        }
    }

    return <>{renderMenu(options)}</>
}

type Props = {
    activeValueId?: string | number | null,
    autocomplete: boolean,
    badge?: BadgeType,
    descriptionFieldId: string,
    enabledFieldId: string,
    format?: string,
    groupFieldId: string,
    hasCheckboxes: boolean,
    iconFieldId: string,
    imageFieldId: string,
    labelFieldId: string,
    loading: boolean,
    onRemoveItem: InputContentProps['onRemoveItem'],
    onSelect: InputContentProps['onSelect'],
    options: InputContentProps['options'],
    popUpItemRef?: Ref,
    renderIfEmpty: boolean,
    selected: InputContentProps['selected'],
    setActiveValueId: InputContentProps['setActiveValueId'],
    statusFieldId: string,
    valueFieldId: string
}

PopupItems.defaultProps = {
    renderIfEmpty: true,
    setActiveValueId: () => {},
    loading: false,
    onSelect: () => {},
    onRemoveItem: () => {},
    options: [],
    selected: [],
    autocomplete: false,
    hasCheckboxes: false,
    descriptionFieldId: '',
    enabledFieldId: '',
    statusFieldId: '',
    valueFieldId: '',
    labelFieldId: '',
    iconFieldId: '',
    imageFieldId: '',
    groupFieldId: '',
} as Props
