import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import isNil from 'lodash/isNil'
import isUndefined from 'lodash/isUndefined'
import some from 'lodash/some'
import keys from 'lodash/keys'
import values from 'lodash/values'
import map from 'lodash/map'
import forEach from 'lodash/forEach'
import toString from 'lodash/toString'
import isEqual from 'lodash/isEqual'
import cx from 'classnames'
import { DropdownItem } from 'reactstrap'

import propsResolver from '../../../utils/propsResolver'
import { Icon } from '../../snippets/Icon/Icon'
import { StatusText } from '../../snippets/StatusText/StatusText'
import { Badge } from '../../snippets/Badge/Badge'
import { isBadgeLeftPosition, isBadgeRightPosition, resolveBadgeProps } from '../../snippets/Badge/utils'
// eslint-disable-next-line import/no-named-as-default
import CheckboxN2O from '../Checkbox/CheckboxN2O'
import { Shape } from '../../snippets/Badge/enums'

import {
    groupData,
    inArray,
    isDisabled,
} from './utils'
import { UNKNOWN_GROUP_FIELD_ID } from './constants'

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

function PopupItems({
    loading,
    options,
    labelFieldId,
    iconFieldId,
    valueFieldId,
    imageFieldId,
    descriptionFieldId,
    statusFieldId,
    enabledFieldId,
    disabledValues,
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
}) {
    // eslint-disable-next-line consistent-return
    const handleItemClick = ({ target }, item) => {
        if (target.nodeName === 'LABEL') { return false }
        if (inArray(selected, item)) {
            onRemoveItem(item)
        } else {
            onSelect(item)
        }
    }

    const withStatus = item => !isNil(item[statusFieldId])

    const displayTitle = (item) => {
        if (format) { return propsResolver({ format }, item).format }
        if (withStatus(item)) {
            return (
                <StatusText
                    text={item[labelFieldId]}
                    color={item[statusFieldId]}
                    textPosition="left"
                />
            )
        }

        return item[labelFieldId]
    }

    const isSelectedItem = (selected, item) => {
        const valuesToString = (object) => {
            const acc = {}

            const objectKeys = keys(object)
            const objectValues = values(object)

            forEach(objectKeys, (key, index) => {
                acc[key] = toString(objectValues[index])
            })

            return acc
        }

        const itemToCompare = valuesToString(item)
        const selectedToCompare = map(selected, selectedItem => valuesToString(selectedItem))

        return some(selectedToCompare, selectedItem => isEqual(selectedItem, itemToCompare))
    }

    const getDisabled = (item) => {
        if (!isNil(item[enabledFieldId])) {
            return !item[enabledFieldId]
        }

        if (isSelectedItem(selected, item) && !hasCheckboxes) {
            return true
        }

        return !hasCheckboxes && isDisabled(
            autocomplete ? item[valueFieldId] : item,
            selected,
            disabledValues,
        )
    }

    const onMouseOver = (item) => {
        if (setActiveValueId) {
            setActiveValueId(item[valueFieldId])
        }
    }

    const onMouseLeave = useCallback(() => {
        if (setActiveValueId) {
            setActiveValueId('')
        }
    }, [setActiveValueId])

    const renderSingleItem = (item, index) => {
        const {
            fieldId: badgeFieldId,
            position: badgePosition,
        } = badge || {}

        const disabled = getDisabled(item)

        return (
            <div ref={popUpItemRef}>
                <DropdownItem
                    className={cx('n2o-eclipse-content', {
                        active: activeValueId === item[valueFieldId] && !disabled,
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
                    {iconFieldId && renderIcon(item, iconFieldId)}
                    {imageFieldId && renderImage(item, imageFieldId)}
                    {badgeFieldId && isBadgeLeftPosition(badgePosition) && <Badge key="badge-left" {...resolveBadgeProps(badge, item)} shape={badge?.shape || Shape.Square} />}
                    {hasCheckboxes ? renderCheckbox(item, selected) : renderLabel(item)}
                    {badgeFieldId && isBadgeRightPosition(badgePosition) && <Badge key="badge-right" {...resolveBadgeProps(badge, item)} shape={badge?.shape || Shape.Square} />}
                    {descriptionFieldId && !isUndefined(item[descriptionFieldId]) && (
                        <DropdownItem
                            className={cx('n2o-eclipse-content__description', {
                                'n2o-eclipse-content__description-with-icon':
                                !hasCheckboxes && item[iconFieldId],
                                'n2o-eclipse-content__description-with-checkbox':
                                hasCheckboxes && !item[iconFieldId],
                                'n2o-eclipse-content__description-with-icon-checkbox':
                                hasCheckboxes && item[iconFieldId],
                            })}
                            header
                        >
                            {item[descriptionFieldId]}
                        </DropdownItem>
                    )}
                </DropdownItem>
            </div>
        )
    }

    const renderIcon = (item, iconFieldId) => item[iconFieldId] && <Icon name={item[iconFieldId]} />
    // eslint-disable-next-line jsx-a11y/alt-text
    const renderImage = (item, imageFieldId) => item[imageFieldId] && <img src={item[imageFieldId]} />
    const renderCheckbox = (item, selected) => (
        <CheckboxN2O
            value={inArray(selected, item)}
            label={displayTitle(item)}
            inline
            tabIndex={-1}
        />
    )
    const renderLabel = item => (
        <span className="n2o-input-select__label text-cropped">{displayTitle(item)}</span>
    )

    const renderSingleItems = options => options.map((item, i) => renderSingleItem(item, i))
    const renderGroupedItems = (options, groupFieldId) => {
        const groupedData = groupData(options, groupFieldId)

        return Object.keys(groupedData).map(key => renderGroup(key, groupedData[key]))
    }

    const renderGroup = (key, value) => (
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

    const renderMenuItems = options => (groupFieldId
        ? renderGroupedItems(options, groupFieldId)
        : renderSingleItems(options))

    // eslint-disable-next-line consistent-return
    const renderMenu = (options) => {
        if (!loading && options.length === 0 && !renderIfEmpty) {
            return null
        }
        if (options && options[0] !== null && options.length) {
            return renderMenuItems(options)
        }
        if (!loading && options.length === 0) {
            return <DropdownItem header>Ничего не найдено</DropdownItem>
        }
    }

    return <>{renderMenu(options)}</>
}

PopupItems.propTypes = {
    loading: PropTypes.bool,
    descriptionFieldId: PropTypes.string,
    enabledFieldId: PropTypes.string,
    statusFieldId: PropTypes.string,
    options: PropTypes.array.isRequired,
    valueFieldId: PropTypes.string.isRequired,
    labelFieldId: PropTypes.string.isRequired,
    iconFieldId: PropTypes.string,
    imageFieldId: PropTypes.string,
    groupFieldId: PropTypes.string,
    badge: PropTypes.shape({
        fieldId: PropTypes.string,
        colorFieldId: PropTypes.string,
        position: PropTypes.string,
        shape: PropTypes.string,
        imageFieldId: PropTypes.string,
        imagePosition: PropTypes.string,
        imageShape: PropTypes.string,
    }),
    disabledValues: PropTypes.array,
    onSelect: PropTypes.func,
    selected: PropTypes.array,
    hasCheckboxes: PropTypes.bool,
    onRemoveItem: PropTypes.func,
    format: PropTypes.string,
    setActiveValueId: PropTypes.func,
    activeValueId: PropTypes.string,
    autocomplete: PropTypes.bool,
    renderIfEmpty: PropTypes.bool,
}

PopupItems.defaultProps = {
    renderIfEmpty: true,
    setActiveValueId: () => {},
}

export default PopupItems
