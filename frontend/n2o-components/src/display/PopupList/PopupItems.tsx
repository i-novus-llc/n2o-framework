import React, { MouseEvent, useCallback } from 'react'
import isNil from 'lodash/isNil'
import get from 'lodash/get'
import classNames from 'classnames'
import { DropdownItem } from 'reactstrap'
import { findDOMNode } from 'react-dom'
import scrollIntoView from 'scroll-into-view-if-needed'

import { StatusText } from '../StatusText/StatusText'
import { Badge } from '../Badge/Badge'
import { isBadgeLeftPosition, isBadgeRightPosition, resolveBadgeProps } from '../Badge/utils'
import { Checkbox } from '../../inputs/Checkbox/Checkbox'
import { Shape } from '../Badge/enums'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../../utils/emptyTypes'

import { Option, type PopUpItemsProps } from './types'
import { groupData, contains } from './utils'
import { UNKNOWN_GROUP_FIELD_ID } from './constants'
import { PopupIcon, PopupImage } from './snippets'

/**
 * Компонент попапа
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
    loading = false,
    options = EMPTY_ARRAY,
    labelFieldId = 'label',
    iconFieldId = '',
    valueFieldId = 'id',
    imageFieldId = '',
    descriptionFieldId = '',
    statusFieldId = '',
    enabledFieldId = '',
    selected = EMPTY_ARRAY,
    groupFieldId = '',
    hasCheckboxes = false,
    format,
    badge,
    onRemoveItem = NOOP_FUNCTION,
    onSelect = NOOP_FUNCTION,
    setActiveValueId = NOOP_FUNCTION,
    activeValueId = null,
    autocomplete = false,
    renderIfEmpty = true,
    popUpItemRef,
    multiSelect = false,
    searchMinLengthHint = null,
    isExpanded,
}: PopUpItemsProps) {
    const handleRef = (item: React.ReactInstance | null | undefined) => {
        if (loading || !isExpanded) { return }

        if (item) {
            // eslint-disable-next-line react/no-find-dom-node
            const el = findDOMNode(item) as Element

            if (el?.classList?.contains('active')) {
                scrollIntoView(el, { scrollMode: 'if-needed', block: 'nearest' })
            }
        }
    }

    const handleItemClick = ({ target }: MouseEvent<HTMLElement>, item: Option) => {
        if ((target as HTMLInputElement).nodeName === 'LABEL') { return }
        if (contains(selected, item)) {
            onRemoveItem(item)
        } else {
            onSelect(item)
        }
    }

    const withStatus = (item: Option) => !isNil(get(item, statusFieldId))

    const displayTitle = (item: Option) => {
        if (item.formattedTitle) { return item.formattedTitle }

        const text = get(item, labelFieldId)

        if (withStatus(item)) {
            const color = get(item, statusFieldId)

            return <StatusText text={text} color={color} textPosition="left" />
        }

        return text
    }

    const getDisabled = (item: Option, isSelected: boolean) => {
        if (isSelected && !multiSelect && !hasCheckboxes) { return true }
        const enabledField = get(item, enabledFieldId)

        return !isNil(enabledField) ? !enabledField : false
    }

    const onMouseOver = (item: Option) => setActiveValueId?.(get(item, valueFieldId))

    const onMouseLeave = useCallback(() => {
        setActiveValueId?.('')
    }, [setActiveValueId])

    const renderLabel = (item: Option) => (
        <span className="n2o-input-select__label text-cropped">{displayTitle(item)}</span>
    )

    const renderSingleItem = (item: Option, index: string | number) => {
        const { fieldId: badgeFieldId, position: badgePosition } = badge || {}
        const shouldRenderBadge = badgeFieldId && badgePosition && badge
        const isSelected = contains(selected, item)

        const disabled = getDisabled(item, isSelected)
        const description = get(item, descriptionFieldId)
        const withDescription = !isNil(description)
        const icon = get(item, iconFieldId)

        const enabled = get(item, enabledFieldId, true)

        return (
            <div ref={popUpItemRef}>
                <DropdownItem
                    ref={handleRef}
                    className={classNames('n2o-eclipse-content', {
                        active: activeValueId === get(item, valueFieldId) && !disabled,
                        selected: isSelected && !hasCheckboxes,
                        'no-available': !enabled,
                        'with-status': withStatus(item),
                        'with-description': withDescription,
                    })}
                    onMouseOver={() => onMouseOver(item)}
                    onMouseLeave={onMouseLeave}
                    disabled={disabled || loading}
                    key={index}
                    onClick={e => handleItemClick(e, item)}
                    title={displayTitle(item)}
                    toggle={false}
                >
                    <div>
                        <PopupIcon item={item} iconFieldId={iconFieldId} />
                        <PopupImage item={item} imageFieldId={imageFieldId} />
                        {shouldRenderBadge && isBadgeLeftPosition(badgePosition) && (
                            <Badge
                                key="badge-left"
                                {...resolveBadgeProps(badge, item as unknown as Record<string, string>)}
                                shape={badge?.shape || Shape.Square}
                            />
                        )}
                        {hasCheckboxes ? (
                            <Checkbox
                                value={isSelected}
                                label={displayTitle(item)}
                                tabIndex={-1}
                                inline
                            />
                        ) : (
                            renderLabel(item)
                        )}
                        {shouldRenderBadge && isBadgeRightPosition(badgePosition) && (
                            <Badge
                                key="badge-right"
                                {...resolveBadgeProps(badge, item as unknown as Record<string, string>)}
                                shape={badge?.shape || Shape.Square}
                            />
                        )}
                    </div>
                    {withDescription && (
                        <DropdownItem
                            header
                            className={classNames('n2o-eclipse-content__description', {
                                'n2o-eclipse-content__description-with-icon': !hasCheckboxes && icon,
                                'n2o-eclipse-content__description-with-checkbox': hasCheckboxes && !icon,
                                'n2o-eclipse-content__description-with-icon-checkbox': hasCheckboxes && icon,
                            })}
                        >
                            {description}
                        </DropdownItem>
                    )}
                </DropdownItem>
            </div>
        )
    }

    const renderSingleItems = (items: Option[]) => items.map((item, i) => renderSingleItem(item, i))

    const renderGroup = (key: string, value: Option[]) => (
        <React.Fragment key={key}>
            {key && key !== UNKNOWN_GROUP_FIELD_ID && <DropdownItem key={key} header>{key}</DropdownItem>}
            {renderSingleItems(value)}
            <DropdownItem divider />
        </React.Fragment>
    )

    const renderGroupedItems = (items: Option[], fieldId: string) => {
        const groupedData = groupData(items, fieldId)

        return Object.keys(groupedData).map(key => renderGroup(key, groupedData[key]))
    }

    const renderMenuItems = (items: Option[]) => (
        groupFieldId ? renderGroupedItems(items, groupFieldId) : renderSingleItems(items)
    )

    const renderMenu = (items: Option[]) => {
        if (searchMinLengthHint) { return <DropdownItem header>{searchMinLengthHint}</DropdownItem> }
        if (!loading && items.length === 0 && !renderIfEmpty) { return null }
        if (items?.[0] !== null && items.length) { return renderMenuItems(items) }
        if (!loading && items.length === 0) { return <DropdownItem header>Ничего не найдено</DropdownItem> }

        return null
    }

    return <>{renderMenu(options)}</>
}

PopupItems.displayName = 'PopupItems'
